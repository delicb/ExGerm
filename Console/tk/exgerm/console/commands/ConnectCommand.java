package tk.exgerm.console.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class ConnectCommand implements ExGCommand {

	ICoreContext context;

	public ConnectCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Connects two or more nodes. Can be used only if graph or subgraph is active in console.\n"
				+ getSyntax();
	}

	@Override
	public String getKeyword() {
		return "connect";
	}

	@Override
	public String getSyntax() {
		return "Use 'connect <nodeA> <direction> <nodeB>'. Direction symbols are '<-', '->' and '--'";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {

		IGraph graph = (IGraph) context
				.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);
		INode node = (INode) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);

		IGraph useGraph;

		if (node != null)
			if (IGraph.class.isInstance(node))
				useGraph = (IGraph) node;
			else
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"Currently using node, so there is nothing to connect.");
		else if (graph != null)
			useGraph = graph;
		else
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"No active graph selected. Please, select graph first.");

		if (params.length % 2 != 0 || params.length == 2)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Illegal parameter count. Please, check your syntax.");

		List<INode> nodeList = new ArrayList<INode>();
		List<String> connTypes = new ArrayList<String>();
		INode current;

		for (int i = 0; i < params.length; i++) {
			// svaki neparni treba da je node
			if (i % 2 != 0) {
				current = useGraph.getNode(params[i]); // connect a -- b
				if (current != null)
					nodeList.add(current);
				else {
					throw new ExGCommandErrorException("Unknown node found "
							+ params[i]);
				}
				// a svaki parni počev od 2 operator
			} else if (i >= 2) {
				if (!params[i].equals("--") && !params[i].equals("->")
						&& !params[i].equals("<-"))
					throw new ExGCommandErrorException(
							"Invalid direction parameter found. Use '<-', '->' or '--'");
				else
					connTypes.add(params[i]);
			}
		}

		try {
			INode source;
			INode dest;
			String connType;
			IEdge edge;
			for (int i = 0; i < nodeList.size() - 1; i++) {
				source = nodeList.get(i);
				dest = nodeList.get(i + 1);
				connType = connTypes.get(i);
				if (connType.equals("--"))
					edge = useGraph.addEdge(source, dest, false);
				else if (connType.equals("->"))
					edge = useGraph.addEdge(source, dest, true);
				else if (connType.equals("<-"))
					edge = useGraph.addEdge(dest, source, true);
				else
					throw new ExGCommandErrorException(
							CommandErrorType.CRITICAL_ERROR,
							"Unexpected ConnectCommand error occured. (1)");
				out.println("New edge " + source.getName() + " " + connType
						+ " " + dest.getName() + " created! Edge ID: "
						+ edge.getID());
			}
		} catch (ExGNodeDoesNotExsistException e) {
			throw new ExGCommandErrorException(CommandErrorType.CRITICAL_ERROR,
					"Unexpected ConnectCommand error occured. (2)");
		}
		return null;
	}
}
