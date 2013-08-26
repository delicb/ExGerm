package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGEdgeDoesNotExsistException;
import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class DeleteCommand implements ExGCommand {

	ICoreContext context;

	public DeleteCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Deletes edge, node or graph. If there is no active graph selected, you can only"
				+ " delete graph. Otherwise, you can delete node or edge."
				+ getSyntax();
	}

	@Override
	public String getKeyword() {
		return "delete";
	}

	@Override
	public String getSyntax() {
		return "Use 'delete <graph|node|edge> <name>'.";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length < 3)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Delete what?");
		else if (params.length > 3)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Too many parameters");
		else if (!params[1].equals("node") && !params[1].equals("graph")
				&& !params[1].equals("edge"))
			throw new ExGCommandErrorException("Invalid element type "
					+ params[1] + ". Use graph, node or edge.");
		else {
			IGraph graph = (IGraph) context
					.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);
			INode node = (INode) context
					.getData(ExGCommand.CONSOLE_ACTIVE_NODE);

			if (node != null) {
				if (IGraph.class.isInstance(node)) {
					if (params[1].equals("node"))
						deleteNode((IGraph) node, out, params[2]);
					else if (params[1].equals("edge"))
						deleteEdge((IGraph) node, out, params[2]);
					else if (params[1].equals("graph"))
						throw new ExGCommandErrorException(
								CommandErrorType.WARNING,
								"Active node (subgraph) is selected. You can delete node or edge only.");
				} else
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"Active NODE (not subgraph) is selected. There is nothing to delete.");
			} else if (graph != null) {
				if (params[1].equals("node"))
					deleteNode(graph, out, params[2]);

				else if (params[1].equals("edge"))
					deleteEdge(graph, out, params[2]);
				else if (params[1].equals("graph"))
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"Active graph is selected. You can delete node or edge only.");
			} else {
				if (!params[1].equals("graph"))
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"No active graph/subgraph selected. First, select active graph");
				else {
					try {
						context.removeGraph(params[2]);
						out.println("Graph deleted: " + params[2]);
					} catch (ExGGraphDoesNotExsistException e) {
						throw new ExGCommandErrorException(
								"Graph with that name does not exist!");
					}
				}
			}
		}
		return null;
	}

	/**
	 * Briše zadati node.
	 * 
	 * @param graph
	 *            iz kojeg se briše node
	 * @param out
	 *            PrintStream izlaz
	 * @param name
	 *            ime noda
	 * @throws ExGCommandErrorException
	 *             poruke greške
	 */
	private void deleteNode(IGraph graph, PrintStream out, String name)
			throws ExGCommandErrorException {
		try {
			graph.removeNode(name);
			out.println("Node deleted: " + name);
		} catch (ExGNodeDoesNotExsistException e) {
			throw new ExGCommandErrorException(CommandErrorType.ERROR, "Graph "
					+ graph.getName() + " doesn't have node with this name.");
		}
	}

	/**
	 * Briše zadati edge.
	 * 
	 * @param graph
	 *            iz kojeg se briše edge
	 * @param out
	 *            PrintStream izlaz
	 * @param id
	 *            ide edga
	 * @throws ExGCommandErrorException
	 *             poruke greške
	 */
	private void deleteEdge(IGraph graph, PrintStream out, String id)
			throws ExGCommandErrorException {
		try {
			graph.removeEdge(Integer.parseInt(id));
			out.println("Edge with ID:" + id + " deleted!");
		} catch (NumberFormatException e) {
			throw new ExGCommandErrorException("Edge ID (number) expected!");
		} catch (ExGEdgeDoesNotExsistException e) {
			throw new ExGCommandErrorException(
					"Edge with that ID does not exist!");
		}
	}
}
