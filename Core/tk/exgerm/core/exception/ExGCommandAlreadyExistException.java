/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGCommandAlreadyExistException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 303552505473015145L;

	public ExGCommandAlreadyExistException() {
		super();
	}

	public ExGCommandAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExGCommandAlreadyExistException(String message) {
		super(message);
	}

	public ExGCommandAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
