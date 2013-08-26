package tk.exgerm.graphstatis.statisticTools;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.graphstatis.StatisticTool;

public class NodeCount implements StatisticTool {

	private IGraph g;
	
	@Override
	public String getDescription() {
		return "Calculates and counts node count";
	}

	@Override
	public String getName() {
		return "nodes";
	}

	@Override
	public String getResult() {
		return "Nodes: " + g.getNodeCount();
	}

	@Override
	public void setGraph(IGraph graph) {
		this.g = graph;
	}

}
