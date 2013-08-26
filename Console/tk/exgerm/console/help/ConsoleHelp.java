package tk.exgerm.console.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class ConsoleHelp implements ExGHelp{
	
	private Map<String, String> help;

	public ConsoleHelp() {
		help = new HashMap<String, String>();
		help.put("Konzola", getClass().getResource("main.htm").toString());
		help.put("Komande konzole", getClass().getResource("commands.htm").toString());
	}

	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
