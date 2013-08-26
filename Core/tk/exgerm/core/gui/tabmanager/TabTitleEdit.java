package tk.exgerm.core.gui.tabmanager;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Implementira polje za promenu naziva tab-a.
 * 
 * @author Tim 2
 *
 */
public class TabTitleEdit extends JTextField implements KeyListener,
		FocusListener {

	private static final long serialVersionUID = -4900482772351958897L;

	private final TabPanel tabPanel;
	private TabComponent tabComponent;

	public TabTitleEdit(TabPanel tabPanel, TabComponent tabComponent) {
		this.tabPanel = tabPanel;
		this.tabComponent = tabComponent;

		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		addKeyListener(this);
		addFocusListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_ENTER) {
			if (!getText().isEmpty()
					&& !getText().equals(tabComponent.getTabTitle())) {
				try {
					tabPanel.setTitleAt(tabPanel
							.indexOfTabComponent(tabComponent), getText());
				} catch (IndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}
				tabComponent.setEditMode(false);

				Core.getInstance().getEventDispatcher().raiseEvent(
						ExGGraphicalComponent.TAB_TITLE_CHANGED,
						tabPanel.getComponentAt(tabPanel
								.indexOfTabComponent(tabComponent)), getText());
			}
		} else if (key == KeyEvent.VK_ESCAPE) {
			tabComponent.setEditMode(false);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		tabComponent.setEditMode(false);
	}
}
