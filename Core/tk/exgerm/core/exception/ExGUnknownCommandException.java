/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGUnknownCommandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 303552505473015145L;

	public ExGUnknownCommandException() {
		super();
	}

	public ExGUnknownCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExGUnknownCommandException(String message) {
		super(message);
	}

	public ExGUnknownCommandException(Throwable cause) {
		super(cause);
	}

}
