package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class ListCommand implements ExGCommand {

	ICoreContext context;

	public ListCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Lists nodes/edges/subgraphs when used with different parameters.\n"
				+ getSyntax();
	}

	@Override
	public String getKeyword() {
		return "list";
	}

	@Override
	public String getSyntax() {
		return "Use 'list' to get all available info about currently active graph or node.\n"
				+ "Use 'list subgraphs' to get list of all subgraphs that active graph has.\n"
				+ "Use 'list <nodes|edges> to get list of specific elements.\n"
				+ "Use 'list all' to get complete graph list with all elements. Active graph/node is ignored.";
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

		if (params.length == 1) {
			if (node != null) {
				listConnections(out, node);
				if (IGraph.class.isInstance(node)) {
					listNodes(out, (IGraph) node);
					listEdges(out, (IGraph) node);
				}
			} else if (graph != null) {
				listNodes(out, graph);
				listEdges(out, graph);
			} else
				listGraphs(out);
		} else if (params.length == 2) {
			if (params[1].equals("subgraphs")) {
				if (node == null && graph == null)
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"No active node or graph.");
				INode useGraph;
				if (node != null)
					useGraph = node;
				else
					useGraph = graph;

				if (!IGraph.class.isInstance(useGraph))
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"Active graph is node and has no subgraphs.");
				else
					listSubgraphs(out, graph);
			} else if (params[1].equals("nodes")) {
				if (graph == null)
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING, "No active graph.");
				else
					listNodes(out, graph);
			} else if (params[1].equals("edges")) {
				if (graph == null)
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING, "No active graph.");
				else
					listEdges(out, graph);
			} else if (params[1].equals("all")) {
				listGraphsAll(out);
			} else {
				IGraph useGraph = context.getGraph(params[1]);
				if (useGraph == null)
					throw new ExGCommandErrorException("Unknown node/subgraph.");
				else {
					listNodes(out, useGraph);
					listEdges(out, useGraph);
				}
			}
		} else if (params.length == 3) {
			IGraph useGraph = context.getGraph(params[1]);
			if (useGraph == null)
				throw new ExGCommandErrorException("Unknown graph.");
			else {
				if (params[2].equals("nodes"))
					listNodes(out, useGraph);
				else if (params[2].equals("edges"))
					listEdges(out, useGraph);
				else {
					INode useNode = useGraph.getNode(params[2]);
					if (useNode == null)
						throw new ExGCommandErrorException("Unknown node.");
					else
						listConnections(out, useNode);
				}
			}
		} else
			throw new ExGCommandErrorException(
					CommandErrorType.WARNING, "Too many parameters. See help.");
		return null;
	}

	/**
	 * Piše po streamu sve veze noda sa drugim nodovima.
	 * 
	 * @param out
	 * @param node
	 */
	private void listConnections(PrintStream out, INode node) {
		if (node.getConnectedEdges().size() == 0)
			out.println("There are no connections.");
		else {
			out.println("Node connections:");
			for (IEdge edge : node.getConnectedEdges()) {
				out.print("\t" + edge.getID());
				out.println("\t" + edge.getFrom().getName()
						+ (edge.isDirected() ? " -> " : " -- ")
						+ edge.getTo().getName());
			}
			out.println("---------------------");
		}
	}

	/**
	 * Piše po streamu sve linkove jednog grafa
	 * 
	 * @param out
	 * @param graph
	 */
	private void listEdges(PrintStream out, IGraph graph) {
		if (graph.getAllEdges().size() == 0)
			out.println("There are no edges.");
		else {
			out.println("Edges:");
			String symbol;
			for (IEdge edge : graph.getAllEdges()) {
				if (edge.isDirected())
					symbol = " -> ";
				else
					symbol = " -- ";
				out.print("\t" + edge.getID());
				out.println("\t" + edge.getFrom().getName() + symbol
						+ edge.getTo().getName());
			}
			out.println("---------------------");
		}
	}

	/**
	 * Piše po streamu sve nodove jednog grafa
	 * 
	 * @param out
	 * @param graph
	 */
	private void listNodes(PrintStream out, IGraph graph) {
		if (graph.getAllNodes().size() == 0)
			out.println("There are no nodes.");
		else {
			out.println("Nodes:");
			for (INode node : graph.getAllNodes()) {
				out.print("\t" + node.getName());
				if (IGraph.class.isInstance(node))
					out.println("*");
				else
					out.println("");
			}
			out.println("---------------------");
		}
	}

	/**
	 * Piše po streamu sve grafove
	 * 
	 * @param out
	 */
	private void listGraphs(PrintStream out) {
		if (context.getAllGraphs() == null
				|| context.getAllGraphs().size() == 0)
			out.println("There are no graphs.");
		else {
			out.println("List of all graphs:");
			for (IGraph graph : context.getAllGraphs())
				out.println("\t" + graph.getName());
			out.println("---------------------");
		}
	}

	/**
	 * Piše po streamu sve podgrafove nekog grafa
	 * 
	 * @param out
	 * @param graph
	 */
	private void listSubgraphs(PrintStream out, IGraph graph) {
		if (graph.getAllNodes().size() == 0)
			out.println("There are no subgraphs.");
		else {
			out.println("List of all subgraphs:");
			for (INode node : graph.getAllNodes()) {
				if (IGraph.class.isInstance(node))
					out.println("\t" + node.getName());
			}
			out.println("---------------------");
		}
	}

	/**
	 * Ispisuje sve grafove sa svim svojim podacima
	 * 
	 * @param out
	 */
	private void listGraphsAll(PrintStream out) {
		if (context.getAllGraphs().size() == 0)
			out.println("There are no graphs.");
		else {
			out.println("ALL GRAPHS:");
			for (IGraph graph : context.getAllGraphs()) {
				out.println("-----> " + graph.getName() + " <-----");
				listNodes(out, graph);
				listEdges(out, graph);
			}
			out.println("------------------------------------------");
		}
	}
}
