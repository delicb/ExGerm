package tk.exgerm.help;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.help.gui.HelpWindow;

@SuppressWarnings("serial")
public class HelpAction extends ExGAction{
	
	@SuppressWarnings("unused")
	private HelpService helpService;
	private ICoreContext coreContext;
	

	public HelpAction(HelpService helpService, ICoreContext coreContext) {
		putValue(NAME, "Help"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("gui/icons/Help.png"));
		putValue(SHORT_DESCRIPTION, "Open help"); //$NON-NLS-1$
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		this.helpService = helpService;
		this.coreContext = coreContext;
	}

	@Override
	public int getActionPosition() {
		return ExGAction.MENU;
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
		return ExGAction.HELP_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 1000;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		HelpWindow win = new HelpWindow(coreContext);
		win.setVisible(true);
	}

}
