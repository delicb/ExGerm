package tk.exgerm.console;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;

import tk.exgerm.console.actions.NewConsoleAction;
import tk.exgerm.console.commands.AliasCommand;
import tk.exgerm.console.commands.AttributesCommand;
import tk.exgerm.console.commands.CdCommand;
import tk.exgerm.console.commands.ConnectCommand;
import tk.exgerm.console.commands.DeleteCommand;
import tk.exgerm.console.commands.DirCommand;
import tk.exgerm.console.commands.FilesystemCommand;
import tk.exgerm.console.commands.HelpCommand;
import tk.exgerm.console.commands.ListCommand;
import tk.exgerm.console.commands.NewCommand;
import tk.exgerm.console.commands.PwdCommand;
import tk.exgerm.console.commands.SetCommand;
import tk.exgerm.console.commands.UnsetCommand;
import tk.exgerm.console.commands.UnuseCommand;
import tk.exgerm.console.commands.UseCommand;
import tk.exgerm.console.gui.Console;
import tk.exgerm.console.gui.ConsoleConfigWindow;
import tk.exgerm.console.help.ConsoleHelp;
import tk.exgerm.console.listeners.TabEventsListener;
import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;

public class ConsoleService implements IComponent {

	/*
	 * CoreContext na koji se komponenta povezuje
	 */
	private ICoreContext coreContext;

	/*
	 * OSGI context u okviru koga se pokrece ConsoleService
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	private List<ConsoleView> consoleViews;

	private ArrayList<ExGCommand> commands;

	private NewConsoleAction newConsoleAction;

	private Map<String, String> aliases;

	private IListener listener;

	private ConsoleHelp help;

	private ConsoleConfigWindow configWindow;

	public ConsoleService(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		consoleViews = new ArrayList<ConsoleView>();
		aliases = new HashMap<String, String>();
		commands = new ArrayList<ExGCommand>();
		listener = new TabEventsListener(this);
		help = new ConsoleHelp();
	}

	@Override
	public void setContext(ICoreContext context) {
		this.coreContext = context;
		checkForConfig();
		newConsoleAction = new NewConsoleAction(context, this);
		context.addAction(newConsoleAction);
		context.registerHelp(help);

		if (consoleViews.size() == 0) {
			ConsoleView consoleView = new ConsoleView(new Console(coreContext,
					aliases, "Console " + 1), 1);
			consoleViews.add(consoleView);
			coreContext.addGraphicalComponent(consoleView);
		} else
			for (ConsoleView cv : consoleViews)
				coreContext.addGraphicalComponent(cv);

		registerCommands();
		context.listenEvent(ExGGraphicalComponent.TAB_CLOSED, listener);
		context.listenEvent(ExGGraphicalComponent.TAB_TITLE_CHANGED, listener);

		configWindow = new ConsoleConfigWindow(context);
		context.addConfigPane(configWindow);
	}

	/**
	 * Uklanja iz konzole sve grafičke komponente (sve konzole) ako postoje
	 */
	public void shutDown() {
		for (ConsoleView cv : consoleViews)
			coreContext.removeGraphicalComponent(cv);
		unRegisterCommands();
		coreContext.removeAction(newConsoleAction);
		coreContext.removeListener(listener);
		coreContext.removeHelp(help);
		coreContext.removeConfigPane(configWindow);
	}

	/**
	 * Vrši registraciju svih default komandi u Coru. Ukoliko baci
	 * ExGCommandAlreadyExistException znači da je komande već registrovala neka
	 * konzola.
	 */
	private void registerCommands() {
		try {
			registerCommand(new ConnectCommand(coreContext));
			registerCommand(new DeleteCommand(coreContext));
			registerCommand(new ListCommand(coreContext));
			registerCommand(new NewCommand(coreContext));
			registerCommand(new SetCommand(coreContext));
			registerCommand(new UnsetCommand(coreContext));
			registerCommand(new AttributesCommand(coreContext));
			registerCommand(new UseCommand(coreContext));
			registerCommand(new UnuseCommand(coreContext));
			registerCommand(new HelpCommand(coreContext));
			registerCommand(new AliasCommand(coreContext, aliases));
			registerCommand(new CdCommand(coreContext));
			registerCommand(new DirCommand(coreContext));
			registerCommand(new PwdCommand(coreContext));
			registerCommand(new FilesystemCommand(coreContext));
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
	 * Briše sve komande koje je konzola prijavila iz Cora. Ovu metodu poziva
	 * stop() metoda bundlea.
	 */
	public void unRegisterCommands() {
		for (ExGCommand command : commands)
			coreContext.removeCommand(command);
	}

	public List<ConsoleView> getConsoleViews() {
		return consoleViews;
	}

	public Map<String, String> getAliases() {
		return aliases;
	}

	public int getUniqueID() {
		int id = 0;
		for (ConsoleView v : consoleViews) {
			if (v.getID() > id)
				id = v.getID();
		}
		return id + 1;
	}

	/**
	 * Proverava da li postoji konfiguracija u sistemu, ukoliko ne postoji
	 * postavlja default vrednosti.
	 */
	private void checkForConfig() {
		if (coreContext.getConfigData("console_font_size") == null) {
			coreContext.putConfigData("console_font_size", "14");
			coreContext.putConfigData("console_syntax_color", String
					.valueOf(Color.BLUE.getRGB()));
			coreContext.putConfigData("console_semantic_color", String
					.valueOf(Color.RED.getRGB()));
			coreContext.putConfigData("console_command_color", String
					.valueOf(Color.ORANGE.getRGB()));
			coreContext.putConfigData("console_font_style", String
					.valueOf(Font.PLAIN));
			coreContext.putConfigData("console_font_name","Consolas");
		}
	}
}
