package tk.exgerm.mp3player;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	private Mp3PlayerService service;
	
	public void start(BundleContext context) throws Exception {
		if (service == null)
			service = new Mp3PlayerService();
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(IComponent.NAME_PROPERTY, "Mp3 player");
		context.registerService(IComponent.class.getName(), service, params);
	}

	public void stop(BundleContext context) throws Exception {
		service.stop();
	}

}
