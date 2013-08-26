package tk.exgerm.visualiser.navigator;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.VisualiserConfig;
import tk.exgerm.visualiser.view.VisualiserView;

public class ShowNeverAction extends AbstractAction{

	private static final long serialVersionUID = 7653395365882319150L;

	private ViewManager viewManager;

	public ShowNeverAction(ViewManager viewManager) {
		putValue(NAME, "Never"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Never show navigator"); //$NON-NLS-1$
		
		this.viewManager = viewManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VisualiserConfig.getInstanse().putValue("show_navigation",
				String.valueOf(Navigator.SHOW_NEVER));
		VisualiserConfig.getInstanse().loadConfiguration();
		
		if(viewManager == null)
			return;
		VisualiserView view = viewManager.getActiveView();
		if(view != null){
			Navigator navigator = view.getNavigator();
			navigator.paintImmediately();
		}
	}
}
