package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.view.VisualiserView;

public class DirtyFlagChangedListener implements IListener {
	private VisualiserView view;
	
	public DirtyFlagChangedListener(VisualiserView view){
		this.view = view;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		view.repaint();
	}

}
