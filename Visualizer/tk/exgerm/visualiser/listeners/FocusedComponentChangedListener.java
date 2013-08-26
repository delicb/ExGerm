package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class FocusedComponentChangedListener implements IListener {
	
	private ViewManager viewManager;
	
	public FocusedComponentChangedListener(ViewManager _vm){
		viewManager = _vm;
	}

	@Override
	public void raise(String event, Object... parameters) {
		try{
			VisualiserView view = (VisualiserView) parameters[0];
			if(view.getFinalRoot() == null) viewManager.setActiveView(view.getName(), 0, view.getName());
			else viewManager.setActiveView(view.getFinalRoot(), view.getLevel(), view.getName());
		}catch(Exception e){
			return;
		}
	}
}
