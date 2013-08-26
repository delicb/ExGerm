package tk.exgerm.core.gui.tabmanager.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import tk.exgerm.core.Core;
import tk.exgerm.core.gui.tabmanager.TabPanel;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Impelmentira akciju zatvaranja svih prikazanih tab-ova, konteksnog menija
 * {@link TabPanel tabPanel-a}.
 * 
 * @author Tim 2
 * 
 */
public class CloseAllTabsAction implements ActionListener {

	TabPanel tabPanel;

	public CloseAllTabsAction(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		for (int i = tabPanel.getTabCount() - 1; i > -1; i--) {
			JComponent component = null;
			try {
				component = (JComponent) tabPanel.getComponentAt(i);
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				return;
			} catch (ClassCastException ex) {
				ex.printStackTrace();
				return;
			}
			try {
				tabPanel.remove(i);
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				return;
			}
			Core.getInstance().getEventDispatcher().raiseEvent(
					ExGGraphicalComponent.TAB_CLOSED, component);
		}
	}
}
