package tk.exgerm.core.gui.tabmanager.listeners;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;

import tk.exgerm.core.gui.tabmanager.TabPanel;

/**
 * Klasa koja reaguje na klik i prelazak miša preko dugmeta na tabu za
 * zatvaranje.
 * 
 * @author Tim 2
 */
public class TabCloseButtonListener extends MouseAdapter {

	TabPanel tabPanel;

	/**
	 * Konstruktor, samo inicijalizuje
	 * 
	 * @param tabPanel
	 *            tabPanel na kome se nalazi dugme
	 */
	public TabCloseButtonListener(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Component component = e.getComponent();
		if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			// iscrtaj ivicu
			button.setBorderPainted(true);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Component component = e.getComponent();
		if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			// ukloni ivicu
			button.setBorderPainted(false);
		}
	}

}
