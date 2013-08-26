package tk.exgerm.mp3player.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class Mp3Help implements ExGHelp {
	
	private Map<String, String> help;

	public Mp3Help() {
		help = new HashMap<String, String>();
		help.put("Life's good", getClass().getResource("mp3_player.htm").toString());
	}
	
	@Override
	public Map<String, String> getHelpMap() {
		return help;
	}

}
