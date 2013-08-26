package tk.exgerm.pluginmanager.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

/**
 * Help sistem PM-a
 */
public class PluginManagerHelp implements ExGHelp {

	private Map<String, String> help;

	public PluginManagerHelp() {
		help = new HashMap<String, String>();
		help.put("Početna stranica", getClass().getResource("index.html")
				.toString());
		help.put("Komande", getClass().getResource("komande.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
