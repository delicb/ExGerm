package tk.exgerm.core.gui;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import tk.exgerm.core.Core;
import tk.exgerm.core.actions.ActionRegister;
import tk.exgerm.core.gui.konami.Konami;
import tk.exgerm.core.gui.tabmanager.TabManager;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;

/**
 * <p>
 * Glavni prozor aplikacije. U suštini prozor bi trebao da bude manje - više
 * prazan i da ga popunjavaju ostale komponente koje implementiraju
 * {@link tk.exgerm.core.plugin.IGraphicalComponent} interfejs.
 * <p>
 * U suštini, glavna stvar koja u ovoj klasi treba da se čuva je više
 * {@link TabManager tab menadžera}, a svaki od njih treba da čuva koje
 * komponente prikazuje.
 * 
 * @author Tim 2
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 7079934648220452008L;

	/**
	 * self-explanatory
	 */
	private StatusBar statusBar;

	/**
	 * Jedina labela koja uvek postoji. Služi da bi komponente mogle da postave
	 * jednostavne poruke u statusbar, a da ne moraju da registruju svoje labele
	 * (ili druge komponente) u statusbar.
	 */
	private DefaultStatusbarLabel label;

	/**
	 * self-explanatory
	 */
	private ActionManager actionManager;

	/**
	 * self-explanatory
	 */
	private ActionRegister actionRegister;

	/**
	 * self-explanatory
	 */
	private TabManager tabManager;

	/*
	 * FIXME
	 */
	public TabManager getTabManager() {
		return tabManager;
	}

	/**
	 * Kreira sve što je potrebno da bi se prozor prikazao i funkcionisao.
	 * 
	 * TODO: Dodati javadoc kad se implementira kod.
	 */
	public MainWindow() {

		try {
			URL titlebarIcon = getClass().getResource("images/icon16.png");
			if (titlebarIcon != null)
				setIconImage(ImageIO.read(titlebarIcon));
		} catch (Exception ex) {
		}

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		this.setTitle("ExGERM - by Tim 2");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(MainWindow.DO_NOTHING_ON_CLOSE);

		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		this.addWindowListener(new WindowAdapter() {
			/*
			 * Auto-generated method stub super.windowClosing(e);
			 */
			@Override
			public void windowClosing(WindowEvent e) { // TODO
				Core.getInstance().close();
			}
		});

		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				Core.getInstance().getEventDispatcher().raiseEvent(
						ICoreContext.MAIN_WINDOW_STATE_CHANGED, e.getNewState(),
						e.getOldState());
				super.windowStateChanged(e);
			}
		});
		this.initializeActionManager();
		this.initializeTabManager();
		this.initializeStatusbar();
		this.initializeActions();

		// don't ask
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new Konami());
	}

	/**
	 * Postavlja poruku na {@link StatusBar statusbar}. Ovo bi trebalo da
	 * koriste komponente koje imaju potrebu da ponekad koriste statusbar, ali
	 * ne registruju sopstvene komponente na statusbaru.
	 * 
	 * @param message
	 */
	public void setStatusbarMessage(String message) {
		this.label.setMessage(message);
	}

	/**
	 * @return the statusBar
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}

	public ActionManager getActionManager() {
		return this.actionManager;
	}

	/**
	 * Inicijalizacija menija
	 */
	private void initializeActionManager() {
		actionManager = new ActionManager(this);

		actionManager.registerMenu(ExGAction.FILE_MENU, "File");
		actionManager.registerMenu(ExGAction.EDIT_MENU, "Edit");
		actionManager.registerMenu(ExGAction.VIEW_MENU, "View");
		actionManager.registerMenu(ExGAction.TOOLS_MENU, "Tools");
		actionManager.registerMenu(ExGAction.PLUGINS_MENU, "Plugins");
		actionManager.registerMenu(ExGAction.HELP_MENU, "Help");

		actionManager.registerToolbar(ExGAction.MAIN_TOOLBAR);
	}

	/**
	 * Inicijalizacija statusbar-a
	 */
	private void initializeStatusbar() {
		statusBar = new StatusBar();
		label = new DefaultStatusbarLabel();
		statusBar.addComponent(label);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Inicijalizacija tabmenager-a
	 */
	private void initializeTabManager() {
		tabManager = new TabManager();
		this.getContentPane().add(tabManager, BorderLayout.CENTER);
	}

	private void initializeActions() {
		this.actionRegister = new ActionRegister();

		actionManager.addAction(actionRegister
				.getAction(ActionRegister.showHideStatusbarAction));
		actionManager.addAction(actionRegister
				.getAction(ActionRegister.exitApplication));
		actionManager.addAction(actionRegister
				.getAction(ActionRegister.showConfiguration));
	}

}
