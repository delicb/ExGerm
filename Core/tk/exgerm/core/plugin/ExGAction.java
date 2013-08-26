package tk.exgerm.core.plugin;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Apstraktna klasa koja služi za definisanje akcija svake metode.
 * 
 * Instance ove klase suže da se dodaju u menije, toolbar, kontekstne menije...
 * 
 * Što se tiče same akcije, sve metode koje konkretne akcije preklapaju su
 * definisane u {@link javax.swing.AbstractAction}. Ovde se samo definiše gde
 * akcija treba da se ugradi u glavni prozor.
 * 
 * @author Tim 2
 * 
 */
public abstract class ExGAction extends AbstractAction implements ExGMenuItem {

	private static final long serialVersionUID = -7932665974746597654L;

	/**
	 * <p>
	 * Konstante koje određuju poziciju akcije u sklopu aplikacije. Jedna akcija
	 * može da se pojavi na više mesta.
	 * <p>
	 * Konstante su tako napravljene da sve informacije mogu da se prenesu
	 * jednim integer-om. Na primer, ako akcija želi da bude u meniju i
	 * toolbar-u, treba da vrati integer MENU | TOOLBAR.
	 */

	/**
	 * Oznaka da akcija treba da se ugradi u toolbar. Ugradiće se u
	 * podrazumevani toolbar. Ako komponenta
	 */
	public static final int TOOLBAR = 0x1;

	/**
	 * Oznaka da komponenta treba da se ugradi u meni. U koji meni se komponenta
	 * ugrađuje definiše se string-om.
	 */
	public static final int MENU = 0x2;
	
	/**
	 * Oznaka glavnog toolbar-a koji podrazumevano postoji
	 */
	public static final String MAIN_TOOLBAR = "__MAIN_TOOLBAR__";


	/**
	 * <p>
	 * Metoda koja vraća poziciju na koju akcija treba da se doda. Ovo može da
	 * bude konstanta {@link ExGAction#TOOLBAR} ili {@link ExGAction#MENU} ili
	 * bitwise ILI operator između ove dve konstante ukoliko akcija treba da ide
	 * i u meni i u toolbar.
	 * <p>
	 * Ako akcija treba da se ugradi meni biće pozvana metoda
	 * {@link ExGAction#getMenu()} i shodno ako treba da se ugradi u toolbar
	 * pozvaće se metoda {@link ExGAction#getToolbar()}.
	 */
	public abstract int getActionPosition();

	/**
	 * Metoda koja treba da vrati string sa kodom toolbara u koji akcija treba
	 * da se ugradi. U najjednostavnijem slučaju ova metoda treba da vrati
	 * {@link ExGAction#MAIN_TOOLBAR} ako akcija treba da se ugradi u
	 * podrazumevani toolbar. Ako akcija treba da se ugradi u sopstveni toolbar
	 * treba da vrati njegovo ime. Ukoliko toolbar sa istim imenom već postoji,
	 * akcija će biti dodrata na njega, a ukoliko ne postoji biće napravljen
	 * novi toolbar sa vraćenim imenom.
	 * 
	 * @return Ime toolbar-a u koji treba da se ugradi akcija
	 */
	public abstract String getToolbar();

	/**
	 * <p>
	 * Vraća redni broj pozicije u toolbar-u. Ima potpuno isti efekat kao
	 * {@link #getMenuPostition()}
	 * <p>
	 * Metoda neće biti pozvana ako se akcija ne ugrađuje u toolbar.
	 * 
	 * @return Redni broj pozicije u toolbar-u.
	 */
	public abstract int getToolbarPosition();

	/**
	 * Učitava ikonicu.
	 * 
	 * @param path
	 *            putanja do ikonice
	 * @return ikonica
	 */
	protected Icon loadIcon(String path) {
		URL imageURL = getClass().getResource(path); //$NON-NLS-1$ //$NON-NLS-2$
		Icon icon = null;

		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		} else {
			System.err.println("Greška pri učitavanju slike " + path); //$NON-NLS-1$
		}

		return icon;
	}
}
