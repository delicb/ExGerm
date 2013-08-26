package tk.exgerm.graphtree.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class GraphTreeHelp implements ExGHelp {

	private Map<String, String> help;

	public GraphTreeHelp() {
		help = new HashMap<String, String>();
		help.put("GraphTree", getClass().getResource("GraphTreeHelp.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
