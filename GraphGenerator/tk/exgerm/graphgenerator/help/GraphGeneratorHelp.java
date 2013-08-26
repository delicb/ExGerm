package tk.exgerm.graphgenerator.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.graphgenerator.GraphGenerator;

public class GraphGeneratorHelp implements ExGHelp {

	/**
	 * Lista help stranica ove komponente
	 */
	private Map<String, String> help;

	public GraphGeneratorHelp() {
		help = new HashMap<String, String>();
		help.put(GraphGenerator.componentName, getClass().getResource("GraphGeneratorHelp.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
