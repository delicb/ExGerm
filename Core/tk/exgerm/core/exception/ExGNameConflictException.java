package tk.exgerm.core.exception;

public class ExGNameConflictException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8772409372786689888L;

	public ExGNameConflictException() {
	}

	public ExGNameConflictException(String message) {
		super(message);
	}

	public ExGNameConflictException(Throwable cause) {
		super(cause);
	}

	public ExGNameConflictException(String message, Throwable cause) {
		super(message, cause);
	}

}
