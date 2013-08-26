package tk.exgerm.core.plugin;

import javax.swing.Icon;
import javax.swing.JPanel;

import tk.exgerm.core.gui.ConfigurationManager;

/**
 * Interfejs implementira {@link JPanel panel} koji konfigurabilna komponenta
 * prosleđuje {@link ConfigurationManager}-u. Na panelu se nalaze vizuelne
 * komponente preko kojih se vrši konfigurisanje.
 * 
 * @author Tim 2
 * 
 */
public interface ExGConfigPanel {

	/**
	 * Treba vratiti {@link JPanel panel} koji prikazuje
	 * {@link ConfigurationManager}.
	 * 
	 * @return panel koji prikazuje {@link ConfigurationManager}
	 */
	public JPanel getPanel();

	/**
	 * Naziv koji identifikuje panel sa podešavanjima.
	 * 
	 * @return naziv kartice sa podešavanjima
	 */
	public String getTitle();

	/**
	 * Ikona koja identifikuje panel sa podešavanjima.
	 * 
	 * @return {@link Icon ikona kartice sa podešavanjima}
	 */
	public Icon getIcon();

	/**
	 * Pozicija na koju panel zeli da se doda u okviru ConfigurationManager-a.
	 * 
	 * @return poziciju na koju komponenta zeli da se doda
	 */
	public int getPosition();

	/**
	 * Realizuje učitavanje podataka iz konfiguracionog fajla i osvežavanje
	 * vizuelnih komponenti panela.
	 */
	public void load();

	/**
	 * Realizuje čuvanje podataka u konfiguracioni fajl. Poziva je
	 * {@link ConfigurationManager} prilikom potvrđivanja čuvanja izmena od
	 * strane korisnika.
	 */
	public void save();
}
