package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

public class GraphRemovedListener implements IListener{

	private GraphTree graphTree;
	
	public GraphRemovedListener(GraphTree graphTree, ICoreContext context){
		this.graphTree = graphTree;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if (event.equals(ExGGraphRegister.GRAPH_REMOVED)){
			graphTree.getRoot().removeGraph((IGraph)parameters[0]);
			graphTree.updateUI();
		}
	}

}
