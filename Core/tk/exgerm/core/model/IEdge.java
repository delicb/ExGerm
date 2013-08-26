package tk.exgerm.core.model;

import java.util.List;
import java.util.Map;

import tk.exgerm.core.plugin.IVisitor;

/**
 * Predstavlja vezu između dva {@link INode noda}.
 * 
 * Ovo je samo interfejs koji vide klijenti.
 * 
 * @author Tim 2
 */
public interface IEdge {
	/**
	 * Označava događaj da je dodat novi atribut.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - grana kojoj je dodat atribut</li>
	 * <li>attribute: String - atribut koji je dodat</li>
	 * </ol>
	 */
	public static final String EDGE_ATTRIBUTE_ADDED = "edge.attribute_added";

	/**
	 * Označava događaj da je atribut uklonjen.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - grana iz koje je uklonjen atribut</li>
	 * <li>attribute: String - atribut koji je uklonjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String EDGE_ATTRIBUTE_REMOVED = "edge.attribute_removed";

	/**
	 * Označava događaj da je atribut promenjen.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - grana u kojoj je atribut promenjen.</li>
	 * <li>attribute: String - atribut koji je izmenjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String EDGE_ATTRIBUTE_CHANGED = "edge.attribute_changed";
	
	/**
	 * Označava događaj da je promenjena direkcija edgea.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - veza kojoj je promenjena direkcija</li>
	 * </ol>
	 */
	public static final String EDGE_DIRECTED_CHANGED = "edge.directed_changed";

	/**
	 * Vraća identifikator grane. Ovo je garantovano jedinstvano u sklopu jednog
	 * grafa.
	 * 
	 * @return Identifikator grane.
	 */
	public int getID();

	/**
	 * Vraća graf kome pripada
	 * 
	 * @return Graf kome pripada
	 */
	public IGraph getGraph();

	/**
	 * Postavlja graf kome pripada.
	 * 
	 * @param graph
	 *            Graf kome pripada.
	 */
	public void setGraph(IGraph graph);

	/**
	 * Vraća atribut tipa Object sa zadatim imenom. Odgovornost klijenta je da
	 * ga cast-uje u odgovarajuću klasu. Ako atribut ne postoji vraća
	 * <em>null</em>.
	 * 
	 * @param attr
	 *            Naziv atributa
	 * @return Vrednost atributa
	 */
	public Object getAttribute(String attr);

	/**
	 * Uklanja postojeći atribut. Ako atribut ne postoji ništa se ne desi.
	 * 
	 * @param attr
	 *            Atribut koji se uklanja.
	 */
	public void removeAttribute(String attr);

	/**
	 * Postavlja novi atribut. Ukoliko atribut već postoji biće prepisan, a
	 * ukoliko ne postoji, biće kreiran novi.
	 * 
	 * @param attr
	 *            Naziv atributa
	 * @param value
	 *            Vrednost atributa
	 */
	public void setAttribute(String attr, Object value);

	/**
	 * Vraća sve atribute, kao {@link java.util.Map mapu} parova
	 * {@link java.lang.String string} -> {@link java.lang.Object object}
	 * 
	 * Ako ne postoji ni jedan atribut mora da vrati praznu mapu.
	 * 
	 * @return Mapa atributa
	 */
	public Map<String, Object> getAllAttributes();

	/**
	 * Poziva metodu {@link tk.exgerm.core.plugin.IVisitor#visit(IEdge)} sa
	 * parametrom <em>this</em>. Posao se prepušta konkretnoj implementaciji
	 * interfejsa {@link tk.exgerm.core.plugin.IVisitor}
	 */
	public void accept(IVisitor visitor);

	/**
	 * Vraća odredišni {@link INode nod}.
	 * 
	 * @return Odredišni nod.
	 */
	public INode getTo();

	/**
	 * Vraća izvorni {@link INode nod}.
	 * 
	 * @return Izvorni nod.
	 */
	public INode getFrom();

	/**
	 * Vraća listu nodova koje povezuje. Ova lista ima dva elementa (from i to)
	 * koji mogu da se dobiju i pozivanjem metoda {@link IEdge#getFrom()} i
	 * {@link IEdge#getTo()}. U vraćenoj listi čvorovi će uvek biti u redosledu
	 * <em>from</em>, <em>to</em>.
	 * 
	 * @return Lista nodova koje povezuje.
	 */
	public List<INode> getNodes();

	/**
	 * @return Vraća true ako je grana usmerena, false inače
	 */
	public boolean isDirected();

	/**
	 * Postavlja flag da li je grana usmerena ili nije.
	 * 
	 * @param directed
	 *            True ako je grana usmerena, false ako nije
	 */
	public void setDirected(boolean directed);
	
	/**
	 * Postavlja podatak u nod. Služi za internu upotrebu.
	 * 
	 * @param key
	 *            Ključ pod kojim se čuva podatak.
	 * @param value
	 *            Vrednost.
	 */
	public void setData(String key, Object value);

	/**
	 * Vraća podatak sačuvan sa {@link #setData(String, Object)}
	 * 
	 * @param key
	 *            Ključ pod kojim je podatak sačuvan
	 * @return
	 */
	public Object getData(String key);
}
