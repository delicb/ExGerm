package tk.exgerm.graphtree.listeners;

import java.util.ArrayList;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;
import tk.exgerm.graphtree.model.Graph;
import tk.exgerm.graphtree.model.Node;
import tk.exgerm.graphtree.model.SubGraph;

public class NodeCreatedListener implements IListener {

	private GraphTree graphTree;

	public NodeCreatedListener(GraphTree graphTree, ICoreContext context) {
		this.graphTree = graphTree;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals(IGraph.GRAPH_NODE_ADDED)){
			
			Graph g = new Graph((IGraph)parameters[0]);
			
			//Pravimo i popunjavamo listu svih nadgrafova
			ArrayList<IGraph> parents = new ArrayList<IGraph>();
			parents.add(((IGraph)parameters[0]));
			
			while(g.getGraph().getGraph() != null){
				parents.add(g.getGraph().getGraph());
				g = new Graph(g.getGraph().getGraph());
			}
			
			//uzimamo root graf i u zavisnosti od broja nivoa podgrafova nalazim
			//graf u koji treba ubaciti prosledjeni INode
			Graph root = graphTree.getRoot().getGraph(parents.get(parents.size() - 1).getName());
			if(root != null){
				if(parents.size() == 1){
					if(parameters[1] instanceof IGraph){
						root.getNodeSeparator().addNode(new SubGraph((IGraph)parameters[1]));
					}else if(parameters[1] instanceof INode){
						root.getNodeSeparator().addNode(new Node((INode)parameters[1]));
					}				
				}else if(parents.size() == 2){
					if(parameters[1] instanceof IGraph){
						root.getSubGraph((IGraph)parameters[0]).getNodeSeparator().addNode(new SubGraph((IGraph)parameters[1]));
					}else if(parameters[1] instanceof INode){
						root.getSubGraph((IGraph)parameters[0]).getNodeSeparator().addNode(new Node((INode)parameters[1]));
					}
				}else{
					SubGraph sub = root.getSubGraph(parents.get(parents.size() - 2));
					for(int i = parents.size() - 3; i != -1; i--){
						sub = sub.getSubGraph(parents.get(i));
					}
					if(parameters[1] instanceof IGraph){
						sub.getNodeSeparator().addNode(new SubGraph((IGraph)parameters[1]));
					}else if(parameters[1] instanceof INode){
						sub.getNodeSeparator().addNode(new Node((INode)parameters[1]));
					}
				}
			}
			graphTree.updateUI();
		}
	}

}