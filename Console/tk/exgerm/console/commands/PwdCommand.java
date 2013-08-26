package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class PwdCommand implements ExGCommand {

	ICoreContext context;
	
	public PwdCommand(ICoreContext context) {
		this.context = context;
	}
	
	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length != 1) 
			throw new ExGCommandErrorException(
					CommandErrorType.WARNING,"pwd command does not accept any arguments");
		out.println(context.getData(CURRENT_FILESYSTEM_PATH));
		return null;
	}

	@Override
	public String getHelp() {
		return "Prints workding directory. Does not accept any arguments.\n" +
				"Also see 'cd' and 'dir' commands.";
	}

	@Override
	public String getKeyword() {
		return "pwd";
	}

	@Override
	public String getSyntax() {
		return "pwd - print working directory. Does not accept any arguments.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
