package tk.exgerm.pluginmanager.commands;

import java.io.PrintStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Komanda vrši deinstalaciju plugina
 *
 */
public class UninstallCommand implements ExGCommand {

	private PluginManager manager;

	public UninstallCommand(PluginManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length < 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Uninstall what?");
		else if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Too many parameters. See help.");

		Bundle bundle = null;
		try {
			bundle = manager.getContext()
					.getBundle(Integer.parseInt(params[1]));
		} catch (NumberFormatException e) {
			throw new ExGCommandErrorException(
					"Parameter should be plugin ID. Execute 'plugins' to find your plugin ID");
		}
		if (bundle != null) {
			try {
				bundle.uninstall();
				out.println("Plugin " + bundle.getSymbolicName()
						+ " is successfully uninstalled!");
			} catch (BundleException e) {
				out.println(e.getMessage());
				return null;
			}
		} else {
			throw new ExGCommandErrorException(
					"Plugin with ID name does not exist!");
		}
		return bundle;
	}

	@Override
	public String getHelp() {
		return "Uninstalls instaled plugin.";
	}

	@Override
	public String getKeyword() {
		return "uninstall";
	}

	@Override
	public String getSyntax() {
		return "Use 'uninstall <plugin_id>'. You can find plugin ID with 'plugins' command.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
