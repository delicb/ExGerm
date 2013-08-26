package tk.exgerm.console.commands;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class AliasCommand implements ExGCommand {

	Map<String, String> aliases;
	ICoreContext context;

	public AliasCommand(ICoreContext coreContext, Map<String, String> aliases) {
		this.aliases = aliases;
		this.context = coreContext;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length == 1) {
			Iterator<String> alias = aliases.keySet().iterator();
			String key;
			if (aliases.size() == 0)
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"There are no registered aliases.");
			while (alias.hasNext()) {
				key = alias.next();
				out.println(key + " -> " + aliases.get(key));
			}

		} else if (params.length > 3) {
			throw new ExGCommandErrorException("Too many parameters. See help.");
		} else if (params.length == 2) {
			throw new ExGCommandErrorException("Too few parameters. See help.");
		} else {
			aliases.put(params[1], params[2]);
			out.println("New alias '"+params[1]+"' registered!");
		}
		return null;
	}

	@Override
	public String getHelp() {
		return "Creates command alias.";
	}

	@Override
	public String getKeyword() {
		return "alias";
	}

	@Override
	public String getSyntax() {
		return "Use 'alias <alias> <command_string> to register new alias. <alias> is name for new alias. <command_string> can be any command with or without parameters. Use quotes if command_string contains spaces. Use 'alias' to display list of all registered alieases.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
