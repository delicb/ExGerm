package tk.exgerm.core.plugin;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;

/**
 * Ovaj interface treba da implementiraju sve komande koje postoje u sistemu.
 * Uglavnom će to biti algoritmi, ali moguće je da bilo koja komponenta
 * registruje svoju komandu.
 * 
 * @author Tim 2
 */
public interface ExGCommand {
	
	/**
	 * Označava događaj da je potrebno sakriti ili prikazati filesystem path
	 * u promptu aktivne konzole
	 * 
	 * Parametri:
	 * <ol>
	 * <li>ps: PrintStream - odgovarajući stream putem kojeg prepoznajemo
	 * odgovarajuću konzolu</li>
	 * <li>show: Boolean - true-prikaži, false-sakrij</li>
	 * </ol>
	 */
	public static String SHOW_PATH_IN_PROMPT_CHANGED = "show_path_in_prompt_changed";
	
	/**
	 * Označava konstantu pod kojom se čuva trenutna putanja u file sistemu.
	 */
	public static String CURRENT_FILESYSTEM_PATH = "current_filesystem_path";

	/**
	 * Označava događaj da je promenjen aktuelni FS dir u aktivnoj konzoli
	 * 
	 * Parametri:
	 * <ol>
	 * <li>ps: PrintStream - odgovarajući stream putem kojeg prepoznajemo
	 * odgovarajuću konzolu</li>
	 * <li>path: String - string trenutno aktivnog patha</li>
	 * </ol>
	 */
	public static String FILESYSTEM_PATH_CHANGED = "filesystem_path_changed";
	
	/**
	 * Označava događaj da je promenjen aktivni node u konzoli.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>ps: PrintStream - odgovarajući stream putem kojeg prepoznajemo
	 * odgovarajuću konzolu</li>
	 * <li>node: INode - referenca na node koji se postavlja za aktivan (null za
	 * poništavanje aktivnog)</li>
	 * </ol>
	 */
	public static final String ACTIVE_NODE_CHANGED = "console.active_node_changed";

	/**
	 * Označava događaj da je promenjen aktivni graf u konzoli.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>ps: PrintStream - odgovarajući stream putem kojeg prepoznajemo
	 * odgovarajuću konzolu</li>
	 * <li>node: IGraph - referenca na graf koji se postavlja za aktivan (null
	 * za poništavanje aktivnog)</li>
	 * </ol>
	 */
	public static final String ACTIVE_GRAPH_CHANGED = "console.active_graph_changed";

	/**
	 * Označava ključ pod kojim se čuva aktivan graf konzole u Core-u.
	 * 
	 * Tip podataka: IGraph
	 */
	public static final String CONSOLE_ACTIVE_GRAPH = "console.active_graph";

	/**
	 * Označava ključ pod kojim se čuva aktivan čvor konzole u Core-u.
	 * 
	 * Tip podataka: INode
	 */
	public static final String CONSOLE_ACTIVE_NODE = "console.active_node";
	
	/**
	 * Vraća ključnu reč na koju se komanda odaziva.
	 * 
	 * @return Ključna reč na koju se komanda odaziva
	 */
	public String getKeyword();

	/**
	 * Izvršava komandu.
	 * 
	 * @param command
	 *            Komanda za izvršavanje
	 * @param params
	 *            Lista parametara komande
	 * 
	 * @throws ExGCommandErrorException
	 *             Ukoliko postoji graška u prosleđenim parametrima
	 * @return Rezultat izvršavanja komande. Ovo mora biti
	 *         {@link tk.exgerm.core.model.IGraph IGraph}
	 *         {@link ExGCommand#returnsGraph()} vraća true.
	 */
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException;

	/**
	 * Koristi se za proveru da li komanda vraća objekat tipa
	 * {@link tk.exgerm.core.model.IGraph IGraph}, da bi
	 * {@link tk.exgerm.core.service.IPipeLine IPipeLine} mogao da utvrdi na
	 * kojoj poziciji u pipeline-u algoritam može da se nalazi.
	 * 
	 * @return true ako je rezultat izvršavanja
	 *         {@link tk.exgerm.core.model.IGraph IGraph}, inače false
	 */
	public boolean returnsGraph();

	/**
	 * Vraća sintaksu koja se koristi za ovu komandu. Preporuka je da ovo bude
	 * dugačko jednu liniju, najviše dve.
	 * 
	 * @return Sinraksa komande
	 */
	public String getSyntax();

	/**
	 * Treba da vrati string sa opisom kako se koristi komanda. Ovo treba da
	 * bude detaljno objašnjenje komande, posledica izvršavanja, preduslova da
	 * bi se komanda izvršila, lista i objašnjenje svih argumenata, opcionih i
	 * obaveznih...
	 * 
	 * @return Help komande
	 */
	public String getHelp();
}
