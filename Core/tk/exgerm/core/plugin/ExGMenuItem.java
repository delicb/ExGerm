package tk.exgerm.core.plugin;

/**
 * Ovaj iterface obuhvata zajedničke stavke koje mogu da se pojave u bilo kojoj
 * stavci u meniju. U ovom slučaju nasleđuju ga samo {@link ExGAction} i
 * {@link ExGSubmenu}. Ni jedna komponenta ne bi trebalo direktno da
 * implementira ovaj interface, neko nekog o njegovih naslednika...
 * 
 * @author Tim 2
 */
public interface ExGMenuItem {
	

	/**
	 * Oznaka File menija
	 */
	public static final String FILE_MENU = "__FILE_MENU__";

	/**
	 * Oznaka Edit menija
	 */
	public static final String EDIT_MENU = "__EDIT_MENU__";

	/**
	 * Oznaka View menija
	 */
	public static final String VIEW_MENU = "__VIEW_MENU__";

	/**
	 * Oznaka Tools menija
	 */
	public static final String TOOLS_MENU = "__TOOLS_MENU__";

	/**
	 * Oznaka Plugins menija
	 */
	public static final String PLUGINS_MENU = "__PLUGINS_MENU__";

	/**
	 * Oznaka Help menija
	 */
	public static final String HELP_MENU = "__HELP_MENU__";
	
	/**
	 * Metoda koja treba da vrati string sa imenom menija u koji akcija treba da
	 * se ugradi. Ako takav meni ne postoji biće kreiran. Metoda će biti pozvana
	 * samo ako {@link ExGAction#getActionPosition()} označi akciju kao da treba
	 * da bude dodata u meni, u suprotnom neće nikad biti pozvana. Ako se akcija
	 * dodaje u neki od standardnih menija treba iskoristiti neku od konstanti
	 * definisanih u ovom interface-u.
	 * 
	 * @return Ime menija u koji treba da se ugradi akcija.
	 */
	public abstract String getMenu();
	
	/**
	 * <p>
	 * Vraća poziciju na kojoj će se akcija ubaciti u meni. Ovo treba da bude
	 * velik ceo broj, sa dosta razmaka između rednih brojeva akcija, da bi se
	 * ostavio prostor drugim komponentama da se uglave između ove akcije i
	 * prehodne ili sledeće.
	 * <p>
	 * Akcije sa istim rednim brojem će biti grupisane između dva separatora.
	 * <p>
	 * Metoda neće biti pozvana ako se ova akcija ne ugrađuje u meni.
	 * 
	 * @return Redni broj pozicije u meniju.
	 */
	public abstract int getMenuPostition();
}
