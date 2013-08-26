package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.view.VisualiserView;

public class TabClosedListener implements IListener {

	public TabClosedListener(){
		
	}

	@Override
	public void raise(String event, Object... parameters) {
		try{
			VisualiserView view = (VisualiserView) parameters[0];
			Activator.visService.removeVisualiser(view);
			view.setVisual(false);
			view.visualization.stop();
			view.animation.stop();
		}catch(Exception e){
			return;
		}
	}

}
