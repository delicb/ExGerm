package tk.exgerm.core.plugin;

import java.util.List;

import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.model.IGraph;

/**
 * <p>
 * Ovaj interface treba da implementria komponenta koja predstavlja registrar
 * {@link tk.exgerm.core.model.IGraph grafova}.
 * 
 * <p>
 * Registar ima mogućnosti dodavanja, brisanja i preuzimanja grafova. Način na
 * koji registar čuva grafove je ostavljen implementaciji. U Core-u je
 * impelentiran jedostavan registrar koji čuva grafove u RAM-u, tako da ne
 * postoji način njihovog restauriranja kad se program ponovo pokrene. Neka
 * druga komponena može da čuva grafove na nekoj trajnoj lokaciji...
 * 
 * @author Tim 2
 */
public interface ExGGraphRegister {

	/**
	 * Označava događaj da je dodat graf u registar.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf koji je dodat</li>
	 * </ol>
	 */
	public static final String GRAPH_ADDED = "graph_added";

	/**
	 * Označava događaj da je graf uklonjen iz registra.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf koji je uklonjen</li>
	 * </ol>
	 */
	public static final String GRAPH_REMOVED = "graph_removed";

	/**
	 * Dodaje {@link tk.exgerm.core.model.IGraph graf} u registar
	 * 
	 * @param graph
	 *            {@link tk.exgerm.core.model.IGraph Graf} koji se dodaje.
	 * 
	 * @throws ExGGraphExsistException
	 *             Ukoliko graf sa istim imenom već postoji u registru
	 */
	public void addGraph(IGraph graph) throws ExGGraphExsistException;

	/**
	 * Tražu u registru graf sa imenom <em>name</em> i vraća ga. Ako ne postoji
	 * {@link tk.exgerm.core.model.IGraph graf} sa traženim imenom, vraća
	 * <em>null</em>.
	 * 
	 * @param name
	 *            Ime grafa koji se traži.
	 * @return {@link tk.exgerm.core.model.IGraph Graf} sa imenom <em>name</em>
	 */
	public IGraph getGraph(String name);

	/**
	 * Uklanja graf iz registra na osnovu imena <em>name</em>.
	 * 
	 * @param name
	 *            Ime {@link tk.exgerm.core.model.IGraph grafa} koji se uklanja.
	 * @throws ExGGraphDoesNotExsistException
	 *             Ukoliko graf sa traženim imenom ne postoji.
	 */
	public void removeGraph(String name) throws ExGGraphDoesNotExsistException;

	/**
	 * Uklanja graf <em>graph<em> iz registra.
	 * 
	 * @param graph
	 *            {@link tk.exgerm.core.model.IGraph Graf} koji se uklanja
	 * 
	 * @throws ExGGraphDoesNotExsistException
	 *             Ukoliko graf koji treba da se ukloni ne postoji u registru.
	 */
	public void removeGraph(IGraph graph) throws ExGGraphDoesNotExsistException;

	/**
	 * Vraća sve grafove iz registra. Ukoliko ne postoji ni jedan graf u
	 * registru treba da vrati <em>null</em>
	 * 
	 * @return {@link java.util.List Lista} svih
	 *         {@link tk.exgerm.core.model.IGraph grafova} iz registra.
	 */
	public List<IGraph> getAllGraphs();

	/**
	 * Proverava da li se graf sa imenom <em>name</em> nalazi u registru.
	 * 
	 * @param name
	 *            Ime grafa
	 * @return true ako graf sa imenom <em>name</em> postoji u registru, false
	 *         inače
	 */
	public boolean containsGraph(String name);
}
