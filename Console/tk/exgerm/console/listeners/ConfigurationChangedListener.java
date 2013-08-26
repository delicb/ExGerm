package tk.exgerm.console.listeners;

import tk.exgerm.console.gui.Console;
import tk.exgerm.core.plugin.IListener;

public class ConfigurationChangedListener implements IListener{
	
	private Console console;

	public ConfigurationChangedListener(Console console) {
		super();
		this.console = console;
	}

	@Override
	public void raise(String event, Object... parameters) {
		console.updateConfiguration();
	}

}
