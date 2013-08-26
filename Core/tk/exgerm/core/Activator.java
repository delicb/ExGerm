package tk.exgerm.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * <p>
 * Klasa koju poziva OSGI framework prilikom pokretanja komponente. 
 * <p>
 * Ne radi ništa pamento, samo instancira {@link Core} koji obavlja 
 * sve ostalo.
 * 
 * @author Tim 2
 */
public class Activator implements BundleActivator {

	/**
	 * <p>
	 * Ova metoda biva pozvana kada se pokreće komponenta.
	 * <p>
	 * Samo instancira Core i prepušta mu sve ostalo.
	 */
	public void start(BundleContext context) throws Exception {
		// prvo moramo da kreiramo Core, jer od njega zavise servisi
		// koji rade sa ostalim komponentama
		Core.createCore(context).start();
	}

	/**
	 * <p>
	 * Ova metoda biva pozvana kad se zaustavlja komponenta. 
	 * <p>
	 * TODO: Dodati javadoc ukoliko se neki kod ovde doda. 
	 */
	public void stop(BundleContext context) throws Exception {
		Core.getInstance().stop();
	}

}
