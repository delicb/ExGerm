package tk.exgerm.graphstats.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class GraphStatsHelp implements ExGHelp {

	private Map<String, String> help;

	public GraphStatsHelp() {
		help = new HashMap<String, String>();
		help.put("GraphStats", getClass().getResource("GraphStatsHelp.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}
}
