/**
 * 
 */
package tk.exgerm.persistance.parser;

/**
 * @author del-boy
 *
 */
public class InternalParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3276117237572912817L;

	/**
	 * 
	 */
	public InternalParseException() {
	}

	/**
	 * @param message
	 */
	public InternalParseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InternalParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InternalParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
