package tk.exgerm.core.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class CoreHelp implements ExGHelp {
	
	private Map<String, String> help;

	public CoreHelp() {
		help = new HashMap<String, String>();
		help.put("Podesavanja", getClass().getResource("Podesavanja.htm").toString());
		help.put("Radna povrsina", getClass().getResource("RadnaPovrsina.htm").toString());
		help.put("Uvod", getClass().getResource("Uvod.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
