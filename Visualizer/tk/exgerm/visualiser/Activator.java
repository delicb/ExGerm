package tk.exgerm.visualiser;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {
	
	public static VisualiserService visService;
	public static String name = "Visualiser";
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(IComponent.NAME_PROPERTY, name);
		if(visService == null)
			visService = new VisualiserService(context);
		context.registerService(IComponent.class.getName(),visService, params);
		visService.turnOn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		visService.turnOff();
	}

}
