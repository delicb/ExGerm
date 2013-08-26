package tk.exgerm.core.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.Core;
import tk.exgerm.core.event.EventDispatcher;
import tk.exgerm.core.gui.StatusBar;
import tk.exgerm.core.plugin.ExGAction;

@SuppressWarnings("serial")
public class ShowHideStatusbarAction extends ExGAction {

	public ShowHideStatusbarAction() {
		putValue(NAME, "Show statusbar");
		putValue(SHORT_DESCRIPTION, "Shows and hides statusbar");
	}
	
	@Override
	public int getActionPosition() {
		return ExGAction.MENU;
	}

	@Override
	public String getMenu() {
		return ExGAction.VIEW_MENU;
	}

	@Override
	public String getToolbar() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Core c = Core.getInstance();
		StatusBar statusbar = c.getMainWindow().getStatusBar();
		EventDispatcher eventDispatcher = c.getEventDispatcher();
		if (statusbar.isVisible()) {
			Core.getInstance().getMainWindow().getStatusBar().setVisible(false);
			eventDispatcher.raiseEvent("mainwindow.statusbar_hidden");
		}
		else { 
			statusbar.setVisible(true);
			eventDispatcher.raiseEvent("mainwindow.statusbar_shown");
		}
	}

	@Override
	public int getMenuPostition() {
		return 300;
	}

	@Override
	public int getToolbarPosition() {
		return -1;
	}

}
