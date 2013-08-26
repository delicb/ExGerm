package tk.exgerm.visualiser.navigator;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.VisualiserConfig;
import tk.exgerm.visualiser.view.VisualiserView;

public class ShowAutomaticAction extends AbstractAction {

	private static final long serialVersionUID = 4707123454147663273L;

	private ViewManager viewManager;

	public ShowAutomaticAction(ViewManager viewManager) {
		putValue(NAME, "Automatic"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Automatic show navigator"); //$NON-NLS-1$

		this.viewManager = viewManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VisualiserConfig.getInstanse().putValue("show_navigation",
				String.valueOf(Navigator.SHOW_AUTOMATICALLY));
		VisualiserConfig.getInstanse().loadConfiguration();
		if (viewManager == null)
			return;
		VisualiserView view = viewManager.getActiveView();
		if (view != null) {
			Navigator navigator = view.getNavigator();
			navigator.paintImmediately();
		}
	}
}
