package tk.exgerm.systemtray;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	private static SystemTrayService systemTrayService;

	public void start(BundleContext context) throws Exception {
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(IComponent.NAME_PROPERTY, "SystemTray");
		if (systemTrayService == null)
			systemTrayService = new SystemTrayService(context);
		context.registerService(IComponent.class.getName(), systemTrayService,
				params);
		systemTrayService.turnOn();
	}

	public void stop(BundleContext context) throws Exception {
		systemTrayService.turnOff();
	}
}
