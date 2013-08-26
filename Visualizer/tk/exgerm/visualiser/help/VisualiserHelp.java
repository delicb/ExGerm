package tk.exgerm.visualiser.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class VisualiserHelp implements ExGHelp {
	
	private Map<String, String> help;

	public VisualiserHelp() {
		help = new HashMap<String, String>();
		help.put("Visualiser", getClass().getResource("VisualizerHelp.htm").toString());
		help.put("Navigator", getClass().getResource("Navigator.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
