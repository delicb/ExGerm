/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGGraphExsistException extends ExGNameConflictException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225009114648265087L;

	public ExGGraphExsistException() {
		super();
	}

	public ExGGraphExsistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExGGraphExsistException(String message) {
		super(message);
	}

	public ExGGraphExsistException(Throwable cause) {
		super(cause);
	}

}
