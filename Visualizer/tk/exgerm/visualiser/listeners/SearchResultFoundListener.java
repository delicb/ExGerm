package tk.exgerm.visualiser.listeners;

import java.util.ArrayList;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class SearchResultFoundListener implements IListener {
	
	private ViewManager viewManager;
	
	public SearchResultFoundListener(ViewManager _vm){
		viewManager = _vm;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void raise(String event, Object... parameters) {
		
		VisualiserView view = viewManager.getActiveView();

		view.getModel().setSearchResults( (ArrayList<Object>) parameters[1] );
		view.setDrawSearchResults(true);
		view.getModel().handleSearch();
		view.repaint();
	}

}
