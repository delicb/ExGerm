package tk.exgerm.pluginmanager.commands;

import java.io.PrintStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Komanda pokreće instaliran plugin.
 *
 */
public class StartCommand implements ExGCommand {

	private PluginManager manager;

	public StartCommand(PluginManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length < 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Start what?");
		else if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Too many parameters. See help.");

		Bundle install = null;
		try {
			install = manager.getContext().getBundle(Integer.parseInt(params[1]));
		} catch (NumberFormatException e) {
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Parameter should be plugin ID. Execute 'plugins' to find your plugin ID");
		}
		if (install != null) {
			try {
				install.start();
				out.println("Plugin " + install.getSymbolicName()
						+ " is successfully started!");
			} catch (BundleException e) {
				throw new ExGCommandErrorException(e.getMessage());
			}
		} else {
			out.println();
			throw new ExGCommandErrorException(
					"Plugin with typed ID does not exist!");
		}
		return install;
	}

	@Override
	public String getHelp() {
		return "Starts resolved plugin.";
	}

	@Override
	public String getKeyword() {
		return "start";
	}

	@Override
	public String getSyntax() {
		return "Use 'start <plugin_id>'. You can find plugin ID with 'plugins' command.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
