package tk.exgerm.core.service;

import tk.exgerm.core.exception.ExGUnknownCommandException;
import tk.exgerm.core.plugin.ExGCommand;

/**
 * Koristi se za kreiranje uvezanih komandi. Klijent dobija pipeline koristeći
 * {@link ICoreContext kontekst}
 * 
 * @author Tim 2
 */
public interface IPipeLine {
	/**
	 * Dodaje komandu u pipeline. Komanda mora biti registrovana u
	 * {@link tk.exgerm.core.register.CommandRegister registru komandi}.
	 * 
	 * @param command
	 *            Komdanda koja se dodaje
	 * @param params
	 *            Dodatni parametri komande
	 * 
	 * @throws ExGUnknownCommandException
	 *             Ukoliko prosleđena komanda ne postoji.
	 */
	public void addCommand(ExGCommand command)
			throws ExGUnknownCommandException;

	/**
	 * Pokreće sve komande koje se dodate u pipeline metodom
	 * {@link IPipeLine#addCommand(ExGCommand command)}
	 */
	public void run();

	/**
	 * Vraća rezultat izvršavanja poslednje komande u nizu.
	 * 
	 * @return Rezultat izvršavanja pipeline-a
	 */
	public Object getResult();
}
