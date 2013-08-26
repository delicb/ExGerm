package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class EdgeRemovedListener implements IListener {
	
	private ViewManager viewManager;
	private VisualiserView view;

	public EdgeRemovedListener(ViewManager _vm){
		viewManager = _vm;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		IEdge e = (IEdge) parameters[1];
		
		view = viewManager.getSubView(e.getFrom().getFinalRoot().getName(), e.getFrom().getLevel()-1, e.getFrom().getGraph().getName());
		view.getModel().removeEdge(e.getID());
	}
	
}
