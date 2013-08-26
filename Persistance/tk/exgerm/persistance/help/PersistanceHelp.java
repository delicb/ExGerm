package tk.exgerm.persistance.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class PersistanceHelp implements ExGHelp {
	
	private Map<String, String> help;
	
	public PersistanceHelp() {
		help = new HashMap<String, String>();
		
		help.put("Grafički interfejs", getClass().getResource("persistance_gui.htm").toString());
		help.put("Konzola", getClass().getResource("persistance_commands.htm").toString());
		help.put("GDL", getClass().getResource("persistance_gdl.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
