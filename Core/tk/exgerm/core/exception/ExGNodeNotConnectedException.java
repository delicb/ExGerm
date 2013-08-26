/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 *
 */
public class ExGNodeNotConnectedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4402540190704458889L;

	/**
	 * 
	 */
	public ExGNodeNotConnectedException() {
	}

	/**
	 * @param message
	 */
	public ExGNodeNotConnectedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ExGNodeNotConnectedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ExGNodeNotConnectedException(String message, Throwable cause) {
		super(message, cause);
	}

}
