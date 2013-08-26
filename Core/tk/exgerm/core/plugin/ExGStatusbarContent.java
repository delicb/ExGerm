package tk.exgerm.core.plugin;

import javax.swing.JComponent;

import tk.exgerm.core.service.ICoreContext;

/**
 * Komponente koje žele da nešto ugrade u statusbar, treba ovaj interface da
 * implementijrau. Ukoliko komponenta samo želi da prikaže poruku u statusbar-u,
 * nema potreba da dodaje nešto novo, dovoljno je koristiti
 * {@link ICoreContext#setStatusbarMessage(String)} metodu. Međutim, ako
 * komponenta želi da registruje dugme, prograssbar, novu labelu samo za sebe
 * ili bilo šta drugo treba ovaj interface da impelementira.
 * 
 * @author Tim 2
 * 
 */
public interface ExGStatusbarContent {

	/**
	 * Konstante koje treba da koristi svaka komponenta koja želi da zauzme
	 * mesto u {@link tk.exgerm.core.gui.StatusBar status baru}.
	 */
	public final int LEFT = 0x1;
	public final int CENTER = 0x2;
	public final int RIGHT = 0x4;

	/**
	 * Vraća jednu od konstanti:
	 * <ul>
	 * <li>{@link IComponent#LEFT}</li>
	 * <li>{@link IComponent#CENTER}</li>
	 * <li>{@link IComponent#RIGHT}</li>
	 * </ul>
	 * Ova konstanta označava položaj na statusnoj liniji. Željeni položaj ne
	 * mora biti ispoštovan u smislu da će komponenta biti dodana u kranje levu
	 * stranu ako ova metoda vrati {@link ExGStatusbarContent#LEFT}, ali će biti
	 * grupisana na levu stranu.
	 * 
	 * @return
	 */
	public int getPosition();

	/**
	 * Sadržaj koji se dodaje u statusbar. Pošto je ovo JComponent, praktično
	 * može biti bilo šta (dugme, labela, progress bar...).
	 * 
	 * @return Sadržaj koji se ugrađuje u statusbar
	 */
	public JComponent getContent();
}
