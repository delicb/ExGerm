package tk.exgerm.pluginmanager.commands;

import java.io.PrintStream;

import org.osgi.framework.Bundle;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.pluginmanager.ExGOSGiUpdateException;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Komanda proverava da li za određeni plugin postoji update
 * @author Duje
 *
 */
public class UpdateCommand implements ExGCommand {

	private PluginManager manager;

	public UpdateCommand(PluginManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length < 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Update what?");
		else if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Too many parameters. See help.");

		Bundle bundle = null;
		int ID;
		try {
			ID = Integer.parseInt(params[1]);
			bundle = manager.getContext().getBundle(ID);
		} catch (NumberFormatException e) {
			throw new ExGCommandErrorException(
					"Parameter should be plugin ID. Execute 'plugins' to find your plugin ID");
		}
		if (bundle != null) {
			try {
				manager.updateBundle(ID);
				out.println("Plugin " + bundle.getSymbolicName()
						+ " is successfully updated!");
			} catch (ExGOSGiUpdateException e) {
				throw new ExGCommandErrorException(e.getMessage());
			}

		} else {
			throw new ExGCommandErrorException(
					"Plugin with ID name does not exist!");
		}
		return bundle;
	}

	@Override
	public String getHelp() {
		return "Updates instaled plugin.";
	}

	@Override
	public String getKeyword() {
		return "update";
	}

	@Override
	public String getSyntax() {
		return "Use 'update <plugin_id>'. You can find plugin ID with 'plugins' command.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}
}
