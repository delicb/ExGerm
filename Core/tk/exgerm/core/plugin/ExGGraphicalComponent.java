package tk.exgerm.core.plugin;

import javax.swing.JComponent;

public interface ExGGraphicalComponent {

	/**
	 * Konstante koje treba da koriste grafičke komponente za određivanje
	 * pozicije unutar {@link tk.exgerm.core.gui.MainWindow glavnog prozora}
	 */
	public final int CENTER = 0x1;
	public final int SOUTH = 0x2;
	public final int EAST = 0x4;
	public final int WEST = 0x8;

	/**
	 * Označava događaj da je tab zatvoren.
	 * 
	 * Parametr:
	 * <ol>
	 * <li>component: JComponent - komponenta koja se prikazuje u tab-u</li>
	 * </ol>
	 */
	public static final String TAB_CLOSED = "tabmanager.tab_closed";

	/**
	 * Označava događaj da je aktivan tab promenjen.
	 * 
	 * Parametar:
	 * <ol>
	 * <li>component: JComponent - komponenta koju prikazuje sada aktivan tab</li>
	 * </ol>
	 */
	public static final String ACTIVE_TAB_CHANGED = "tabmanager.active_tab_changed";

	/**
	 * Označava događaj da je naziv tab-a promenjen.
	 * 
	 * Parametari:
	 * <ol>
	 * <li>component: JComponent - komponenta koju prikazuje tab ciji je naziv
	 * promenjen</li>
	 * <li>newTitle: String - novi naziv taba</li>
	 * </ol>
	 */
	public static final String TAB_TITLE_CHANGED = "tabmanager.tab_title_changed";

	/**
	 * Označava događaj da je komponenta dobila fokus.
	 * 
	 * Parametar:
	 * <ol>
	 * <li>component: JComponent - komponenta koja je dobila fokus</li>
	 * </ol>
	 */
	public static final String FOCUSED_COMPONENT_CHANGED = "exggraphicalcomponent.focused_component_changed";

	/**
	 * Treba da vrati jednu od konstanti:
	 * <ul>
	 * <li>{@link ExGGraphicalComponent#CENTER}</li>
	 * <li>{@link ExGGraphicalComponent#SOUTH}</li>
	 * <li>{@link ExGGraphicalComponent#EAST}</li>
	 * <li>{@link ExGGraphicalComponent#WEST}</li>
	 * </ul>
	 * i predstavlja poziciju unutar {@link tk.exgerm.core.gui.MainWindow
	 * glavnog prozora} gde će se komponenta prikazati.
	 * 
	 * @return Poziciju komponente
	 */
	public int getPosition();

	/**
	 * Vraća komponentu koju treba prikazati unutar glavnog prozora. Ono što
	 * metoda {@link JComponent#getName()} vraćene komponente vrati će biti
	 * iskorišženo za ime taba.
	 * 
	 * @return Komponentu koja treba da se prikaže
	 */
	public JComponent getComponent();

	/**
	 * Ispituje da li je moguće promeniti ime tabova komponente
	 * 
	 * @return true ako je dozvoljeno menjanje imena
	 */
	public boolean isTabNameChangeable();
}
