package tk.exgerm.ucsearch;

import java.io.PrintStream;
import java.util.ArrayList;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class UCSCommand implements ExGCommand {

	ICoreContext context;

	public UCSCommand(ICoreContext context) {
		super();
		this.context = context;
	}
	
	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		
		if (params.length != 3 && params.length != 5)
			throw new ExGCommandErrorException(CommandErrorType.ERROR,
					"Illegal number of arguments!");
		
		IGraph graph = (IGraph) context
				.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);

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
		
		String attribute = "";
		if(params.length == 5){
			if(((String)params[3]).equals("-a")){
				attribute = (String)params[4];
			}				
		}

		UCSearch search = new UCSearch(attribute, start, finish);
		ArrayList<Object> result;
		try {
			result = search.search();
		} catch (Exception e) {
			throw new ExGCommandErrorException(CommandErrorType.ERROR,
					"Node not found!");
		}
		out.println();
		if(result == null){
			throw new ExGCommandErrorException(CommandErrorType.WARNING,
			"Success not possible! Nodes do not have direct connection or all the connections are directed from finish to start.");
		}
		for (Object node : result){
			if(node instanceof INode)
				out.print(((INode)node).getName() + " ");
			if(node instanceof IEdge)
				out.print(((IEdge)node).toString() + " ");	
		}
		out.println();
		return null;
	}

	@Override
	public String getHelp() {
		return "Searches for the path from <start_node> to <finish_node> using <attribute> as weight.";
	}

	@Override
	public String getKeyword() {
		return "ucs";
	}

	@Override
	public String getSyntax() {
		return "Use ucs <start_node> <finish_node>";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
