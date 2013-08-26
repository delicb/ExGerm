package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

public class GraphRenamedListener implements IListener {

	private GraphTree graphTree;

	public GraphRenamedListener(GraphTree graphTree, ICoreContext context) {
		this.graphTree = graphTree;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals(IGraph.GRAPH_NAME_CHANGED))
			graphTree.updateUI();
	}

}
