/**
 * 
 */
package tk.exgerm.core.exception;

/**
 * @author Tim 2
 * 
 */
public class ExGCommandErrorException extends Exception {

	CommandErrorType errorType;
	String message;

	public enum CommandErrorType {
		WARNING, ERROR, CRITICAL_ERROR
	}

	private static final long serialVersionUID = 3653809294835625761L;

	public ExGCommandErrorException(CommandErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
		this.message = message;
	}
	
	/**
	 * Konstruktor sa default ErrorType-om (CommandErrorType.ERROR)
	 * @param message poruka greške
	 */
	public ExGCommandErrorException(String message){
		super(message);
		this.errorType = CommandErrorType.ERROR;
		this.message = message;
	}
	
	public CommandErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(CommandErrorType errorType) {
		this.errorType = errorType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
