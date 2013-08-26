package tk.exgerm.graphstatis;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphstatis.commands.StatsCommand;
import tk.exgerm.graphstatis.statisticTools.StatsToolsRegister;
import tk.exgerm.graphstats.help.GraphStatsHelp;

public class GraphStatsService implements IComponent {

	/**
	 * Za komunikaciju sa Core-om...
	 */
	protected ICoreContext context;

	/**
	 * Registar svih alata za statistiku.
	 */
	protected StatsToolsRegister register = new StatsToolsRegister();

	/**
	 * Lista svih registrovanih komandi za čišćenje.
	 */
	protected List<ExGCommand> commands = new ArrayList<ExGCommand>();
	
	/**
	 * Za help komponentu...
	 */
	private GraphStatsHelp help;

	@Override
	public void setContext(ICoreContext context) {
		this.context = context;
		this.help = new GraphStatsHelp();
		this.context.registerHelp(help);
		regiserCommands();
		registerActions();
	}

	public void stop() {
		unregisterCommands();
		unregisterActions();
		this.context.removeHelp(help);
	}

	private void regiserCommands() {
		registerCommand(new StatsCommand(register, context));
	}
	
	private void registerCommand(ExGCommand command) {
		this.commands.add(command);
		try {
			this.context.registerCommand(command);
		} catch (ExGCommandAlreadyExistException e) {
			JOptionPane
			.showMessageDialog(
					null,
					"GraphStats is trying to register already register command",
					"GraphStats starting error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void registerActions() {}

	private void unregisterCommands() {
		for (ExGCommand command : this.commands) {
			this.context.removeCommand(command);
		}
	}

	private void unregisterActions() {}

}
