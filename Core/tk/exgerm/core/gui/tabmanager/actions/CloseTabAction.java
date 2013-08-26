package tk.exgerm.core.gui.tabmanager.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import tk.exgerm.core.Core;
import tk.exgerm.core.gui.tabmanager.TabComponent;
import tk.exgerm.core.gui.tabmanager.TabPanel;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Impelmentira akciju zatvaranja tab-a, konteksnog menija {@link TabPanel
 * tabPanel-a}.
 * 
 * @author Tim 2
 * 
 */
public class CloseTabAction implements ActionListener {

	TabComponent tabComponent;
	TabPanel tabPanel;

	public CloseTabAction(TabPanel tabPanel, TabComponent tabComponent) {
		this.tabPanel = tabPanel;
		this.tabComponent = tabComponent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = tabPanel.indexOfTabComponent(tabComponent);
		JComponent component = null;
		try {
			component = (JComponent) tabPanel.getComponentAt(index);
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return;
		} catch (ClassCastException ex) {
			ex.printStackTrace();
			return;
		}

		if (index != -1) {
			try {
				tabPanel.remove(index);
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				return;
			}
			Core.getInstance().getEventDispatcher().raiseEvent(
					ExGGraphicalComponent.TAB_CLOSED, component);
		}
	}
}
