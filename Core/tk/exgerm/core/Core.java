package tk.exgerm.core;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import tk.exgerm.core.commands.ExitCommand;
import tk.exgerm.core.config.Configuration;
import tk.exgerm.core.event.EventDispatcher;
import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.exception.ExGIteratorAlreadyExsistException;
import tk.exgerm.core.gui.ConfigurationManager;
import tk.exgerm.core.gui.MainWindow;
import tk.exgerm.core.help.CoreHelp;
import tk.exgerm.core.impl.service.BFIterator;
import tk.exgerm.core.impl.service.CoreContext;
import tk.exgerm.core.impl.service.DFIterator;
import tk.exgerm.core.impl.service.DataRegister;
import tk.exgerm.core.impl.service.DefaultGraphFactory;
import tk.exgerm.core.impl.service.DefaultIterator;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.register.ServiceRegister;
import tk.exgerm.core.service.ICoreContext;

/**
 * Glavna klasa u celom projektu. Preko nje se dolazi do svih servisa. Nije
 * vidljiva spolja, služi samo za internu upotrebu unutar Core komponente. <br/>
 * Druge komponete mogu da koriste njene servise preko ICoreContext interfejsa.
 * 
 * @author Tim 2
 */
public class Core {
	/**
	 * Pošto je klasa singleton, ovde se čuva referenca
	 */
	protected static Core instance = null;

	/**
	 * Kontekst Core komponente koji koristi svaka druga komponenta za
	 * komunikaciju sa Core-om.
	 */
	protected ICoreContext coreContext;

	/**
	 * Registar svih servisa koje pružaju komponente
	 */
	protected ServiceRegister serviceRegister;

	/**
	 * Registar podataka koje komponente mogu da razmenjuju.
	 */
	protected DataRegister dataRegister;

	/**
	 * Kontekst u kome se komponenta izvršava unutar OSGI Fremework-a.
	 */
	protected BundleContext context;

	/**
	 * Glavni porozor aplikacije
	 */
	protected MainWindow mainWindow;

	/**
	 * Klasa zadužena za mehanizam događaja.
	 */
	protected EventDispatcher eventDispatcher;

	/**
	 * Komande koje sam Core registruje
	 */
	protected List<ExGCommand> commands;

	/**
	 * Iteratori koje Core sam registruje
	 */
	protected List<ExGIterator> iterators;

	/**
	 * Za čuvanje konfiguracija
	 */
	protected Configuration config;

	/**
	 * Prozor za konfigurisanje aplikacije
	 */
	protected ConfigurationManager configurationManager;

	/**
	 * Podrška za zaustavljanje zatvaranja
	 */
	private boolean preventClosing = false;

	/**
	 * Za registrovanje help-a
	 */
	private CoreHelp coreHelp;

	/**
	 * {@link tk.exgerm.core.service.ICoreContext CoreContext} koji dobijaju sve
	 * komponente za komunikaciju sa Core-om
	 */

	/**
	 * Instancira sve što je potrebno za dalji rad aplikacije. To praktično
	 * znači da če sva polja ove klase biti inicijalizovana.
	 * 
	 * @param context
	 *            Kontekst u kome se komponenta izvršava. Komponenta ga dobija
	 *            od {@link Activator aktivatora}, a on od OSGI-a.
	 */
	private Core(BundleContext context) {
		this.context = context;
		this.setMainWindow(new MainWindow());
		this.setEventDispatcher(new EventDispatcher());
		this.setDataRegister(new DataRegister());
		this.commands = new ArrayList<ExGCommand>();
		this.iterators = new ArrayList<ExGIterator>();
		this.config = new Configuration();
		this.configurationManager = new ConfigurationManager(getMainWindow(),
				true);
		this.setServiceRegister(new ServiceRegister(context));
		this.setCoreContext(this.getCoreContext("Core"));
		
		coreHelp = new CoreHelp();
	}

	/**
	 * Metoda koja prvi put instancira Core. Nije javna, jer jedini ko treba da
	 * joj pristupa je Activator. <br/>
	 * Sve ostale metode treba da pozivaju {@link Core#getInstance()} <br/>
	 * 
	 * @param context
	 *            Kontekst koji prima {@link Activator aktivator} od OSGI-a.
	 * @return Upravo napravljenu instancu klase {@link Core}.
	 */
	static Core createCore(BundleContext context) {
		if (instance == null) {
			instance = new Core(context);
		}
		return instance;
	}

