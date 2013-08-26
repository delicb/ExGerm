package tk.exgerm.graphstatis.statisticTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.exgerm.graphstatis.StatisticTool;

/**
 * Čuva sve alate za testiranje.
 * 
 * @author Tim 2
 */
public class StatsToolsRegister {
	private Map<String, StatisticTool> tools = new HashMap<String, StatisticTool>();
	
	public StatsToolsRegister() {
		addTool(new NodeCount());
		addTool(new EdgeCount());
		addTool(new SubgraphCount());
		addTool(new GraphDepth());
	}
	
	public void addTool(StatisticTool tool) {
		tools.put(tool.getName(), tool);
	}
	
	public StatisticTool getTool(String name) {
		return tools.get(name);
	}
	
	public List<StatisticTool> getAllTools() {
		return new ArrayList<StatisticTool>(tools.values());
	}
}
