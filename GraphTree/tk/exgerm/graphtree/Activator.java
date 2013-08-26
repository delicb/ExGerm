package tk.exgerm.graphtree;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	GraphTreeService graphTreeService;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(IComponent.NAME_PROPERTY, "GraphTree");
		graphTreeService = new GraphTreeService(context);
		context.registerService(IComponent.class.getName(),
				graphTreeService, params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		graphTreeService.shutDown();
	}

}
