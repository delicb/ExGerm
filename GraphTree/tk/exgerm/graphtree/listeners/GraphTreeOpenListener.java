package tk.exgerm.graphtree.listeners;

import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;
import tk.exgerm.graphtree.GraphTreeScroll;
import tk.exgerm.graphtree.actions.NewGraphTreeAction;

public class GraphTreeOpenListener implements IListener {

	private NewGraphTreeAction action;
	
	public GraphTreeOpenListener(GraphTree graphTree, ICoreContext context, NewGraphTreeAction action){
		this.action = action;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals(ExGGraphicalComponent.ACTIVE_TAB_CHANGED)){
			if(parameters[0] instanceof GraphTreeScroll)
				this.action.setEnabled(false);
		}
	}

}
