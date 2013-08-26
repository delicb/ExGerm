/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGNodeAlreadyExsistException extends ExGNameConflictException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3329495827439227607L;

	public ExGNodeAlreadyExsistException() {
		super();
	}

	public ExGNodeAlreadyExsistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExGNodeAlreadyExsistException(String message) {
		super(message);
	}

	public ExGNodeAlreadyExsistException(Throwable cause) {
		super(cause);
	}

}
