package tk.exgerm.eventtracker;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	EventTrackerService service;

	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(IComponent.NAME_PROPERTY, "EventTracker");
		service = new EventTrackerService(context);
		context.registerService(IComponent.class.getName(), service, params);
	}

	public void stop(BundleContext context) throws Exception {
		service.stop();
	}

}
