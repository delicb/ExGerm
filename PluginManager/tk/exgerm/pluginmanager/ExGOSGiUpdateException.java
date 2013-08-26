package tk.exgerm.pluginmanager;

import org.osgi.framework.Bundle;

/**
 * Klasa koja predstavlja poseban exception, skup svih mogućih grešaka
 * koje se mogu desiti tokom instalacije ili update-a plugina.
 */
@SuppressWarnings("serial")
public class ExGOSGiUpdateException extends Exception {

	private ExGOSGiUpdateException.ErrorType type;
	private Bundle bundle;

	/**
	 * Jedini konstruktor exceptiona kada se vrši update bundla.
	 * 
	 * @param bundle
	 *            koji se updatuje
	 * @param type
	 *            tip greške
	 * @param message
	 *            poruka greške
	 */
	public ExGOSGiUpdateException(Bundle bundle,
			ExGOSGiUpdateException.ErrorType type, String message) {
		super(message);
		this.type = type;
		this.bundle = bundle;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	public ExGOSGiUpdateException.ErrorType getType() {
		return type;
	}

	public Bundle getBundle() {
		return bundle;
	}

	/**
	 * Tipovi ishoda update-a.
	 */
	public static enum ErrorType {
		IO_ERROR, NO_REPOSITORY, CONNECTION_ERROR, BAD_URL, BUNDLE_ERROR, ALLREADY_UPTODATE
	}

}
