/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGRuntimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7730973036444962067L;

	public ExGRuntimeException() {
		super();
	}

	public ExGRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExGRuntimeException(String message) {
		super(message);
	}

	public ExGRuntimeException(Throwable cause) {
		super(cause);
	}

}
