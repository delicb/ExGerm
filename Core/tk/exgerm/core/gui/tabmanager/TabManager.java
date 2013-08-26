package tk.exgerm.core.gui.tabmanager;

import static tk.exgerm.core.plugin.ExGGraphicalComponent.CENTER;
import static tk.exgerm.core.plugin.ExGGraphicalComponent.EAST;
import static tk.exgerm.core.plugin.ExGGraphicalComponent.SOUTH;
import static tk.exgerm.core.plugin.ExGGraphicalComponent.WEST;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import tk.exgerm.core.gui.listeners.ExGGraphicalComponentFocusListener;
import tk.exgerm.core.plugin.ExGGraphicalComponent;

/**
 * Klasa implementira funkcionalnost radnog prostora. Sadrži četiri nezavisna
 * {@link TabPanel panela} i koji zavisno od njihove pozicije mogu biti: CENTER,
 * EAST, SOUTH, WEST. Panelima je omogućeno dodavanje i uklanjanje
 * {@link ExGGraphicalComponent komponenti} koje se prikazuju u obliku tab-ova.
 * 
 * @see TabPanel
 * @see ExGGraphicalComponent
 * @author Tim 2
 */
public class TabManager extends JPanel {

	private static final long serialVersionUID = 3506502630855051711L;

	/**
	 * {@link Map Mapa} koja sadrži panele
	 */
	private Map<Integer, TabPanel> tabs = new HashMap<Integer, TabPanel>();

	/**
	 * Separatori koji odvajaju {@link TabPanel tabPanele i omogućavaju promenu
	 * veličine njihovog prikaza}
	 */
	private JSplitPane leftSeparator;
	private JSplitPane bottomSeparator;
	private JSplitPane rightSeparator;

	/**
	 * Konstruktor
	 */
	public TabManager() {

		setLayout(new BorderLayout());

		TabPanel t;

		t = new TabPanel();
		t.setTabLayoutPolicy(TabPanel.SCROLL_TAB_LAYOUT);
		tabs.put(CENTER, t);

		t = new TabPanel();
		t.setTabLayoutPolicy(TabPanel.SCROLL_TAB_LAYOUT);
		tabs.put(SOUTH, t);

		t = new TabPanel();
		t.setTabLayoutPolicy(TabPanel.SCROLL_TAB_LAYOUT);
		tabs.put(EAST, t);

		t = new TabPanel();
		t.setTabLayoutPolicy(TabPanel.SCROLL_TAB_LAYOUT);
		tabs.put(WEST, t);

		leftSeparator = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		bottomSeparator = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		rightSeparator = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

		leftSeparator.setRightComponent(bottomSeparator);
		bottomSeparator.setTopComponent(rightSeparator);

		leftSeparator.setResizeWeight(0.10);
		bottomSeparator.setResizeWeight(0.85);
		rightSeparator.setResizeWeight(0.85);

		leftSeparator.setDividerSize(8);
		bottomSeparator.setDividerSize(8);
		rightSeparator.setDividerSize(8);

		rightSeparator.setLeftComponent(tabs.get(CENTER));
		rightSeparator.setRightComponent(tabs.get(EAST));
		leftSeparator.setLeftComponent(tabs.get(WEST));
		bottomSeparator.setBottomComponent(tabs.get(SOUTH));

		add(leftSeparator, BorderLayout.CENTER);
	}

	/**
	 * Dodaje {@link ExGGraphicalComponent komponentu}, na poziciju
	 * {@link TabManager TabManager-a} odredjenu parametrom
	 * <code>position</code>. Parametar <code>position</code> uzima sledeće
	 * vrendnosti:
	 * <ul>
	 * <li>CENTER - centralna pozicija</li>
	 * <li>SOUTH - donja pozicija</li>
	 * <li>EAST - desna pozicija</li>
	 * <li>WEST - leva pozicija</li>
	 * </ul>
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koja se dodaje
	 * @param position
	 *            na koju poziciju se {@link ExGGraphicalComponent komponenta}
	 *            dodaje
	 */
	synchronized public void addTab(ExGGraphicalComponent component,
			int position) {
		TabPanel t = tabs.get(position);
		t.insertTab(component.getComponent(), t.getTabCount(), component
				.isTabNameChangeable());
		component.getComponent()
				.addFocusListener(
						new ExGGraphicalComponentFocusListener(component
								.getComponent()));
	}

	/**
	 * Dodaje {@link ExGGraphicalComponent komponentu} na centralnu poziciju
	 * {@link TabManager TabManager-a}.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta koja se dodaje
	 */
	public void addTabToCenter(ExGGraphicalComponent component) {
		addTab(component, CENTER);
	}

	/**
	 * Dodaje {@link ExGGraphicalComponent komponentu} na donju poziciju
	 * {@link TabManager TabManager-a}.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koja se dodaje
	 */
	public void addTabToSouth(ExGGraphicalComponent component) {
		addTab(component, SOUTH);
	}

	/**
	 * Dodaje {@link ExGGraphicalComponent komponentu} na desnu poziciju
	 * {@link TabManager TabManager-a}.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koja se dodaje
	 */
	public void addTabToEast(ExGGraphicalComponent component) {
		addTab(component, EAST);
	}

	/**
	 * Dodaje {@link ExGGraphicalComponent komponentu} na levu poziciju
	 * {@link TabManager TabManager-a}.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koja se dodaje
	 */
	public void addTabToWest(ExGGraphicalComponent component) {
		addTab(component, WEST);
	}

	/**
	 * Uklanja {@link ExGGraphicalComponent komponentu} iz {@link TabManager
	 * TabManager-a}.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koju treba ukloniti
	 */
	public void removeTab(ExGGraphicalComponent component) {
		Iterator<TabPanel> it = tabs.values().iterator();
		while (it.hasNext()) {
			it.next().remove(component.getComponent());
		}
	}

	/**
	 * Postavlja tab u kome se nalazi prosleđena {@link ExGGraphicalComponent
	 * komponenta} kao aktivan.
	 * 
	 * @param component
	 *            {@link ExGGraphicalComponent komponenta} koja se nalazi u
	 *            tab-u
	 * @return <code>true</code> ako {@link ExGGraphicalComponent komponenta}
	 *         postoji u okviru {@link TabManager tabManager-a}, ako ne
	 *         <code>false</code>
	 */
	public boolean setActiveComponent(ExGGraphicalComponent component) {
		Iterator<TabPanel> it = tabs.values().iterator();
		while (it.hasNext()) {

			TabPanel tabPanel = it.next();
			int index = tabPanel.indexOfComponent(component.getComponent());

			if (index != -1) {
				try {
					tabPanel.setSelectedIndex(index);
					return true;
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
}
