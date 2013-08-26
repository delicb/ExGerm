package tk.exgerm.persistance.parser;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.persistance.PersistanceService;

/**
 * Apstrakcija parsera... Svaki parser treba da implementira ovaj interface radi
 * uniformnog pristupa iz {@link PersistanceService}.
 * 
 * @author Tim 2
 */
public interface Parser {
	/**
	 * Parsira prosleđeni file. File treba da bude prosleđen drugim načinom (u
	 * zavisnosti od konkretnog parsera - ovako se ostavlja podrška za
	 * parsiranje iz raznih izvora).
	 * 
	 * @return {@link IGraph Graph} dobijen parsiranjem.
	 * @throws InternalParseException
	 *             Ukoliko dođe do greške u toku parsiranja u zadatom fajlu.
	 */
	public IGraph parse() throws InternalParseException;
}
