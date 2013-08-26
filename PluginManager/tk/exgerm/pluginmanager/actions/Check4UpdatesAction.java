package tk.exgerm.pluginmanager.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.pluginmanager.PluginManager;
import tk.exgerm.pluginmanager.gui.PluginManagerMainWindow;

/**
 * Akcija koja služi za proveravanje da li postoje nove verzije svih
 * pluginova.
 */
@SuppressWarnings("serial")
public class Check4UpdatesAction extends ExGAction {

	PluginManager manager;

	public Check4UpdatesAction(PluginManager manager) {
		this.manager = manager;
		putValue(NAME, "Check for updates"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/updateall.png"));
		putValue(SHORT_DESCRIPTION, "Checks for plugin updates"); //$NON-NLS-1$
	}

	@Override
	public int getActionPosition() {
		return MENU;
	}

	@Override
	public String getMenu() {
		return HELP_MENU;
	}

	@Override
	public String getToolbar() {
		return null;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		PluginManagerMainWindow w = manager.getService().getWindow();
		int[] rows = new int[w.getTableRowCount()];
		for (int i = 0; i < w.getTableRowCount(); i++)
			rows[i] = i;
		w.getTable().addRowSelectionInterval(0, w.getTableRowCount() - 1);
		w.updateSelected();
		JOptionPane.showMessageDialog(null,
				"You are using latest versions of all plugins.",
				"Plugins are up-to-date!", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public int getMenuPostition() {
		return 1000;
	}

	@Override
	public int getToolbarPosition() {
		return -1;
	}

}
