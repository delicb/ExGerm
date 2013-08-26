package tk.exgerm.pluginmanager.commands;

import java.io.File;
import java.io.PrintStream;

import org.osgi.framework.Bundle;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.pluginmanager.PluginManager;

/**
 * Komanda za instalaciju novog plugina. Prepoznaje putanju i prosleđuje je
 * glavnoj PluginManager klasi na dalju obradu.
 *
 */
public class InstallCommand implements ExGCommand {

	private PluginManager manager;

	public InstallCommand(PluginManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		if (params.length < 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Install what?");
		else if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Too many parameters. See help.");

		Bundle install = null;
		String enteredPath = params[1];
		String fullPath = "";
		File filePath = new File(enteredPath);
		if (!filePath.isAbsolute())
			fullPath += manager.getCoreContext().getData(
					ExGCommand.CURRENT_FILESYSTEM_PATH)
					+ File.separator;
		fullPath += enteredPath;

		try {
			install = manager.installFilesysetem(fullPath);
		} catch (Exception e) {
			throw new ExGCommandErrorException(e.getMessage());
		}
		out.println("Plugin " + install.getSymbolicName()
				+ " is successfully installed!" + "\nPlugin ID: "
				+ install.getBundleId());
		return install;
	}

	@Override
	public String getHelp() {
		return "Installs new plugin into exGERM";
	}

	@Override
	public String getKeyword() {
		return "install";
	}

	@Override
	public String getSyntax() {
		return "Use 'install <path>', where path should be like \"C:\\plugins\\my little plugin\\plugin.jar or relative to current path like: /plugins/plugin.jar";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
