package tk.exgerm.help;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {
	
	public static HelpService helpService;
	
	/*
	 * OSGI context u okviru koga se pokrece ConsoleService
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(IComponent.NAME_PROPERTY, "Help");
		if (helpService == null)
			helpService = new HelpService(context);
		context.registerService(IComponent.class.getName(), helpService,
				params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		helpService.shutDown();
	}

}
