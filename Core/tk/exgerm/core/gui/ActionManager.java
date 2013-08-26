package tk.exgerm.core.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGMenuItem;
import tk.exgerm.core.plugin.ExGSubmenu;

/**
 * Klasa koja manipuliše akcijama u menijima i toolbar-u.
 * 
 * @author Tim 2
 * 
 */
public class ActionManager {

	/**
	 * Konstante za određivanje po čemu sorted metoda sortira
	 */
	public static int MENU = 1;
	public static int TOOLBAR = 2;

	/**
	 * Menibar u koji se dodaju svi meniji
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * Panel sa svim toolbar-ovima
	 */
	private JPanel toolbarPanel;

	/**
	 * Mapa svih menija koji postoje u meni bar-u
	 */
	private Map<String, JMenu> menies = new HashMap<String, JMenu>();

	/**
	 * Mapa svih toolbar-va koji se pojavljuju na glavnom prozoru
	 */
	private Map<String, JToolBar> toolbars = new HashMap<String, JToolBar>();

	private Map<ExGMenuItem, AbstractButton> menuItems = new HashMap<ExGMenuItem, AbstractButton>();

	private Map<ExGAction, AbstractButton> toolbarItems = new HashMap<ExGAction, AbstractButton>();

	private Map<Integer, List<ExGMenuItem>> menuItemPositions = new HashMap<Integer, List<ExGMenuItem>>();

	private Map<Integer, List<ExGAction>> toolbarItemsPositions = new HashMap<Integer, List<ExGAction>>();

	private MainWindow mainWindow;

	public ActionManager(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.mainWindow.setJMenuBar(menuBar);

		this.toolbarPanel = new JPanel();
		this.toolbarPanel.setLayout(new BoxLayout(toolbarPanel,
				BoxLayout.Y_AXIS));

		this.mainWindow.getContentPane().add(this.toolbarPanel,
				BorderLayout.NORTH);
	}

	/**
	 * Registruje novi meni.
	 * 
	 * @param menuCode
	 *            Kod novog menija
	 * @param menuName
	 *            Ime novog menija.
	 */
	public void registerMenu(String menuCode, String menuName) {
		JMenu menu = new JMenu(menuName);
		menu.setVisible(false);
		menies.put(menuCode, menu);
		menuBar.add(menu);
	}

	/**
	 * Registruje novi toolbar.
	 * 
	 * @param toolbarCode
	 *            Kod novog toolbar-a.
	 */
	public void registerToolbar(String toolbarCode) {
		JToolBar toolbar = new JToolBar();
		toolbar.setVisible(false);
		toolbars.put(toolbarCode, toolbar);
		this.toolbarPanel.add(toolbar);
	}

	/**
	 * Dodaje podmeni u glavni meni
	 * 
	 * @param menu
	 *            naziv glavnog menija u koji podmeni treba da se doda.
	 * @param submenu
	 *            Podmeni koji se dodaje
	 */
	public void addSubmenu(ExGSubmenu submenu) {
		menuItems.put(submenu, submenu.getMenuContent());

		List<ExGMenuItem> l = menuItemPositions.get(submenu.getMenuPostition());
		if (l == null) {
			l = new ArrayList<ExGMenuItem>();
			menuItemPositions.put(submenu.getMenuPostition(), l);
		}
		l.add(submenu);

		refresh();
	}

	/**
	 * Uklanja podmeni iz bilo kog menija u kome se ovaj podmeni pojavljivao.
	 * 
	 * @param submenu
	 *            Podmeni koji se uklanja
	 */
	public void removeSubmenu(ExGSubmenu submenu) {
		menuItems.remove(submenu);
		menuItemPositions.get(submenu.getMenuPostition()).remove(submenu);
		refresh();
	}

	/**
	 * Dodaje akciju. Gde će akciju biti dodata zavisi od same akcije.
	 * 
	 * @param action
	 *            Akcija koja se dodaje.
	 */
	public void addAction(ExGAction action) {
		if ((action.getActionPosition() & ExGAction.MENU) != 0)
			addActionToMenu(action);
		if ((action.getActionPosition() & ExGAction.TOOLBAR) != 0)
			addActionToToolbar(action);

		refresh();
	}

	/**
	 * Uklanja akciju sa svih lokacije gde može da se pojavi (meniji i
	 * toolbar-ovi).
	 * 
	 * @param action
	 *            Akcija koja se uklanja.
	 */
	public void removeAction(ExGAction action) {
		menuItems.remove(action);
		toolbarItems.remove(action);
		refresh();
	}

	/**
	 * Dodaje akciju u meni.
	 * 
	 * @param action
	 *            Akcija koja se dodaje.
	 */
	private void addActionToMenu(ExGAction action) {
		JMenuItem item = new JMenuItem(action);
		menuItems.put(action, item);
		List<ExGMenuItem> l = menuItemPositions.get(action
				.getMenuPostition());
		if (l == null) {
			l = new ArrayList<ExGMenuItem>();
			menuItemPositions.put(action.getMenuPostition(), l);
		}
		l.add(action);
	}

