package tk.exgerm.pluginmanager;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.pluginmanager.actions.Check4UpdatesAction;
import tk.exgerm.pluginmanager.actions.PluginManagerAction;
import tk.exgerm.pluginmanager.commands.InstallCommand;
import tk.exgerm.pluginmanager.commands.PluginsCommand;
import tk.exgerm.pluginmanager.commands.StartCommand;
import tk.exgerm.pluginmanager.commands.StopCommand;
import tk.exgerm.pluginmanager.commands.UninstallCommand;
import tk.exgerm.pluginmanager.commands.UpdateCommand;
import tk.exgerm.pluginmanager.gui.PluginManagerMainWindow;
import tk.exgerm.pluginmanager.help.PluginManagerHelp;

public class PluginManagerService implements IComponent {

	private ICoreContext coreContext;
	private BundleContext bundleContext;
	private PluginManager pluginManager;
	private PluginManagerMainWindow window;

	private PluginManagerAction managerAction;
	private Check4UpdatesAction updatesAction;

	private List<ExGCommand> commands;
	private PluginManagerHelp help;

	public PluginManagerService(BundleContext context) {
		this.bundleContext = context;
		commands = new ArrayList<ExGCommand>();
	}

	@Override
	public void setContext(ICoreContext coreContext) {
		pluginManager = new PluginManager(coreContext, bundleContext, this);
		managerAction = new PluginManagerAction(pluginManager);
		updatesAction = new Check4UpdatesAction(pluginManager);
		help = new PluginManagerHelp();
		this.coreContext = coreContext;
		coreContext.addAction(managerAction);
		coreContext.addAction(updatesAction);
		coreContext.registerHelp(help);
		registerCommands();
	}

	/**
	 * Prijavljuje sve komande PM-a Coru.
	 */
	private void registerCommands() {
		try {
			registerCommand(new InstallCommand(pluginManager));
			registerCommand(new UninstallCommand(pluginManager));
			registerCommand(new PluginsCommand(pluginManager));
			registerCommand(new StartCommand(pluginManager));
			registerCommand(new StopCommand(pluginManager));
			registerCommand(new UpdateCommand(pluginManager));
		} catch (ExGCommandAlreadyExistException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registruje prosleđenu komandu u Coru i pamti je radi uklanjanja kada se
	 * bundle stopira.
	 * 
	 * @param command
	 *            koja se registruje
	 * @throws ExGCommandAlreadyExistException
	 *             ukoliko već postoji komanda
	 */
	private void registerCommand(ExGCommand command)
			throws ExGCommandAlreadyExistException {
		commands.add(command);
		coreContext.registerCommand(command);
	}

	/**
	 * Uklanja sve što je servis dodao. Ovu metodu poziva stop() metoda bundlea.
	 */
	public void shutDown() {
		coreContext.removeAction(managerAction);
		coreContext.removeAction(updatesAction);
		coreContext.removeHelp(help);

		for (ExGCommand command : commands)
			coreContext.removeCommand(command);
	}

	/**
	 * Vraća, a a ko ne postoji generiše, novi glavni prozor PM-a
	 * @return
	 */
	public PluginManagerMainWindow getWindow() {
		if (window == null)
			window = new PluginManagerMainWindow(pluginManager);
		return window;
	}
}
