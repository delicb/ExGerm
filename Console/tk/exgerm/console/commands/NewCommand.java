package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class NewCommand implements ExGCommand {

	ICoreContext context;

	public NewCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Creates new node or graph/subgraph and adds it to active graph/subgraph.\n"
				+ getSyntax();
	}

	@Override
	public String getKeyword() {
		return "new";
	}

	@Override
	public String getSyntax() {
		return "Use 'new <graph|node> <name>'. Names must be unique inside current graph/subgraph.";
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

		if (params.length < 3 || params.length > 4) {
			throw new ExGCommandErrorException(
					CommandErrorType.WARNING,
					"Illegal number of parameters.\nExpected node/graph followed by name or edge folowed by node node.");
		} else {
			if (params[1].equalsIgnoreCase("graph")) {
				IGraph newGraph = context.newGraph(params[2]);
				if (graph == null) {
					try {
						context.addGraph(newGraph);
						out.println("New graph created: " + params[2]);
					} catch (ExGGraphExsistException e) {

						throw new ExGCommandErrorException(
								"This graph already exist in graph register. Try another name.");
					}
				} else {
					IGraph useNode;
					if (node != null)
						if (IGraph.class.isInstance(node))
							useNode = (IGraph) node;
						else
							throw new ExGCommandErrorException(
									CommandErrorType.WARNING,
									"Active node is not a subgraph. Could not add new subgraph.");
					else
						useNode = graph;
					try {
						useNode.addNode(newGraph);
						out.println("New subgraph created: " + params[2]);
					} catch (ExGNodeAlreadyExsistException e) {
						throw new ExGCommandErrorException(
								"This graph already has subgraph with that name.");
					}
				}
				return newGraph;
			} else if (params[1].equalsIgnoreCase("node")) {
				INode newNode = context.newNode(params[2]);
				if (graph == null) {
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							"Graph not selected. This node will not belong to any graph!");
				}
				IGraph useNode;
				if (node != null)
					if (IGraph.class.isInstance(node))
						useNode = (IGraph) node;
					else {
						throw new ExGCommandErrorException(
								CommandErrorType.WARNING,
								"Active node is not a subgraph. Could not add new subgraph.");
					}
				else
					useNode = graph;
				try {
					useNode.addNode(newNode);
					out.println("New node created: " + params[2]);
				} catch (ExGNodeAlreadyExsistException e) {
					throw new ExGCommandErrorException(
							"This graph already has node with that name. Try another name");
				}
				return newNode;
			} else
				throw new ExGCommandErrorException(CommandErrorType.WARNING,
						"Unknown element. 'node' or 'graph' expected.");
		}
	}
}