	/**
	 * Dodaje akciju u toolbar.
	 * 
	 * @param action
	 *            Akcija koja se dodaje.
	 */
	private void addActionToToolbar(ExGAction action) {
		JButton button = new JButton(action);
		if (action.getValue(Action.SMALL_ICON) != null
				|| action.getValue(Action.LARGE_ICON_KEY) != null)
			button.setHideActionText(true);
		toolbarItems.put(action, button);

		List<ExGAction> l = toolbarItemsPositions.get(action
				.getToolbarPosition());
		if (l == null) {
			l = new ArrayList<ExGAction>();
			toolbarItemsPositions.put(action.getToolbarPosition(), l);
		}
		l.add(action);
	}

	private void refresh() {
		refreshMenuBar();
		refreshToolbars();
		mainWindow.validate();
	}

	private void refreshMenuBar() {
		Iterator<String> it = menies.keySet().iterator();
		while (it.hasNext()) {
			String menuCode = it.next();
			JMenu menu = menies.get(menuCode);

			menu.removeAll();
			List<JComponent> components = groupMenu(getItemsForMenu(menuCode));
			for (JComponent c : components) {
				if (c == null)
					menu.addSeparator();
				else
					menu.add(c);
			}

			if (menu.getItemCount() == 0) {
				menu.setVisible(false);
			} else {
				menu.setVisible(true);
			}
		}
	}

	private void refreshToolbars() {
		Iterator<String> it = toolbars.keySet().iterator();
		while (it.hasNext()) {
			String toolbarCode = it.next();
			JToolBar toolbar = toolbars.get(toolbarCode);

			toolbar.removeAll();
			List<JComponent> components = groupToolbar(getItemsForToolbar(toolbarCode));
			for (JComponent c : components) {
				if (c == null)
					toolbar.addSeparator();
				else
					toolbar.add(c);
			}

			if (toolbar.getComponentCount() == 0) {
				toolbar.setVisible(false);
			} else {
				toolbar.setVisible(true);
			}
		}
	}

	private List<ExGAction> getItemsForToolbar(String toolbar) {
		List<ExGAction> list = new ArrayList<ExGAction>();
		for (ExGAction action : toolbarItems.keySet()) {
			if (action.getToolbar().equals(toolbar)) {
				list.add(action);
			}
		}
		return sortToolbarItems(list);
	}

	private List<ExGMenuItem> getItemsForMenu(String menu) {
		List<ExGMenuItem> list = new ArrayList<ExGMenuItem>();
		for (ExGMenuItem item : menuItems.keySet()) {
			if (item.getMenu().equals(menu))
				list.add(item);
		}
		return sortMenuItems(list);
	}

	private List<ExGMenuItem> sortMenuItems(List<ExGMenuItem> list) {
		ExGMenuItem tmp;
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				int i_pos = list.get(i).getMenuPostition();
				int j_pos = list.get(j).getMenuPostition();
				int i_group_pos = getIndexOfMenuAction(list.get(i));
				int j_group_pos = getIndexOfMenuAction(list.get(j));
				if (i_pos < j_pos || (i_pos == j_pos && i_group_pos < j_group_pos)) {
					tmp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, tmp);
				}
			}
		}
		return list;
	}

	private List<ExGAction> sortToolbarItems(List<ExGAction> list) {
		ExGAction tmp;
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				int i_pos = list.get(i).getToolbarPosition();
				int j_pos = list.get(j).getToolbarPosition();
				int i_group_pos = getIndexOfToolbarAction(list.get(i));
				int j_group_pos = getIndexOfToolbarAction(list.get(j));
				if (i_pos < j_pos || (i_pos == j_pos && i_group_pos < j_group_pos)) {
					tmp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, tmp);
				}
			}
		}
		return list;
	}

	private int getIndexOfToolbarAction(ExGAction action) {
		if (toolbarItemsPositions.get(action.getToolbarPosition()) != null)
			return toolbarItemsPositions.get(action.getToolbarPosition())
					.indexOf(action);
		else
			return -1;
	}

	private int getIndexOfMenuAction(ExGMenuItem action) {
		if (menuItemPositions.get(action.getMenuPostition()) != null)
			return menuItemPositions.get(action.getMenuPostition()).indexOf(
					action);
		else
			return -1;
	}

	private List<JComponent> groupToolbar(List<ExGAction> list) {
		List<JComponent> res = new ArrayList<JComponent>();
		int pos = -1;
		for (int i = 0; i < list.size(); i++) {
			ExGAction a = list.get(i);
			if (pos != a.getToolbarPosition())
				res.add(null);
			pos = a.getToolbarPosition();

			res.add(toolbarItems.get(a));
		}

		return removeExtraSeparators(res);
	}

	private List<JComponent> groupMenu(List<ExGMenuItem> list) {
		List<JComponent> res = new ArrayList<JComponent>();
		int pos = -1;
		for (int i = 0; i < list.size(); i++) {
			ExGMenuItem a = list.get(i);
			if (pos != a.getMenuPostition())
				res.add(null);
			pos = a.getMenuPostition();

			res.add(menuItems.get(a));
		}
		return removeExtraSeparators(res);
	}

	private List<JComponent> removeExtraSeparators(List<JComponent> list) {
		if (list != null && list.size() > 0) {
			// na pocetku nam nikad ne trebaju separatori
			if (list.get(0) == null)
				list.remove(0);
			// ni na kraju
			if (list.get(list.size() - 1) == null)
				list.remove(list.size() - 1);
		}
		return list;
	}
}
