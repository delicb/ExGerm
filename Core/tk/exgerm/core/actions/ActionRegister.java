package tk.exgerm.core.actions;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGAction;

/**
 * Čuva sve instance akcija koje pravi Core i nema neki drugi pametniji posao.
 * 
 * @author Tim 2
 */
public class ActionRegister {
	public static final String showHideStatusbarAction = "showHideStatusbarAction";
	public static final String exitApplication = "exitAplicationAction";
	public static final String showConfiguration = "showConfigurationAction";
	
	private Map<String, ExGAction> actions = new HashMap<String, ExGAction>();

	public ActionRegister() {
		actions.put(showHideStatusbarAction, new ShowHideStatusbarAction());
		actions.put(exitApplication, new ExitAction());
		actions.put(showConfiguration, new ConfigurationAction());
	}

	public ExGAction getAction(String action) {
		return actions.get(action);
	}

}
