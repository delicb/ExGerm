/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGNotImplementedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -184927819200347117L;

	/**
	 * 
	 */
	public ExGNotImplementedException() {
	}

	/**
	 * @param message
	 */
	public ExGNotImplementedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExGNotImplementedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExGNotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}
