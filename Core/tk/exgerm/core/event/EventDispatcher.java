package tk.exgerm.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.plugin.IListener;
import static tk.exgerm.core.plugin.IListener.ALL_EVENTS;

/**
 * <p>
 * Vodi računa o eventovima. Ovoj klasi se registruju komponente (preko
 * {@link tk.exgerm.core.service.ICoreContext kontexta})
 * <p>
 * Postoji jedan specijalan event {@link IListener#ALL_EVENTS}.
 * {@link tk.exgerm.core.plugin.IListener Osluškivači} prijavljeni na ovaj
 * događaj će biti obavešteni kad se desi bilo koji događaj. Ovaj događaj ne bi
 * trebalo da generiše ni jedna komponenta. Ova funkcionalnost može biti korsna
 * za log, ili tako nešto...
 * 
 * @author Tim 2
 */
public class EventDispatcher {

	/**
	 * Mapa svih događaja i njihovih obrađivača.
	 */
	private Map<String, List<IListener>> events;

	/**
	 * Ne radi ništa pametno. Samo kreira mapu koja se koristi za prećenje
	 * događaja.
	 */
	public EventDispatcher() {
		this.events = new HashMap<String, List<IListener>>();
	}

	/**
	 * Dodaje {@link tk.exgerm.core.plugin.IListener osluškivač} za događaj
	 * <em>event</em>
	 * 
	 * @param event
	 *            Događaj koji se osluškuje
	 * @param listener
	 *            Obrađivač koga treba pozvati kad se desi događaj
	 */
	public void addListener(String event, IListener listener) {
		List<IListener> listenerList = events.get(event);
		if (listenerList == null)
			events.put(event, new ArrayList<IListener>());
		events.get(event).add(listener);
	}

	/**
	 * Ukljanja {@link tk.exgerm.core.plugin.IListener osluškivač} za događaj
	 * <em>event</em>
	 * 
	 * @param event
	 *            Događaj sa koga se sklanja osluškivač.
	 * @param listener
	 *            Osluškivač koji se sklanja
	 */
	public void removeListener(String event, IListener listener) {
		events.get(event).remove(listener);
	}

	/**
	 * Ukljanja {@link tk.exgerm.core.plugin.IListener osluškivač} iz svih
	 * događaja gde je registrovan.
	 * 
	 * @param listener
	 *            Listener koji se uklanja
	 */
	public void removeListener(IListener listener) {
		for (List<IListener> l : events.values()) {
			l.remove(listener);
		}
	}

	/**
	 * Javlja svim {@link tk.exgerm.core.plugin.IListener osluškivačima} da se
	 * desio događaj <em>event</em> i prosleđuje im sve parametre koje je
	 * događaj poslao.
	 * 
	 * @param event
	 *            Događaj koji se desio
	 * @param parameters
	 *            Parametri koje generiše događaj.
	 */
	public void raiseEvent(String event, Object... parameters) {
		List<IListener> listenerList = events.get(event);
		if (listenerList != null) {
			Iterator<IListener> it = listenerList.iterator();
			while (it.hasNext()) {
				it.next().raise(event, parameters);
			}
		}

		notifyAll(event, parameters);
	}

	/**
	 * Obaveštava osluškivače koji žele da slušaju svaki događaj.
	 * 
	 * @param event
	 *            Događaj koji se desio.
	 * @param parameters
	 */
	protected void notifyAll(String event, Object... parameters) {
		List<IListener> listenerList = events.get(ALL_EVENTS);
		if (listenerList != null) {
			Iterator<IListener> it = listenerList.iterator();
			while (it.hasNext()) {
				it.next().raise(event, parameters);
			}
		}
	}
}
