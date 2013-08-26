package tk.exgerm.core.gui.tabmanager;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import tk.exgerm.core.gui.tabmanager.actions.CloseAllTabsAction;
import tk.exgerm.core.gui.tabmanager.actions.CloseOthersAction;
import tk.exgerm.core.gui.tabmanager.actions.CloseTabAction;
import tk.exgerm.core.gui.tabmanager.actions.RenameTabAction;
import tk.exgerm.core.gui.tabmanager.listeners.TabCloseButtonListener;
import tk.exgerm.core.gui.tabmanager.listeners.TabComponentMouseListener;

/**
 * Predstavlja tab komponentu. Sadrzi {@link JLabel} komponentu za prikaz naziva
 * tab-a i {@link JButton} komponentu za zatvaranje tab-a. Omogućava pomnu
 * naziva tab-a.
 * 
 * @author Tim 2
 */

public class TabComponent extends JPanel {

	private static final long serialVersionUID = 3270472181359783068L;

	private final TabPanel tabPanel;
	private JLabel tabTitle;
	private TabTitleEdit tabTitleEdit;
	private JButton closeButton;
	private JPopupMenu popupMenu;
	private boolean isTabNameChangeable;

	/**
	 * Konstruktor, inicijalizuje naziv tab-a, dugme za zatvaranje i popup meni
	 * {@link TabComponent tab-komponente. Dodaje odgovarajuće osluskivače}
	 * 
	 * @param tabPanel
	 */
	public TabComponent(final TabPanel tabPanel, boolean isTabNameChangeable) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabPanel = tabPanel;
		this.isTabNameChangeable = isTabNameChangeable;

		setOpaque(false); // postavi transparenciju

		// omoguci citanje naslova tab-a
		tabTitle = new JLabel() {
			private static final long serialVersionUID = -4326638274034383542L;

			public String getText() {
				int i = tabPanel.indexOfTabComponent(TabComponent.this);
				if (i != -1) {
					return tabPanel.getTitleAt(i);
				}
				return null;
			}
		};
		tabTitleEdit = new TabTitleEdit(tabPanel, TabComponent.this);
		tabTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		add(tabTitle);

		closeButton = new CloseButton();
		closeButton.addMouseListener(new TabCloseButtonListener(this.tabPanel));
		closeButton.addActionListener(new CloseTabAction(this.tabPanel,
				TabComponent.this));
		add(closeButton);

		// popup meni
		JMenuItem menuItem;
		popupMenu = new JPopupMenu();
		menuItem = new JMenuItem("Close Tab");
		menuItem.addActionListener(new CloseTabAction(tabPanel,
				TabComponent.this));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem("Close Others");
		menuItem.addActionListener(new CloseOthersAction(tabPanel));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem("Close All");
		menuItem.addActionListener(new CloseAllTabsAction(tabPanel));
		popupMenu.add(menuItem);
		popupMenu.addSeparator();
		menuItem = new JMenuItem("Rename");
		menuItem.addActionListener(new RenameTabAction(TabComponent.this));
		menuItem.setEnabled(isTabNameChangeable);
		popupMenu.add(menuItem);
		setComponentPopupMenu(popupMenu);

		TabComponentMouseListener tabComponentMouseListener = new TabComponentMouseListener(
				tabPanel);
		addMouseListener(tabComponentMouseListener);
		addMouseMotionListener(tabComponentMouseListener);
		addMouseWheelListener(tabComponentMouseListener);
	}

	/**
	 * Vraća dugme koje zatvara tab
	 * 
	 * @return dugme koje zatvara tab
	 */
	public JButton getCloseButton() {
		return closeButton;
	}

	/**
	 * Vraća komponentu koja prikazuje naziv tab-a
	 * 
	 * @return {@link JLabel} - komponenta koja prikazuje naziv tab-a
	 */
	public JLabel getTabtitleComponent() {
		return tabTitle;
	}

	/**
	 * Osvežava prikaz naziva taba
	 */
	public void updateTabTitle() {
		tabTitle.revalidate();
	}

	/**
	 * Vraća naziv tab-a
	 * 
	 * @return {@link String} - naziv taba
	 */
	public String getTabTitle() {
		return tabTitle.getText();
	}

	/**
	 * Vraća {@link Boolean} vrednost da li se naziv tab-a može menjati.
	 * 
	 * @return {@link Boolean} - <code>true</code> može se menjati,
	 *         <code>false</code> ne.
	 */
	public boolean isTabNameChangeable() {
		return isTabNameChangeable;
	}

	/**
	 * Omogućava prelazak u stanje za promenu imena tab-u. Ako je prosleđeni
	 * parametar <code>true</code> sistem se postavlja u stanje za promenu
	 * naziva tab-a, <code>false</code> povratak u stanje prikaza.
	 * 
	 * @param editTabTitle
	 *            {@link Boolean} - <code>true</code> stanje promene naziva
	 *            tab-a, <code>false</code> povratak u stanje prikaza.
	 */
	public void setEditMode(boolean editTabTitle) {
		if (editTabTitle) {
			setIgnoreRepaint(true);
			tabTitleEdit.setText(tabTitle.getText());
			remove(tabTitle);
			add(tabTitleEdit, 0);
			tabTitleEdit.selectAll();
			tabTitleEdit.requestFocusInWindow();
			setIgnoreRepaint(false);
		} else {
			setIgnoreRepaint(true);
			remove(tabTitleEdit);
			add(tabTitle, 0);
			setIgnoreRepaint(false);
		}
	}
}
