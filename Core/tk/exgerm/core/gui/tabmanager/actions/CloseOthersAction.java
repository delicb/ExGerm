package tk.exgerm.core.gui.tabmanager.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tk.exgerm.core.Core;
import tk.exgerm.core.gui.tabmanager.TabPanel;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Impelmentira akciju zatvaranja svih tab-ova osim trenutno priskazanog,
 * konteksnog menija {@link TabPanel tabPanel-a}.
 * 
 * @author Tim 2
 * 
 */
public class CloseOthersAction implements ActionListener {

	private TabPanel tabPanel;

	public CloseOthersAction(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Component selectedComponent = tabPanel.getSelectedComponent();
		int removeIndex = 0;
		try {
			while (tabPanel.getTabCount() > 1) {
				if (!tabPanel.getComponentAt(removeIndex).equals(
						selectedComponent)) {
					Component component = tabPanel.getComponentAt(removeIndex);
					tabPanel.remove(removeIndex);
					Core.getInstance().getEventDispatcher().raiseEvent(
							ExGGraphicalComponent.TAB_CLOSED, component);
				} else if (removeIndex != 1)
					removeIndex++;
			}
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return;
		}
	}
}
