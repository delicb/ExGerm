package tk.exgerm.core.commands;

import java.io.PrintStream;

import tk.exgerm.core.Core;
import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.plugin.ExGCommand;

public class ExitCommand implements ExGCommand {

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		Core.getInstance().close();
		return null;
	}

	@Override
	public String getHelp() {
		return "Closes program. Requires no parameters.";
	}

	@Override
	public String getKeyword() {
		return "exit";
	}

	@Override
	public String getSyntax() {
		return "Closes program. No parameters.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
