package tk.exgerm.core.register;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import tk.exgerm.core.Core;
import tk.exgerm.core.plugin.IComponent;

public class IComponentActivator implements ServiceTrackerCustomizer {

	/**
	 * Kontekst u kome se izvršava komponenta u okviru OSGI framework-a.
	 */
	protected BundleContext context;

	protected ServiceRegister register;

	public IComponentActivator(BundleContext context, ServiceRegister register) {
		this.context = context;
		this.register = register;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		String name = (String) reference.getProperty(IComponent.NAME_PROPERTY);
		if (name == null) {
			System.err.println("Pokusaj registrovanja serivsa bez imena");
			return null;
		}
		IComponent c = (IComponent) context.getService(reference);
		register.addService(name, reference);
		c.setContext(Core.getInstance().getCoreContext(name));
		return null;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		String name = (String) reference.getProperty(IComponent.NAME_PROPERTY);
		register.removeService(name);
	}

}
