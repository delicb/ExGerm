package tk.exgerm.graphtree.listeners;

import java.util.ArrayList;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;
import tk.exgerm.graphtree.model.Edge;
import tk.exgerm.graphtree.model.Graph;
import tk.exgerm.graphtree.model.SubGraph;

public class EdgeCreatedListener implements IListener {

	private GraphTree graphTree;

	public EdgeCreatedListener(GraphTree graphTree, ICoreContext context) {
		this.graphTree = graphTree;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals(IGraph.GRAPH_EDGE_ADDED)){
			Graph g = new Graph((IGraph)parameters[0]);
			
			//Pravimo i popunjavamo listu svih nadgrafova
			ArrayList<IGraph> parents = new ArrayList<IGraph>();
			parents.add(((IGraph)parameters[0]));
			
			while(g.getGraph().getGraph() != null){
				parents.add(g.getGraph().getGraph());
				g = new Graph(g.getGraph().getGraph());
			}
			
			//uzimamo root graf i u zavisnosti od broja nivoa podgrafova nalazim
			//graf u koji treba ubaciti prosledjeni IEdge
			Graph root = graphTree.getRoot().getGraph(parents.get(parents.size() - 1).getName());
			if(root != null){
				if(parents.size() == 1){
					root.getEdgeSeparator().addEdge(new Edge((IEdge)parameters[1]));			
				}else if(parents.size() == 2){
					root.getSubGraph((IGraph)parameters[0]).getEdgeSeparator().addEdge(new Edge((IEdge)parameters[1]));
				}else{
					SubGraph sub = root.getSubGraph(parents.get(parents.size() - 2));
					for(int i = parents.size() - 3; i != -1; i--){
						sub = sub.getSubGraph(parents.get(i));
					}
					sub.getEdgeSeparator().addEdge(new Edge((IEdge)parameters[1]));
				}
			}
			graphTree.updateUI();
		}
	}

}
