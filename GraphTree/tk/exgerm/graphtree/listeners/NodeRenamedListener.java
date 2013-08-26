package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

public class NodeRenamedListener implements IListener {

	private GraphTree graphTree;

	public NodeRenamedListener(GraphTree graphTree, ICoreContext context) {
		this.graphTree = graphTree;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
			graphTree.updateUI();
	}

}
