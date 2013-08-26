package tk.exgerm.core.gui.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import tk.exgerm.core.gui.tabmanager.TabComponent;
import tk.exgerm.core.gui.tabmanager.TabPanel;

public class NameChangedListener implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("name")) {
			
			JComponent component = null;
			try {
				component = (JComponent) evt.getSource();
			} catch (ClassCastException e) {
				e.printStackTrace();
				return;
			}
			TabPanel tabPanel = null;
			try {
				tabPanel = (TabPanel) component.getParent();
			} catch (ClassCastException e) {
				e.printStackTrace();
				return;
			}
			
			TabComponent tabComponent = null;
			try {
				tabComponent = (TabComponent) tabPanel
						.getTabComponentAt(tabPanel.indexOfComponent(component));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return;
			} catch (ClassCastException e) {
				e.printStackTrace();
				return;
			}

			try {
				tabPanel.setTitleAt(tabPanel.indexOfComponent(component), component
					.getName());
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return;
			}
			
			tabComponent.updateTabTitle();
		}

	}

}
