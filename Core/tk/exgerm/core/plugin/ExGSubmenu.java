package tk.exgerm.core.plugin;

import javax.swing.JMenu;

/**
 * Interface koji svaki submenu treba da implementira.
 * 
 * Ako komponenti nije dovoljno da registruje nekoliko akcija, nego želi čitav
 * submenu za sebe na ovaj način to može da uradi.
 * 
 * @author Tim 2
 */
public interface ExGSubmenu extends ExGMenuItem {

	/**
	 * Vraća meni koji treba da se ugradi.
	 * 
	 * @return Meni koji treba da se ugradi...
	 */
	public JMenu getMenuContent();
}
