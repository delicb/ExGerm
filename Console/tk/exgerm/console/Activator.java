package tk.exgerm.console;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import tk.exgerm.console.gui.Console;
import tk.exgerm.core.plugin.IComponent;

public class Activator implements BundleActivator {

	public static ConsoleService consoleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		Console.componentName = "Console";
		params.put(IComponent.NAME_PROPERTY, "Console");
		if (consoleService == null)
			consoleService = new ConsoleService(context);
		context.registerService(IComponent.class.getName(), consoleService,
				params);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		consoleService.shutDown();
	}

}
