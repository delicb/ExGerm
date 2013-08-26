package tk.exgerm.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.ExGAction;

@SuppressWarnings("serial")
public class ExitAction extends ExGAction {

	public ExitAction() {
		putValue(NAME, "Exit");
		putValue(SHORT_DESCRIPTION, "Close application");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
	}
	
	@Override
	public int getActionPosition() {
		return MENU;
	}

	@Override
	public String getToolbar() {
		return null;
	}

	@Override
	public int getToolbarPosition() {
		return 0;
	}

	@Override
	public String getMenu() {
		return FILE_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 1000000;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Core.getInstance().close();

	}

}
