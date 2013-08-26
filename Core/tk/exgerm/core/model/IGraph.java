package tk.exgerm.core.model;

import java.util.List;

import tk.exgerm.core.exception.ExGEdgeDoesNotExsistException;
import tk.exgerm.core.exception.ExGIteratorDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.plugin.ExGIterator;

/**
 * Reprezentuje graf u sistemu. Sadrži sve {@link INode čvorove} i {@link IEdge
 * grane} koje graf sadrži i podržava dodavanje i uklanjanje i jednog i drugog.
 * 
 * Nasleđuje {@link INode}, radi podržavanje ugnježdenih grafova.
 * 
 * @author Tim 2
 */
public interface IGraph extends INode {

	/**
	 * Označava događaj da je dodat nod u graf.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf u koji je nod dodat</li>
	 * <li>node: INode - nod koji je dodat</li>
	 * </ol>
	 */
	public static final String GRAPH_NODE_ADDED = "graph.node_added";

	/**
	 * Označava događaj da je dodata grana u graf.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf u koji je grana dodata</li>
	 * <li>edge: IEdge - grana koja je dodata</li>
	 * </ol>
	 */
	public static final String GRAPH_EDGE_ADDED = "graph.edge_added";

	/**
	 * Označava događaj da je izbačen nod iz grafa.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf iz koga je nod izbačen</li>
	 * <li>node: INode - nod koji je izbačen</li>
	 * </ol>
	 */
	public static final String GRAPH_NODE_REMOVED = "graph.node_removed";

	/**
	 * Označava događaj da je izbačena grana iz grafa.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf iz koga je grana izbačena</li>
	 * <li>edge: IEdge - grana koji je izbačena</li>
	 * </ol>
	 */
	public static final String GRAPH_EDGE_REMOVED = "graph.edge_removed";

	/**
	 * Označava događaj da je dodat novi atribut.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf kome je dodat atribut</li>
	 * <li>attribute: String - atribut koji je dodat</li>
	 * </ol>
	 */
	public static final String GRAPH_ATTRIBUTE_ADDED = "graph.attribute_added";

	/**
	 * Označava događaj da je atribut uklonjen
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf iz koga je uklonjen atribut</li>
	 * <li>attribute: String - atribut koji je uklonjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String GRAPH_ATTRIBUTE_REMOVED = "graph.attribute_removed";

	/**
	 * Označava događaj da je atribut promenjen.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf u kome je atribut promenjen.</li>
	 * <li>attribute: String - atribut koji je izmenjen</li>
	 * <li>oldValue: Object - stara vrednost</li>
	 * </ol>
	 */
	public static final String GRAPH_ATTRIBUTE_CHANGED = "graph.attribute_changed";

	/**
	 * Označava događaj da je ime grafa promenjeno.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf kome je ime promenjeno</li>
	 * <li>oldName: String - staro ime ovog grafa</li>
	 * </ol>
	 */
	public static final String GRAPH_NAME_CHANGED = "graph.name_changed";

	/**
	 * Oznčava događaj da se promenjen flag koji označava da li je graf menjan
	 * od poslednjeg save-a.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf kome je dirty flag promenjen</li>
	 * <li>value: Boolean - da li je dirty flag true ili false</li>
	 * </ol>
	 */
	public static final String GRAPH_DIRTY_FLAG_CHANGED = "persistance.dirty_flag_changed";

	/**
	 * Označava aktivno prikazan graf u visualizeru.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf koji je trenutno aktivan</li>
	 * </ol>
	 */
	public static final String ACTIVE_GRAPH = "visualiser.active_graph";

	/**
	 * Konstanta za podatak gde se čuva da li je graf menjan od poslednjeg
	 * save-a. Moguće je da bude null, ako nije pokrenut Peristance, koji vodi 
	 * računa o ovome ili ako je u pitanju podgraf.
	 */
	public static final String DIRTY_FLAG = "graph.dirty_flag";

	/**
	 * Vraća broj čvorova u grafu.
	 * 
	 * @return Broj čvorova u grafu.
	 */
	public int getNodeCount();

	/**
	 * Vraća broj grana u grafu.
	 * 
	 * @return Broj grana u grafu.
	 */
	public int getEdgeCount();

