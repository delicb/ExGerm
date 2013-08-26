package tk.exgerm.graphstatis.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphstatis.StatisticTool;
import tk.exgerm.graphstatis.statisticTools.StatsToolsRegister;

public class StatsCommand implements ExGCommand {

	private StatsToolsRegister register;
	private ICoreContext context;

	public StatsCommand(StatsToolsRegister register, ICoreContext context) {
		this.register = register;
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		IGraph g = getGraph(params);
		if (g == null)
			error("Stat what?");
		else {
			String tool = getValue("-t", params);
			if (tool == null) {
				for (StatisticTool t : register.getAllTools()) {
					executeTool(t, g, out);
				}
			} else {
				StatisticTool t = register.getTool(tool);
				if (t != null)
					executeTool(t, g, out);
				else
					error("Unknown tool... See help -v stat.");
			}
		}

		return null;
	}

	@Override
	public String getHelp() {
		StringBuffer help = new StringBuffer();
		help
				.append("stats - Graph analysis. If graph is not provided, active will\n"
						+ "be used. If tool is not priveded, all will be started.\n\n");
		for (StatisticTool tool : register.getAllTools()) {
			help.append("\t" + tool.getName() + "\n");
			help.append("\t\t" + tool.getDescription() + "\n");
		}
		return new String(help);
	}

	@Override
	public String getKeyword() {
		return "stat";
	}

	@Override
	public String getSyntax() {
		return "stat [graph] [-t tool]";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

	private void executeTool(StatisticTool tool, IGraph g, PrintStream out) {
		tool.setGraph(g);
		out.print(tool.getResult());
	}

	private IGraph getGraph(String... params) {
		IGraph g = null;
		String graphName = null;
		// ako se graf zadaje, mora biti prvi parametar
		try {

			if (!params[1].startsWith("-"))
				graphName = params[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			// samo ostavljamo graphName na null;
		}

		if (graphName == null) {
			g = getActiveGraph();
		} else {
			g = context.getGraph(graphName);
		}

		return g;
	}

	private IGraph getActiveGraph() {
		INode n = (INode) context.getData(CONSOLE_ACTIVE_NODE);
		if (n != null) {
			if (n instanceof IGraph)
				return (IGraph) n;
			else
				return n.getGraph();
		} else {
			return (IGraph) context.getData(CONSOLE_ACTIVE_GRAPH);
		}
	}

	protected String getValue(String sw, String... params) {
		String res = null;
		for (int i = 0; i < params.length; i++) {
			if (params[i].equalsIgnoreCase(sw)) {
				try {
					res = params[i + 1];
				} catch (ArrayIndexOutOfBoundsException e) {
					// samo ostavimo res na null
				}
				break;
			}
		}
		return res;
	}

	protected void error(String message) throws ExGCommandErrorException {
		throw new ExGCommandErrorException(
				ExGCommandErrorException.CommandErrorType.ERROR, message);
	}

}