	/**
	 * Klijenti treba da koriste ovu metodu za dobijanje instance klase
	 * {@link Core}.
	 * 
	 * @return Instancu klase {@link Core}
	 */
	public static Core getInstance() {
		return instance;
	}

	/**
	 * Nacin da se Core obavesti da se pokrene.
	 */
	public void start() {
		registerCommands();
		registerIterators();
		registerHelp();
		getMainWindow().setVisible(true);
	}

	/**
	 * Način da se Core obavesti da treba da se zaustavi.
	 */
	public void stop() {
		eventDispatcher.raiseEvent(IComponent.APPLICATION_CLOSING);
		if (!preventClosing) {
			getMainWindow().setVisible(false);
			unregisterCommands();
			unregisterIterators();
			unregisterHelp();
		}
	}

	/**
	 * Način da MainWindow Core-u da korisnik želi da zatvori aplikaciju.
	 * 
	 * Metoda zaustalja bundle Core i glavni OSGI bunde (sa ID-om 0).
	 */
	public void close() {
		try {
			this.preventClosing = false;
			this.stop();
			if (!preventClosing) {
				config.save();
				context.getBundle(0).stop();
				System.exit(0);
			}
		} catch (BundleException e) {
			System.err.println("Error during closeing...");
			e.printStackTrace();
		}
	}

	/**
	 * @return the coreContext
	 */
	public ICoreContext getCoreContext(String name) {
		return new CoreContext(name, this, new DefaultGraphFactory());
	}

	/**
	 * @param serviceRegister
	 *            the serviceRegister to set
	 */
	public void setServiceRegister(ServiceRegister serviceRegister) {
		this.serviceRegister = serviceRegister;
	}

	/**
	 * @return the serviceRegister
	 */
	public ServiceRegister getServiceRegister() {
		return serviceRegister;
	}

	/**
	 * @param mainWindow
	 *            the mainWindow to set
	 */
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * @return the mainWindow
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * @param eventDispatcher
	 *            the eventDispatcher to set
	 */
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * @return the eventDispatcher
	 */
	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	/**
	 * @param coreContext
	 *            the coreContext to set
	 */
	public void setCoreContext(ICoreContext coreContext) {
		this.coreContext = coreContext;
	}

	/**
	 * @return the dataRegister
	 */
	public DataRegister getDataRegister() {
		return dataRegister;
	}

	/**
	 * @param dataRegister
	 *            the dataRegister to set
	 */
	public void setDataRegister(DataRegister dataRegister) {
		this.dataRegister = dataRegister;
	}

	public Configuration getConfig() {
		return this.config;
	}

	public ConfigurationManager getConfigurationManager() {
		return this.configurationManager;
	}

	public void preventClosing() {
		this.preventClosing = true;
	}

	private void registerCommands() {
		registerCommand(new ExitCommand());
	}

	private void registerCommand(ExGCommand command) {
		this.commands.add(command);
		try {
			this.coreContext.registerCommand(command);
		} catch (ExGCommandAlreadyExistException e) {
			System.err.println(e.getMessage());
		}
	}

	private void unregisterCommands() {
		for (ExGCommand command : commands) {
			this.coreContext.removeCommand(command);
		}
	}

	private void registerIterators() {
		registerIterator(new BFIterator());
		registerIterator(new DFIterator());
		registerIterator(new DefaultIterator());
	}

	private void registerIterator(ExGIterator iterator) {
		iterators.add(iterator);
		try {
			coreContext.registerIterator(iterator);
		} catch (ExGIteratorAlreadyExsistException e) {
			System.err.println(e.getMessage());
		}
	}

	private void unregisterIterators() {
		for (ExGIterator it : iterators) {
			coreContext.removeIterator(it.getName());
		}
	}

	private void registerHelp() {
		coreContext.registerHelp(coreHelp);
	}

	private void unregisterHelp() {
		coreContext.removeHelp(coreHelp);
	}

}
