package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class FilesystemCommand implements ExGCommand {

	ICoreContext coreContext;

	public FilesystemCommand(ICoreContext coreContext) {
		this.coreContext = coreContext;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,"Too many parameters.");
		else if (params.length == 1)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,"Filesystem on or off?. See help.");
		else if (!params[1].equalsIgnoreCase("on")
				&& !params[1].equalsIgnoreCase("off"))
			throw new ExGCommandErrorException("Unknown parameter "+params[1]+". Use 'on' or 'off'");
		
		else{
			if(params[1].equalsIgnoreCase("on"))
				coreContext.raise(ExGCommand.SHOW_PATH_IN_PROMPT_CHANGED, out, true);
			else
				coreContext.raise(ExGCommand.SHOW_PATH_IN_PROMPT_CHANGED, out, false);
		}
		return null;
	}

	@Override
	public String getHelp() {
		return "Displays current filesystem path in this console";
	}

	@Override
	public String getKeyword() {
		return "filesystem";
	}

	@Override
	public String getSyntax() {
		return "Use 'filesystem <on|off>' to toggle filesystem path display.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
