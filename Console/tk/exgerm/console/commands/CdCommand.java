package tk.exgerm.console.commands;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class CdCommand implements ExGCommand {

	private ICoreContext context;

	public CdCommand(ICoreContext context) {
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length != 2) {
			throw new ExGCommandErrorException("Invalid number of parameters! see help cd");
		}

		File dir = getDir(params);
		if (dir.exists() && dir.isDirectory())
			try {
				context.raise(ExGCommand.FILESYSTEM_PATH_CHANGED, out, dir
						.getCanonicalPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			throw new ExGCommandErrorException("Does not exist or not directory!");
		return null;
	}

	@Override
	public String getHelp() {
		return "cd (change directory) changes current active directory. \n"
				+ "It's only parameter is directory user wants to be active. \n"
				+ "This command can handle relative and absolute paths, as well as"
				+ "'..' syntax that is known from operating systems.";
	}

	@Override
	public String getKeyword() {
		return "cd";
	}

	@Override
	public String getSyntax() {
		return "cd <dir> - Change working directory. <dir> parameter is "
				+ "directory user wish to be current.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	private File getDir(String... params) {
		String passedDirectory = "";
		// namerno preskacemo prvi parametar, jer je to ime komande
		for (int i = 1; i < params.length; i++) {
			if (!params[i].startsWith("-"))
				passedDirectory = params[i];
		}

		File dir = null;
		File tmp = new File(passedDirectory);
		if (tmp.isAbsolute())
			dir = new File(passedDirectory);
		else
			dir = new File(context.getData(CURRENT_FILESYSTEM_PATH)
					+ File.separator + passedDirectory);
		return dir;
	}

}
