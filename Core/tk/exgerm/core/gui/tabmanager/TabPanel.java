package tk.exgerm.core.gui.tabmanager;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tk.exgerm.core.Core;
import tk.exgerm.core.gui.listeners.NameChangedListener;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Predstavlja panel koji sadrži {@link ExGGraphicalComponent komponente}
 * 
 * @author Tim 2
 * 
 */
public class TabPanel extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = -5572112340559075311L;

	private static TransferHandler th = new TabPanelTransferHandler();

	private Image img;
	private Point oldTabIndicatorPosition = new Point(-1, -1);
	private boolean tabIndicatorShowed = false;
	private Component selected = null;
	private boolean sendTabChangedEvent = true;

	/**
	 * Konstruktor, inicijalizuje {@link TabPanelTransferHandler
	 * TransferHandler} i učitava sliku tabIndikatora.
	 */
	public TabPanel() {
		setTransferHandler(th);
		addChangeListener(this);
		MediaTracker tracker = new MediaTracker(this);
		img = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("images/tabDragIndicator.png"));
		tracker.addImage(img, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dodaje {@link Component komponentu} na poziciju određenu indeksom
	 * 
	 * @param component
	 *            komponenta koja se dodaje na {@link TabPanel tab panel}
	 * @param index
	 *            pozicija na koju se dodaje komponenta
	 */
	synchronized public void insertTab(Component component, int index,
			boolean isTabNameChangeable) {

		int indexOfComponent = indexOfComponent(component);
		if (indexOfComponent == -1) { // ako komponenta ne postoji
			add(component, index);
			TabComponent tabComponent = new TabComponent(this,
					isTabNameChangeable);

			component.addPropertyChangeListener(new NameChangedListener());
			try {
				setTabComponentAt(indexOfComponent(component), tabComponent);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

			try {
				setSelectedComponent(component);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} else { // ako postoji, postavi njen tab kao aktivan
			try {
				setSelectedIndex(indexOfComponent);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Implementira funkcionalnost {@link ChangeListener ChangeListener-a}.
	 * Šalje događaj tabmanager.active_tab_changed kada se promeni aktivan tab.
	 */
	@Override
	public synchronized void stateChanged(ChangeEvent e) {
		JTabbedPane tabPanel = null;
		try {
			tabPanel = (TabPanel) e.getSource();
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
		Component c = tabPanel.getSelectedComponent();

		if (c != null) {
			if (this.sendTabChangedEvent && !c.equals(this.selected)) {

				Core.getInstance().getEventDispatcher().raiseEvent(
						ExGGraphicalComponent.ACTIVE_TAB_CHANGED, c);

				this.selected = c;
			}
		}
	}

	/**
	 * Dozvoljava slanje tabmanager.active_tab_changed događaja
	 */
	public void setTabChangedEventEnabled() {
		this.sendTabChangedEvent = true;
	}

	/**
	 * Onemogućava slanje tabmanager.active_tab_changed događaja
	 */
	public void setTabChangedEventDisabled() {
		this.sendTabChangedEvent = false;
	}

	/**
	 * Iscrtava indikator na prosleđenu poziciju
	 * 
	 * @param newTabIndicatorPosition
	 *            lokacija na koju se indikator iscrtava
	 */
	public void showTabDragIndicator(Point newTabIndicatorPosition) {
		if (newTabIndicatorPosition == null)
			return;

		if (!tabIndicatorShowed
				|| !this.oldTabIndicatorPosition
						.equals(newTabIndicatorPosition)) {
			Graphics g = getGraphics();

			clearTabDragIndicator();
			g.drawImage(img, newTabIndicatorPosition.x, 0, this);
			this.tabIndicatorShowed = true;
			this.oldTabIndicatorPosition.x = newTabIndicatorPosition.x;
			this.oldTabIndicatorPosition.y = newTabIndicatorPosition.y;
		}
	}

	/**
	 * Uklanja idikator sa kanvasa
	 */
	private void clearTabDragIndicator() {
		if (tabIndicatorShowed) {
			paintImmediately(this.oldTabIndicatorPosition.x,
					this.oldTabIndicatorPosition.y, 11, 11);
		}
	}

	/**
	 * Skriva indikator
	 */
	public void hideTabDragIndicator() {
		if (tabIndicatorShowed) {
			clearTabDragIndicator();
			tabIndicatorShowed = false;
		}
	}
}
