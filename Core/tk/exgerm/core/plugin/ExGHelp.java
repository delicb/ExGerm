package tk.exgerm.core.plugin;

import java.util.Map;

/**
 * <p>
 * Interfejs propisuje metode koje treba da implementiraju Helpovi svih
 * komponenti.
 *
 */
public interface ExGHelp {
	
	/**
	 * Označava događaj da je zatražen help
	 * 
	 * Parametri:
	 * <ol>
	 * <li>ime komponente: String - ime komponente koja trazi help</li>
	 * <li>stranica helpa: String - stranica koju je potrebno prikazati</li>
	 * </ol>
	 */
	public static final String HELP_REQUESTED = "help.help_requested";
	
	/**
	 * Vraća URL sa putanjom do Helpa komponente koja implementira interfejs 
	 * @return URL
	 */
	public Map<String,String> getHelpMap();

}
