package tk.exgerm.pluginmanager.commands;

import java.io.PrintStream;
import java.util.Vector;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Komanda lista sve pluginove koji su trenutno instalirani.
 *
 */
public class PluginsCommand implements ExGCommand {

	PluginManager manager;

	public PluginsCommand(PluginManager manager) {
		this.manager = manager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		if (params.length > 1)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"This command should be used without any parameters.");

		for (Vector<String> bundle : manager.getBundles())
			out.println(bundle.get(0) + " - " + bundle.get(1) + " - "
					+ bundle.get(2) + " - " + bundle.get(3));

		return null;
	}

	@Override
	public String getHelp() {
		return "Returns list of all plugins with their location and state.";
	}

	@Override
	public String getKeyword() {
		return "plugins";
	}

	@Override
	public String getSyntax() {
		return "This command should be used without any parameters.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
