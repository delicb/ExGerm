package tk.exgerm.dabsearch.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class DABHelp implements ExGHelp {

	private Map<String, String> help;
	
	
	public DABHelp() {
		help = new HashMap<String, String>();
		help.put("Slepe pretrage u širinu i dubinu", getClass().getResource("dbs.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
