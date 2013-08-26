package tk.exgerm.core.plugin;

/**
 * Interface koji treba da implementira klasa koja želi da osluškuje događaje.
 * Osluškivači se registruju u {@link tk.exgerm.core.event.EventDispatcher
 * EventDispatcher-u}
 * 
 * @author Tim 2
 */
public interface IListener {
	
	/**
	 * Konstanta koja obeležava specijalan događaj. Svi koji se registruju
	 * da slušaju ovaj događaj će biti obavešteni svaki put da se svaki događaj
	 * desi. Komponente ne bi trebalo da generišu ovaj događaj.
	 */
	public static final String ALL_EVENTS = "__ALL__";
	
	/**
	 * Obaveštava osluškivača da se desio događaj i prosleđuje mu parametre koje
	 * se događaj poslao.
	 * 
	 * @param event
	 *            Događaj koji se desio
	 * @param parameters
	 *            Parametri koje je događaj poslao.
	 */
	public void raise(String event, Object... parameters);
}
