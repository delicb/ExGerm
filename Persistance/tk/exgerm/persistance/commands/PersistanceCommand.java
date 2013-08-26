package tk.exgerm.persistance.commands;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.plugin.ExGCommand;

/**
 * Sloj između {@link ExGCommand} i konkretnih komandi. Implementira neke često
 * korišćene stvari u svim komanama.
 * 
 * @author Tim 2
 */
public abstract class PersistanceCommand implements ExGCommand {

	/**
	 * Baca exception {@link ExGCommandErrorException} sa parametor koji
	 * označava da je <em>ERROR</em> tip izuzetka.
	 * 
	 * @param message
	 *            Poruka koja se prosleđuje
	 * @throws ExGCommandErrorException
	 */
	protected void error(String message) throws ExGCommandErrorException {
		throw new ExGCommandErrorException(
				ExGCommandErrorException.CommandErrorType.ERROR, message);
	}

	/**
	 * Baca exception {@link ExGCommandErrorException} sa parametor koji
	 * označava da je <em>WARNING</em> tip izuzetka.
	 * 
	 * @param message
	 *            Poruka koja se prosleđuje
	 * @throws ExGCommandErrorException
	 */
	protected void warning(String message) throws ExGCommandErrorException {
		throw new ExGCommandErrorException(
				ExGCommandErrorException.CommandErrorType.WARNING, message);
	}

	/**
	 * Baca exception {@link ExGCommandErrorException} sa parametor koji
	 * označava da je <em>CRITICAL_ERROR</em> tip izuzetka.
	 * 
	 * @param message
	 *            Poruka koja se prosleđuje
	 * @throws ExGCommandErrorException
	 */
	protected void critical_error(String message)
			throws ExGCommandErrorException {
		throw new ExGCommandErrorException(
				ExGCommandErrorException.CommandErrorType.CRITICAL_ERROR,
				message);
	}

	protected String getValue(String sw, String... params) {
		String res = null;
		for (int i = 0; i < params.length; i++) {
			if (params[i].equalsIgnoreCase(sw)) {
				try {
					res = params[i + 1];
				} catch (ArrayIndexOutOfBoundsException e) {
					// samo ostavimo res na null
				}
				break;
			}
		}
		return res;
	}
	
	protected boolean hasSwitch(String sw, String... params) {
		for (int i = 0; i < params.length; i++) {
			if (sw.equalsIgnoreCase(params[i]))
				return true;
		}
		return false;
	}

}
