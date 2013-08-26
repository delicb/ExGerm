package tk.exgerm.dabsearch.util;

import java.io.PrintStream;
import java.util.ArrayList;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.dabsearch.Search;

public class BFSCommand implements ExGCommand {

	ICoreContext context;

	public BFSCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length != 3)
			throw new ExGCommandErrorException(CommandErrorType.ERROR,
					"Illegal number of arguments!");
		IGraph graph = null;
		if (context.getData(ExGCommand.CONSOLE_ACTIVE_NODE) instanceof IGraph)
			graph = (IGraph) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);
		else
			graph = (IGraph) context.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);


		if (graph == null)
			throw new ExGCommandErrorException(CommandErrorType.ERROR,
					"No graph selected!");

		INode start = graph.getNode(params[1]);
		INode finish = graph.getNode(params[2]);

		if (finish == null)
			throw new ExGCommandErrorException(CommandErrorType.ERROR, "Node "
					+ params[2] + " not found!");
		if (start == null)
			throw new ExGCommandErrorException(CommandErrorType.ERROR, "Node "
					+ params[1] + " not found!");

		Search search = new Search(graph, start, finish);
		ArrayList<INode> result;
		try {
			result = search.search(ExGIterator.BREADTH_FIRST);
		} catch (ExGNodeDoesNotExsistException e) {
			throw new ExGCommandErrorException(CommandErrorType.ERROR,
					"Node not found!");
		}
		out.println();
		if(result == null){
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
			"Success not possible! Nodes do not have direct connection or all the connections are directed from finish to start.");
		}
		while(result.remove(null));
		for (INode node : result)
			out.print(node.getName() + " ");
		out.println();
		return null;
	}

	@Override
	public String getHelp() {
		return "Searches for the path from <start_node> to <finish_node> from the selected graph.";
	}

	@Override
	public String getKeyword() {
		return "bfs";
	}

	@Override
	public String getSyntax() {
		return "Use bfs <start_node> <finish_node>";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
