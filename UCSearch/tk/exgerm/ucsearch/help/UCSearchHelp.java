package tk.exgerm.ucsearch.help;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class UCSearchHelp implements ExGHelp {

		private Map<String, String> help;

		public UCSearchHelp() {
			help = new HashMap<String, String>();
			help.put("UCSearch", getClass().getResource("UCSearchHelp.htm").toString());
		}

		@Override
		public Map<String, String> getHelpMap() {
			return help;
		}

}
