package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class UseCommand implements ExGCommand {

	ICoreContext context;

	public UseCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Sets using entered subgraph or node as active.\n" + getSyntax();
	}

	@Override
	public String getKeyword() {
		return "use";
	}

	@Override
	public String getSyntax() {
		return "Use 'use <name>'. Name is the name of node.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		if (params.length == 1)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Use what?");

		if (params.length > 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Use command should not have any extra parameters");

		IGraph graph = (IGraph) context
				.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);
		INode node = (INode) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);

		if (graph != null) {
			IGraph useGraph;
			if (node != null)
				useGraph = (IGraph) node;
			else
				useGraph = graph;
			INode useNode = useGraph.getNode(params[1]);
			if (useNode != null) {
				context.raise(ExGCommand.ACTIVE_NODE_CHANGED, out, useNode);
				return useNode;
			} else {
				throw new ExGCommandErrorException("Node unknown");
			}
		} else {
			IGraph useGraph = context.getGraph(params[1]);
			if (useGraph != null) {
				context.raise(ExGCommand.ACTIVE_GRAPH_CHANGED, out, useGraph);
				return useGraph;
			} else {
				throw new ExGCommandErrorException("Graph unknown");
			}
		}
	}
}
