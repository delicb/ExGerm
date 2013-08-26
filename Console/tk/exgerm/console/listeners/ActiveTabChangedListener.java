package tk.exgerm.console.listeners;

import tk.exgerm.console.gui.Console;
import tk.exgerm.console.parser.Parser;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;

/**
 * Obrađuje događaje promene aktivnog taba, zatvaranje jednog ili više tabova...
 * Događaje bi trebalo da ispaljuju KOMANDE, po tome se razlikuje od
 * TabEventsListenera čije događaje ispaljuje TabManager!
 * 
 */
public class ActiveTabChangedListener implements IListener {

	private ICoreContext context;
	private Parser parser;
	private Console console;

	public ActiveTabChangedListener(ICoreContext context, Console console,
			Parser parser) {
		this.context = context;
		this.console = console;
		this.parser = parser;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if (parameters[0] == console) {
			context.addData(ExGCommand.CONSOLE_ACTIVE_GRAPH, parser
					.getActiveGraph());
			context.addData(ExGCommand.CONSOLE_ACTIVE_NODE, parser.getActiveNode());
			context.addData(ExGCommand.CURRENT_FILESYSTEM_PATH, parser.getFileSystemPath());
		}
	}
}
