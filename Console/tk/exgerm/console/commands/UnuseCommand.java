package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class UnuseCommand implements ExGCommand {

	ICoreContext context;

	public UnuseCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		IGraph graph = (IGraph) context
				.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);
		INode node = (INode) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);

		if (graph == null && node == null)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Nothing to unuse.");

		if (params.length > 1) {
			if (params[1].equals("all")) {
				if (node != null)
					context.raise(ExGCommand.ACTIVE_NODE_CHANGED, out, null);
				context.raise(ExGCommand.ACTIVE_GRAPH_CHANGED, out, null);
			} else {
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"Only parameter 'all' is expected.");
			}
		} else {
			if (node == null) {
				context.raise(ExGCommand.ACTIVE_GRAPH_CHANGED, out, null);
			} else {
				if (node.getGraph() == graph)
					context.raise(ExGCommand.ACTIVE_NODE_CHANGED, out, null);
				else
					context.raise(ExGCommand.ACTIVE_NODE_CHANGED, out, node
							.getGraph());
			}
		}
		return null;
	}

	@Override
	public String getHelp() {
		return "Switches back active graph/subgraph/node.\n" + getSyntax();
	}

	@Override
	public String getKeyword() {
		return "unuse";
	}

	@Override
	public String getSyntax() {
		return "Use command without any parameters ('unuse') to get parent graph/subgraph. Use 'unuse all' to get to root with no active graph selected.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
