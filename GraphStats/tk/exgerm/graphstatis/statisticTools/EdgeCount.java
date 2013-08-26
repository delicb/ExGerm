package tk.exgerm.graphstatis.statisticTools;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.graphstatis.StatisticTool;

public class EdgeCount implements StatisticTool {

	private IGraph g;
	
	@Override
	public String getDescription() {
		return "Calculates the number of edges in the main graph";
	}

	@Override
	public String getName() {
		return "edges";
	}

	@Override
	public String getResult() {
		return "Edges: " + g.getEdgeCount();
	}

	@Override
	public void setGraph(IGraph graph) {
		this.g = graph;
	}

}
