package tk.exgerm.core.model;

import java.util.List;
import java.util.Map;

import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeNotConnectedException;
import tk.exgerm.core.plugin.IVisitor;

/**
 * Predstavlja čvoor u {@link IGraph grafu}.
 * 
 * @author Tim 2
 */
public interface INode {

	/**
	 * Označava događaj da je dodat novi atribut.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node kome je dodat atribut</li>
	 * <li>attribute: String - atribut koji je dodat</li>
	 * </ol>
	 */
	public static final String NODE_ATTRIBUTE_ADDED = "node.attribute_added";

	/**
	 * Označava događaj da je atribut uklonjen
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node iz koga je uklonjen atribut</li>
	 * <li>attribute: String - atribut koji je uklonjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String NODE_ATTRIBUTE_REMOVED = "node.attribute_removed";

	/**
	 * Označava događaj da je atribut promenjen.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node u kome je atribut promenjen.</li>
	 * <li>attribute: String - atribut koji je izmenjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String NODE_ATTRIBUTE_CHANGED = "node.attribute_changed";

	/**
	 * Označava događaj da je ime noda promenjeno.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node kome je ime promenjeno</li>
	 * <li>oldName: String - staro ime noda</li>
	 * </ol>
	 */
	public static final String NODE_NAME_CHANGED = "node.name_changed";

	/**
	 * Označava događaj da je nod povezan.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node koji je povezan</li>
	 * <li>edge: IEdge - grana koja se zakačila na node</li>
	 * </ol>
	 */
	public static final String NODE_LINKED = "node.linked";

	/**
	 * Označava događaj da je nod razvezan.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - node koji je razvezan</li>
	 * <li>edge: IEdge - grana koja je bila vezana na node</li>
	 * </ol>
	 */
	public static final String NODE_UNLINKED = "node.unlinked";

	/**
	 * Vraća ime noda.
	 * 
	 * @return Ime noda.
	 */
	public String getName();

	/**
	 * Menja ime postojećeg noda u novo ime. Pošto {@link IGraph} nasleđuje ovaj
	 * interface, ova metoda se koristi i za menjanje imena grafa.
	 * 
	 * @param newName
	 *            Novo ime
	 * @throws ExGNameConflictException
	 *             Ukoliko se nod ili graf sa istim imenom već postoji u istom
	 *             prostoru imena.
	 */
	public void setName(String newName) throws ExGNameConflictException;

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
	 * ga cast-uje u odgovarajuću klasu.
	 * 
	 * @param attr
	 *            Naziv atributa
	 * @return Vrednost atributa
	 */
	public Object getAttribute(String attr);

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
	 * Uklanja postojeći atribut. Ako atribut ne postoji ništa se ne desi.
	 * 
	 * @param attr
	 *            Atribut koji se uklanja.
	 */
	public void removeAttribute(String attr);

	/**
	 * Vraća sve atribute, kao {@link java.util.Map mapu} parova
	 * {@link java.lang.String string} -> {@link java.lang.Object object}
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
	 * Dodaje novu {@link IEdge granu} na čvor. Grana mora da ima <em>to</em>
	 * ili <em>from</em> na ovaj nod ili će biti bačen
	 * ExGNodeNotConnectedException izuzetak.
	 * 
	 * @param edge
	 *            Grana koja se kači na čvor.
	 */
	public void link(IEdge edge) throws ExGNodeNotConnectedException;

	/**
	 * Uklanja {@link IEdge granu} sa čvora.
	 * 
	 * @param edge
	 *            Grana koja se uklanja.
	 */
	public void unlink(IEdge edge);

	/**
	 * Diretno povezuje dva {@link INode čvora}. Ovaj čvor će biti izvorni, a
	 * <em>node</em> će biti odredišni.
	 * 
	 * @param node
	 *            Čvor na koji ovaj čvor treba da se zakači.
	 * 
	 * @throws ExGNodeDoesNotExsistException
	 *             Ukoliko zadati čvor nije deo grafa.
	 * 
	 * @return Novo-kreiranu vezu.
	 */
	public IEdge link(INode node) throws ExGNodeDoesNotExsistException;

	/**
	 * Vraća listu svih grana koje su na ovaj čvor zakačene
	 * 
	 * @return Lista grana
	 */
	public List<IEdge> getConnectedEdges();

	/**
	 * Ispituje da li je prosleđeni graf roditelj čvora, beskonačne dubine
	 * 
	 * @param graph
	 *            za koji sumnjamo da je roditelj ovog čvora
	 * @return true ako jeste roditelj
	 */
	public boolean isChildOf(IGraph graph);

	/**
	 * Koliko god da je nod ugnježden u strukturu, ova metoda će vratiti graf
	 * koji je na vrhu.
	 * 
	 * @return Ultimativni root :)
	 */
	public IGraph getFinalRoot();

	/**
	 * Vraća na kom novou ignježdavanja je nod. Graf koji je na vrhu je na nivou
	 * 0, svi nodovi u njemu su 1, nodovi u podgrafu su 2 i td...
	 * 
	 * @return Nivo ugnježdenja noda
	 */
	public int getLevel();

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
