package tk.exgerm.core.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.ExGAction;

public class ConfigurationAction extends ExGAction{

	private static final long serialVersionUID = 4033382702490494448L;

	public ConfigurationAction() {
		putValue(NAME, "Configuration");
		putValue(SMALL_ICON, loadIcon("images/Config.png"));
		putValue(SHORT_DESCRIPTION, "Shows configuration window.");
	}

	@Override
	public int getActionPosition() {
		return ExGAction.MENU | ExGAction.TOOLBAR;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public int getToolbarPosition() {
		return 5000;
	}

	@Override
	public String getMenu() {
		return ExGAction.PLUGINS_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 5000;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		Core.getInstance().getConfigurationManager().ask();
	}

}
