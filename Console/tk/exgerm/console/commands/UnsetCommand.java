package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class UnsetCommand implements ExGCommand {

	ICoreContext context;

	public UnsetCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Removes an attribute from node/graph/edge.\n" + getSyntax();
	}

	@Override
	public String getKeyword() {
		return "unset";
	}

	@Override
	public String getSyntax() {
		return "unset [<edge_id>] <attribute_name>\n"
				+ "Graph or a node must be selected to attach attribute to.\n";
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
		if (params.length != 2 && params.length != 3)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Illegal number of parameters.\nExpected <unset property_name>");
		if (graph == null)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"No graph or node selected to set parameter to.");
		else {
			if (params.length == 2)
				if (node == null) {
					// uklanjanje sa selektovanog grafa propertija
					graph.removeAttribute(params[1]);
					out.println("Attribute " + params[1] + " unset for graph "
							+ graph.getName());
				} else {
					node.removeAttribute(params[1]);
					out.println("Attribute " + params[1] + " unset for node "
							+ node.getName());
				}
			if (params.length == 3) {
				//params[1] je id, params[2] je atribut
				int id;
				try {
					id = Integer.parseInt(params[1]);
				} catch (NumberFormatException e) {
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING,
							params[1]
									+ "is not a legal edge ID! ID must be a number.");
				}
				if (graph.getEdge(id) == null)
					throw new ExGCommandErrorException(
							CommandErrorType.WARNING, "Edge with id " + id
									+ " not found!");
				graph.getEdge(id).removeAttribute(params[2]);
				out.println("Attribute " + params[2] + " unset for edge "
						+ id);
			}
		}
		return null;
	}
}
