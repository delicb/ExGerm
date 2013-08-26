package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

public class EdgeDirectedListener implements IListener {

	private GraphTree graphTree;
	
	public EdgeDirectedListener(GraphTree graphTree, ICoreContext context){
		this.graphTree = graphTree;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals(IEdge.EDGE_DIRECTED_CHANGED)){
			this.graphTree.updateUI();
		}
	}

}
