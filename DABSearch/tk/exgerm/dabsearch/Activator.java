package tk.exgerm.dabsearch;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	public static DABSService dabsservice;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		
		
		params.put(IComponent.NAME_PROPERTY, "DABSearch");
		if(dabsservice == null){
			dabsservice = new DABSService(context);
		}
		context.registerService(IComponent.class.getName(),
				dabsservice , params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		dabsservice.shutDown();
	}

}
