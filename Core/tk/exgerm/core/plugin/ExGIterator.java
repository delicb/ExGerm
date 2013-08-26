package tk.exgerm.core.plugin;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;

/**
 * Uniforman interface za sve načine prolaska kroz
 * {@link tk.exgerm.core.model.IGraph graf}. Konkretna implementacija treba da
 * vodi računa o početku i kraju {@link tk.exgerm.core.model.IGraph grafa}.
 * Ukoliko je {@link tk.exgerm.core.model.IGraph graf} cikličan, treba da
 * odredi koji {@link tk.exgerm.core.model.INode Ävor} se prvi obraÄđuje i da
 * pazi da se iteracija zaustavi kad se dođe ponovo do istog
 * {@link tk.exgerm.core.model.INode čvora}
 * 
 * @author Tim 2
 */
public interface ExGIterator {

	/**
	 * Konstanta za naziv iteratora koji prelazi po principu prvi u sirinu, a
	 * koji je implementiran u Core komponenti
	 */
	public static final String BREADTH_FIRST = "__DEFAULT_BREADTH_FIRST_ITERATOR__";

	/**
	 * Konstanta za naziv iteratora koji prelazi po principu prvi u dubinu, a
	 * koji je implementiran u Core komponenti
	 */
	public static final String DEPTH_FIRST = "__DEFAULT_DEPTH_FIRST_ITERATOR__";

	/**
	 * Konstanta za naziv default iteratora koji prolazi kroz nodove grafa bez
	 * specifičnog paterna ali garantujue prolayak kroz sve nodove
	 */
	public static final String DEFAULT = "__DEFAULT_EXGERM_ITERATOR__";

	/**
	 * Konstanta za node preko koga iterator dodje do tekućeg noda. Bitno za
	 * pretrage.
	 */
	public static final String PARENT = "__SEARCH_PARENT_NODE__";

	/**
	 * Oznacava dogadjaj da je pretraga završena. Parametar je lista nodova koji
	 * su rešenje.
	 */
	public static final String SEARCH_RESULT_FOUND = "iterator.search.result_found";

	/**
	 * Ova metoda treba da vrati ime iteratora. Ovo bi trebalo da bude ime nekog
	 * od poznatih algoritama za prolazak kroz graf.
	 */
	public String getName();

	/**
	 * Proverava da li je kraj iteracije.
	 * 
	 * @return <em>false</em> ako je kraj iteracije, inače <em>true</em>
	 */
	public boolean hasNext();

	/**
	 * Vraća sledeći {@link tk.exgerm.core.model.INode čvor} u iteraciji. Ovu
	 * metodu ne treba pozivati nakon što {@link ExGIterator#hasNext()} vrati
	 * <em>false</em>
	 * 
	 * @return Sledeći {@link tk.exgerm.core.model.INode čvor}
	 */
	public INode next();

	/**
	 * Ovu metodu bi trebalo da koristi IGraph kad mu neko zatraži iterator.
	 * Moguće je i da klijent uzme iterator pa da ručno podesi kroz koji graf
	 * želi da iterira, ali to nema mnogo smisla :)
	 * 
	 * @param graph
	 *            Graf kroz koji se iterira.
	 */
	public void setGraph(IGraph graph);

	/**
	 * Postavlja node od kojeg kreće iteracija
	 * 
	 * @param node
	 *            od kojeg počinje iteracija
	 * @throws ExGNodeDoesNotExsistException
	 *             u slučaju da traženi node ne postoji
	 */
	public void setStart(INode node) throws ExGNodeDoesNotExsistException;

	/**
	 * Postavlja poštovanje usmerenosti grafova
	 * 
	 * @param respect
	 *            true ukoliko je potrebno da poštuje usmerenost
	 */
	public void setRespectEdgeDirections(boolean respect);

	/**
	 * Metoda koju implementiraju samo neki iteratori kako bi omogućili čišćenje
	 * podataka koje su stavili u graf.
	 * Svaki iterator u opisu klase definiše da li je posle njega potrebno pozivati ovu metodu.
	 */
	public void clear();
}