	/**
	 * Dodaje novi {@link tk.exgerm.core.model.INode čvor} u graf.
	 * 
	 * @param node
	 *            Čvor koji se dodaje
	 * 
	 * @throws ExGNodeAlreadyExsistException
	 *             Ukoliko nod sa istim imenom već postoji u grafu.
	 */
	public void addNode(INode node) throws ExGNodeAlreadyExsistException;

	/**
	 * Kreira novu granu između {@link tk.exgerm.core.model.INode čvorova}
	 * <em>to</em> i <em>from</em> i dodaje je u graf.
	 * 
	 * @param from
	 *            Izvorini {@link tk.exgerm.core.model.INode čvor}
	 * @param to
	 *            Odredišni {@link tk.exgerm.core.model.INode čvor}
	 * @param directed
	 *            da li je graf usmereni
	 * @throws ExGNodeDoesNotExsistException
	 *             Ukoliko ne postoji <em>to</em> ili <em>from</em> nod u grafu.
	 * 
	 * @return Novokreirana {@link tk.exgerm.core.model.IEdge grana}
	 */
	public IEdge addEdge(INode from, INode to, boolean directed)
			throws ExGNodeDoesNotExsistException;

	/**
	 * Dodaje novu granu u graf. Edge mora da ima valide izvorisni i odredišni
	 * čvor.
	 */
	public void addEdge(IEdge edge);

	/**
	 * Uklanja {@link tk.exgerm.core.model.INode čvor} iz grafa.
	 * 
	 * @param node
	 *            {@link tk.exgerm.core.model.INode Čvor} koji se uklanja
	 * 
	 * @throws ExGNodeDoesNotExsistException
	 *             Ukoliko zadati čvor nije deo grafa.
	 */
	public void removeNode(INode node) throws ExGNodeDoesNotExsistException;

	/**
	 * Uklanja {@link tk.exgerm.core.model.INode čvor} iz grafa na osnovu imena.
	 * 
	 * @param node
	 *            Ime {@link tk.exgerm.core.model.INode čvora} koji se uklanja
	 * @throws ExGNodeDoesNotExsistException
	 *             Ukoliko zadati čvor nije deo grafa.
	 */
	public void removeNode(String node) throws ExGNodeDoesNotExsistException;

	/**
	 * Uklanja granu iz grafa.
	 * 
	 * @param edge
	 *            Grana koja se uklanja
	 */
	public void removeEdge(IEdge edge);

	/**
	 * Uklanja granu iz grafa na osnovu ID-a.
	 * 
	 * @param ID
	 *            ID grane koja se uklanja
	 * @throws ExGEdgeDoesNotExsistException
	 *             Ukoliko grana sa traženim IDom nije deo grafa.
	 */
	public void removeEdge(int ID) throws ExGEdgeDoesNotExsistException;

	/**
	 * Vraća node po imenu.
	 * 
	 * @param name
	 *            Ime noda koji se traži
	 * @return Nod sa imenom <em>name</em> ili <em>null</em> ako nod sa traženim
	 *         imenom ne postoji.
	 */
	public INode getNode(String name);

	/**
	 * Vraća granu po ID-u.
	 * 
	 * @param ID
	 *            ID grane koja se traži.
	 * @return Granu sa traženim ID-om <em>ID</em> ili <em>null</em> ako grana
	 *         sa traženim ID-om ne postoji.
	 */
	public IEdge getEdge(int ID);

	/**
	 * Vraća sve čvorove iz grafa.
	 * 
	 * @return Sve čvorove iz grafa
	 */
	public List<INode> getAllNodes();

	/**
	 * Vraća sve grane iz grafa.
	 * 
	 * @return Sve grane iz grafa
	 */
	public List<IEdge> getAllEdges();

	/**
	 * Vraća iterator sa zadatim imenom. Iterator će biti inicijalizovan da radi
	 * sa ovim grafom. Traženi iterator prethodo mora biti registrovan.
	 * 
	 * @param name
	 * @return
	 */
	public ExGIterator getIterator(String name)
			throws ExGIteratorDoesNotExsistException;
}
