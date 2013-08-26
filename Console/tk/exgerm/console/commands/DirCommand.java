package tk.exgerm.console.commands;

import java.io.File;
import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class DirCommand implements ExGCommand {

	private ICoreContext context;

	public DirCommand(ICoreContext context) {
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length != 1 && params.length != 2 && params.length != 3)
			throw new ExGCommandErrorException(
					ExGCommandErrorException.CommandErrorType.ERROR,
					"Wrong number of parameters. See help dir");

		File dir = getDir(params);
		boolean showAll = hasAttr("-a", params);

		if (!dir.exists()) {
			throw new ExGCommandErrorException("Directory does not exist");
		}

		if (dir.isDirectory())
			for (File f : dir.listFiles()) {
				if ((f.isHidden() && showAll) || !f.isHidden()) {
					if (f.isDirectory())
						out.println("\t<" + f.getName() + ">");
					else
						out.println("\t" + f.getName());
				}
			}
		else
			throw new ExGCommandErrorException(dir.getName()
					+ " is not directory");

		return null;
	}

	@Override
	public String getHelp() {
		return "dir command lists all files.\n"
				+ "If parameter is not provided it lists all files in current\n"
				+ "directory, and if parameter is valid directory it lists\n"
				+ "it's fils.\n\n"
				+ "It is posible to pass -a switch to see hidden files";
	}

	@Override
	public String getKeyword() {
		return "dir";
	}

	@Override
	public String getSyntax() {
		return "dir [path] [-a] - lists all files in directory provided or"
				+ " current directory";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	private boolean hasAttr(String attr, String... params) {
		for (String s : params) {
			if (s.equals(attr))
				return true;
		}
		return false;
	}

	private File getDir(String... params) {
		String passedDirectory = "";
		// namerno preskacemo prvi parametar, jer je to ime komande
		for (int i = 1; i < params.length; i++) {
			if (!params[i].startsWith("-")) {
				passedDirectory = params[i];
				break;
			}
		}

		File dir = null;
		File tmp = new File(passedDirectory);
		if (tmp.isAbsolute())
			dir = tmp;
		else
			dir = new File(context.getData(CURRENT_FILESYSTEM_PATH)
					+ File.separator + passedDirectory);
		return dir;
	}

}
