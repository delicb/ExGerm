package tk.exgerm.core.register;

import java.util.HashMap;
import java.util.Map;

import tk.exgerm.core.plugin.ExGHelp;

public class HelpRegister {

	/**
	 * {@link java.util.Map Mapa} svih helpova koje postoje u sistemu.
	 * 
	 */
	private Map<String, ExGHelp> helps = new HashMap<String, ExGHelp>();
	
	/**
	 * Dodaje novi help u registar
	 * @param component koja registruje help
	 * @param help komponente
	 */
	public void addHelp(String component, ExGHelp help) {
		helps.put(component, help);
	}

	/**
	 * Uklanja help iz registra
	 * 
	 * @param help
	 *            Komanda koja se ukljanja
	 */
	public void removeHelp(ExGHelp component) {
		helps.remove(component);
	}

	/**
	 * Vraća help
	 * 
	 * @param component
	 *            Naziv komponente
	 * @return help komponente
	 */
	public ExGHelp getHelp(String component) {
		return helps.get(component);
	}

	/**
	 * Vraća sve helpove koje su registrovane u sistemu.
	 * 
	 * @return Svi registrovani helpovi
	 */
	public Map<String, ExGHelp> getAllHelp() {
		return helps;
	}
}
