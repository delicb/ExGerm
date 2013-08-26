package tk.exgerm.console.listeners;

import java.io.PrintStream;

import tk.exgerm.console.parser.Parser;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IListener;

public class ConsoleParameterChangedListener implements IListener {

	private Parser parser;
	private PrintStream ps;

	public ConsoleParameterChangedListener(Parser parser, PrintStream ps) {
		this.parser = parser;
		this.ps = ps;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if ((ps == parameters[0])) {
			if (event.equals(ExGCommand.ACTIVE_GRAPH_CHANGED))
				parser.setActiveGraph((IGraph) parameters[1]);
			else if (event.equals(ExGCommand.ACTIVE_NODE_CHANGED)) {
				parser.setActiveNode((INode) parameters[1]);
			} else if (event.equals(ExGCommand.FILESYSTEM_PATH_CHANGED)) {
				parser.setFileSystemPath((String) parameters[1]);
			} else if (event.equals(ExGCommand.SHOW_PATH_IN_PROMPT_CHANGED))
				parser.setShowPathInPrompt((Boolean) parameters[1]);
		}
		parser.setPrompt();
	}
}
