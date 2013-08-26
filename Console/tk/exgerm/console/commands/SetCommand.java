package tk.exgerm.console.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class SetCommand implements ExGCommand {

	ICoreContext context;

	public SetCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public String getHelp() {
		return "Adds a new attribute to node/graph/edge.\n" + getSyntax();
	}

	@Override
	public String getKeyword() {
		return "set";
	}

	@Override
	public String getSyntax() {
		return "set [<edge_id>] <attribute_name> <attribute_value>\n"
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
		if (params.length != 3 && params.length != 4)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"Illegal number of parameters.\n See help for detailed instructions.");
		if (graph == null)
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
					"No graph or node selected to set parameter to.");
		else {
			if (params.length == 3) {
				if (node == null) {
					// postavljanje na selektovan graf propertija
					// odmah pretvoriti u objekat potrebne klase
					graph.setAttribute(params[1], params[2]);
					out.println("Attribute " + params[1] + " set to "
							+ params[2] + " for graph " + graph.getName());
				} else {
					node.setAttribute(params[1], params[2]);
					out.println("Attribute " + params[1] + " set to "
							+ params[2] + " for node " + node.getName());
				}
			} else if (params.length == 4) {
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
				graph.getEdge(id).setAttribute(params[2], params[3]);
				out.println("Attribute " + params[2] + " set to "
						+ params[3] + " for edge " + id);
			}
		}
		return null;
	}

}
