package tk.exgerm.graphstatis;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	private GraphStatsService service;
	
	public void start(BundleContext context) throws Exception {
		if (service == null) 
			service = new GraphStatsService();
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(IComponent.NAME_PROPERTY, "GraphStats");
		context.registerService(IComponent.class.getName(), service, params);
	
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		service.stop();
	}

}
