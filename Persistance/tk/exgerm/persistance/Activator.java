package tk.exgerm.persistance;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;

/**
 * Aktivira naš voljeni Persistance... 
 * 
 * @author Tim 2
 */
public class Activator implements BundleActivator {

	private PersistanceService persistanceService;

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(IComponent.NAME_PROPERTY, "Persistance");
		persistanceService = new PersistanceService();
		context.registerService(IComponent.class.getName(),
				persistanceService, params);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		persistanceService.stop();
	}

}
