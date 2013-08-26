package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;
import tk.exgerm.graphtree.model.Graph;

public class GraphCreatedListener implements IListener{

	private GraphTree graphTree;
	
	public GraphCreatedListener(GraphTree graphTree, ICoreContext context){
		this.graphTree = graphTree;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if (event.equals(ExGGraphRegister.GRAPH_ADDED)){
			Graph g = new Graph((IGraph)parameters[0]);
			graphTree.getRoot().addGraph(g);
			graphTree.updateUI();	
		}
	}

}
