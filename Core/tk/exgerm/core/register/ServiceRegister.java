package tk.exgerm.core.register;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import tk.exgerm.core.plugin.IComponent;

/**
 * Prati sve servise koji se pojavljuju u sistemu.
 * 
 */
public class ServiceRegister {

	/**
	 * Kontekst izvršavanja komponente u sklopu OSGI framework-a.
	 */
	protected BundleContext context;

	protected Map<String, ServiceReference> services;

	/**
	 * Proxy za registre grafova.
	 */
	protected GraphRegisterProxy graphRegisterProxy;

	/**
	 * Registar svih komandi
	 */
	protected CommandRegister commandRegister;
	
	/**
	 * Registar svih iteratora
	 */
	protected IteratorRegister iteratorRegister;
	
	/**
	 * Registar svih helpova
	 */
	protected HelpRegister helpRegister;

	/**
	 * Kreira novi ServiceRegiser (samo inicijalizacija)
	 * 
	 * @param context
	 */
	public ServiceRegister(BundleContext context) {
		this.context = context;
		
		this.services = new HashMap<String, ServiceReference>();

		graphRegisterProxy = new GraphRegisterProxy();
		commandRegister = new CommandRegister();
		iteratorRegister = new IteratorRegister();
		helpRegister = new HelpRegister();

		new ServiceTracker(context, IComponent.class.getName(),
				new IComponentActivator(context, this)).open();
	}

	/**
	 * Vraća {@link tk.exgerm.core.plugin.IComponent service}.
	 * 
	 * @param name
	 *            Ime servisa.
	 * @return Service.
	 */
	public IComponent getService(String name) {
		return (IComponent) this.context.getService(services.get(name));
	}

	/**
	 * Dodavanje novog servisa tipa {@link tk.exgerm.core.plugin.IComponent
	 * IComponent}
	 * 
	 * @param name
	 *            ime servisa
	 * @param service
	 *            servis
	 */
	public void addService(String name, ServiceReference service) {
		services.put(name, service);
	}

	/**
	 * Izbacuje servis iz registra
	 * 
	 * @param name
	 *            ime servisa
	 */
	public void removeService(String name) {
		services.remove(name);
	}
	
	public CommandRegister getCommandRegister() {
		return this.commandRegister;
	}

	/**
	 * @return the graphRegisterProxy
	 */
	public GraphRegisterProxy getGraphRegisterProxy() {
		return graphRegisterProxy;
	}

	/**
	 * Vraća help registar
	 * @return help registar
	 */
	public HelpRegister getHelpRegister(){
		return this.helpRegister;
	}
	
	/**
	 * @param graphRegisterProxy
	 *            the graphRegisterProxy to set
	 */
	public void setGraphRegisterProxy(GraphRegisterProxy graphRegisterProxy) {
		this.graphRegisterProxy = graphRegisterProxy;
	}

	public IteratorRegister getIteratorRegister() {
		return this.iteratorRegister;
	}
}
