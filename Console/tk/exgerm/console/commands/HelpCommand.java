package tk.exgerm.console.commands;

import java.io.PrintStream;
import java.util.List;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class HelpCommand implements ExGCommand {

	ICoreContext context;

	public HelpCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length == 1) {
			List<ExGCommand> commands = context.getAllCommands();
			out.print("Available commands:  ");
			for (ExGCommand com : commands)
				out.print(com.getKeyword() + " ");
			out
					.println("\nUse 'help <command>' to get command syntax help or 'help -v <command>' for command manual.");
		} else if (params.length == 2) {
			if (params[1].equals("-v"))
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"Command name expected.");
			ExGCommand command = context.getCommand(params[1]);
			if (command == null)
				throw new ExGCommandErrorException(
						"Typed command does not exist.");
			else
				out.println(command.getSyntax());
		} else if (params.length == 3) {
			if (params[1].equals("-v")) {
				ExGCommand command = context.getCommand(params[2]);
				if (command == null)
					throw new ExGCommandErrorException(
							"Typed command does not exist.");
				else
					out.println(command.getHelp());
			} else
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"Invalid switch.");
		}
		return null;
	}

	@Override
	public String getHelp() {
		return "This command lists all available commands if typed without parameters."
				+ " If it is followed by a command name, command syntax will be described";
	}

	@Override
	public String getKeyword() {
		return "help";
	}

	@Override
	public String getSyntax() {
		return "Displays available commands. Use 'help <command>' to get command syntax help. Use 'help -v <command>' to get command help.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
