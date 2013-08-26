package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class ActiveTabChangedListener implements IListener {
	
	private ViewManager viewManager;
	private ICoreContext context;
	
	public ActiveTabChangedListener(ViewManager _vm, ICoreContext _context){
		viewManager = _vm;
		context = _context;
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


