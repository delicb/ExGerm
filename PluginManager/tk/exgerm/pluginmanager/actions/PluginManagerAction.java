package tk.exgerm.pluginmanager.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Otvara glavni prozor za rad sa pluginovima.
 * 
 */
@SuppressWarnings("serial")
public class PluginManagerAction extends ExGAction {

	PluginManager manager;

	public PluginManagerAction(PluginManager manager) {
		this.manager = manager;
		putValue(NAME, "Plugin Manager"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/plugins.png"));
		putValue(SHORT_DESCRIPTION, "Show Plugin Manager"); //$NON-NLS-1$
	}

	@Override
	public int getActionPosition() {
		return MENU | TOOLBAR;
	}

	@Override
	public String getMenu() {
		return ExGAction.HELP_MENU;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		manager.getService().getWindow().setVisible(true);
	}

	@Override
	public int getMenuPostition() {
		return 1000;
	}

	@Override
	public int getToolbarPosition() {
		return 1200;
	}

}
