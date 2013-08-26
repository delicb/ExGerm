package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.VisualiserConfig;
import tk.exgerm.visualiser.navigator.Navigator;
import tk.exgerm.visualiser.navigator.NavigatorMenu;
import tk.exgerm.visualiser.view.VisualiserView;

public class ConfigChangedListener implements IListener {

	private NavigatorMenu navigatorMenu;
	private ViewManager viewManager;
	
	public ConfigChangedListener(NavigatorMenu navigatorMenu, ViewManager viewManager){
		this.navigatorMenu = navigatorMenu;
		this.viewManager = viewManager;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		
		if (parameters.length == 0 || parameters[0].equals("Visualiser")) {
			int showState = VisualiserConfig.getInstanse().getShowNavigation();
			Navigator.setShowState(showState);
			
			if(viewManager == null)
				return;
			VisualiserView view = viewManager.getActiveView();
			if(view != null){
				Navigator navigator = view.getNavigator();
				navigator.paintImmediately();
			}
			
			navigatorMenu.setSelectedMenu(showState);
			Navigator.setHorizontalGap(VisualiserConfig.getInstanse().getHorizontalGap());
			Navigator.setZoomPanelGap(VisualiserConfig.getInstanse().getZoombarGap());
			Navigator.setFadeFactor(VisualiserConfig.getInstanse().getFadeFactor()/1000);
		}
	}
}
