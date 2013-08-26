package tk.exgerm.core.service;

import java.awt.Frame;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.exception.ExGIteratorAlreadyExsistException;
import tk.exgerm.core.exception.ExGNotImplementedException;
import tk.exgerm.core.gui.MainWindow;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.plugin.ExGStatusbarContent;
import tk.exgerm.core.plugin.ExGSubmenu;
import tk.exgerm.core.plugin.IListener;

/**
 * Omogućava komunikaciju komponenti sa komponentom <em>Core</em>.
 * 
 * @author Tim 2
 */
public interface ICoreContext {

	/**
	 * Dogadjaj se ispaljuje kada se konfiguracija komponenti promeni
	 * 
	 * Parametar: component: String - naziv komponente
	 */
	public static final String CONFIGURATION_CHANGED = "config_manager.configuration_changed";

	/**
	 * Dogadjaj se ispaljuje pri promeni stanja glavnog prozora. Za više
	 * informacija pogledati {@link Frame}.
	 * 
	 * Parametri:
	 * <ul>
	 * <li>newValue - {@link Integer} nova vrednost stanja
	 * <li>oldValue - {@link Integer} stara vrednost stanja
	 * </ul>
	 * 
	 * @see Frame
	 */
	public static final String MAIN_WINDOW_STATE_CHANGED = "main_window.state_changed";

	/**
	 * Registruje {@link tk.exgerm.core.plugin.IListener osluškivač} da
	 * osluškuje događaj <em>event</em>
	 * 
	 * @param event
	 *            Događaj koji se osluškuje
	 * @param listener
	 *            Osluškivač događaja
	 */
	public void listenEvent(String event, IListener listener);

	/**
	 * Uklanja {@link tk.exgerm.core.plugin.IListener osluškivač} iz liste
	 * registrovanih za dati događaj.
	 * 
	 * @param event
	 *            Događaj iz koga se listener uklanja. Ovaj parametar je
	 *            potreban jer jedan listener može da sluša više događaja.
	 * 
	 * @param listener
	 *            IListener koji se uklanja.
	 */
	public void removeListener(String event, IListener listener);

	/**
	 * Uklanja {@link tk.exgerm.core.plugin.IListener osluškivač} iz liste svih
	 * događaja koje je slušao.
	 * 
	 * @param listener
	 *            Listener koji se uklanja.
	 */
	public void removeListener(IListener listener);

	/**
	 * Javalja svim osluškivačima događaja <em>event</em> da se desio događaj i
	 * prosleđuje im listu parametara <em>parameters</em>
	 * 
	 * @param event
	 *            Događaj koji se desio
	 * @param parameters
	 *            Parametri vezani za događaj
	 */
	public void raise(String event, Object... parameters);

	/**
	 * Kreira i vraća {@link tk.exgerm.core.service.IPipeLine pipeline} koji se
	 * može koristiti za pravljenje uvezanih komandi.
	 * 
	 * @return {@link tk.exgerm.core.service.IPipeLine pipeline}
	 */
	public IPipeLine createPipeLine();

	/**
	 * Kreira i vraća novi {@link tk.exgerm.model.INode nod} sa zadatim imenom.
	 * 
	 * @param name
	 *            Ime noda.
	 * @return Novi {@link tk.exgerm.model.INode nod}.
	 */
	public INode newNode(String name);

	/**
	 * Kreira novu {@link tk.exgerm.core.model.IEdge granu} sa zadtim izvornim i
	 * odredišnim čvorom.
	 * 
	 * @param from
	 *            Izvorni {@link tk.exgerm.core.model.INode čvor}
	 * @param to
	 *            Odredišni {@link tk.exgerm.core.model.INode čvor}
	 * 
	 * @return Novokreiranu granu
	 */
	public IEdge newEdge(INode from, INode to);

	/**
	 * Kreira i vraća novi {@link tk.exgerm.model.IGraph graf} sa zadatim
	 * imenom.
	 * 
	 * @param name
	 *            Ime grafa.
	 * @return Novi {@link tk.exgerm.model.IGraph graf}.
	 */
	public IGraph newGraph(String name);

	/**
	 * Postavlja poruku u {@link tk.exgerm.core.gui.StatusBar status bar}.
	 * 
	 * @param message
	 *            Poruka koja se postavlja
	 */
	public void setStatusbarMessage(String message);

	/**
	 * <p>
	 * Ako komponente podržavaju različite jezike, na ovaj način bi trebale da
	 * dobave trenutno aktivan jezik. Metoda vraća oznaku jezika detaljnije
	 * objašnjenu na {@link http
	 * ://www.i18nguy.com/unicode/language-identifiers.html ovom linku}.
	 * <p>
	 * Ukratko, string se sastoji od <i>oznake jezika-podvarijanta</i>. Na
	 * primer, za engleski jezik može da postoji en-US (engleski u americi) i
	 * en-GB (engleski u Velikoj Britaniji).
	 * <p>
	 * Komponenta bi trebala da prilagodi jezik najbliži traženom. Recimo, ako
	 * ova metoda vrati <em>en-GB</em>, a komponenta podržava samo
	 * <em>en-US</em> taj jezik treba da iskoristi.
	 * <p>
	 * Moguće je da ova metoda vrati samo dvoslovnu oznaku jezika (na primer
	 * samo <em>en</em>). U tom slučaju komponenta treba sama da izabere jednu
	 * od varijanti engleskog jezika za prikazivanje.
	 * <p>
	 * Što se srpskog jezika tiče, standard propisuje oznake i za ćirilicu i za
	 * latinicu. One su <em>sr-Cyrl</em> i <em>sr-Latn</em>, respektivno, i ovo
	 * će biti vraćeno u tom slučaju.
	 * 
	 * @return Oznaku trenutno aktivnog jezika u aplikaciji.
	 */
	public String getLangage();

	/**
	 * Ovu metodu treba da koriste komponente koje hoće da dodaju nešto u glavni
	 * prozor.
	 * 
	 * @param position
	 *            <ul>
	 *            <li>ExGGraphicalComponent#CENTER</li>
	 *            <li>
	 *            ExGGraphicalComponent#SOUTH</li>
	 *            <li>
	 *            ExGGraphicalComponent#EAST</li>
	 *            <li>ExGGraphicalComponent#WEST</li>
	 *            </ul>
	 * @param component
	 *            Komponenta koja se dodaje
	 */
	public void addGraphicalComponent(ExGGraphicalComponent component);

	/**
	 * Ako je komponenta dodala nešto u glavni prozor i želi da skloni to ovu
	 * metodu treba da iskoristi.
	 * 
	 * @param component
	 *            Komponenta koja se uklanja. Mora biti ista referenca koja je
	 *            korišćena u metodi
	 *            {@link ICoreContext#addGraphicalComponent(int, JComponent)}
	 */
	public void removeGraphicalComponent(ExGGraphicalComponent component);

	/**
	 * Koriste se za dodavanje akcija u menije i toolbar glavnog prozora.
	 * 
	 * @param action
	 *            Akcija koja se dodaje
	 */
	public void addAction(ExGAction action);

	/**
	 * Koristi se za uklanjanje akcije iz glavnog prozora. Akcija će biti
	 * uklonjena sa svih mesta na koje je bila dodata metodom
	 * {@link ICoreContext#addAction(ExGAction)}.
	 * 
	 * @param action
	 *            Akcija koja se uklanja. Mora biti ista instanca kao ona koja
	 *            je prosleđena metodom
	 *            {@link ICoreContext#addAction(ExGAction)}
	 */
	public void removeAction(ExGAction action);

	/**
	 * Koristi se za dodavanje čitavog podmenija u glavni meni.
	 * 
	 * @param menu
	 *            Kod glavnog menija u koji se dodaje.
	 * @param submenu
	 *            Podmeni koji se dodaje.
	 */
	public void addSubmenu(ExGSubmenu submenu);

	/**
	 * Uklanjanje podmanija.
	 * 
	 * @param submenu
	 *            Podmeni koji se uklanja.
	 */
	public void removeSubmenu(ExGSubmenu submenu);

	/**
	 * Registruje novi meni na koji mogu da se dodaju akcije.
	 * 
	 * @param menuCode
	 *            Kod menija preko koga se akcija registruje. {@see ExGAction}.
	 *            Preko ovog koda i druge komponente mogu da se dodaju u ovaj
	 *            meni.
	 * @param menuName
	 *            Ime menija koji se kreira (ovaj string će se pojaviti u
	 *            GUI-u).
	 */
	public void registerMenu(String menuCode, String menuName);

	/**
	 * Registruje novi toolbar u koji mogu da se dodaju akcije.
	 * 
	 * @param toolbarCode
	 *            Kod toolbar-a preko koga se akcije registruju {@see
	 *            ExGERMAction } Preko ovog koda i druge komponente mogu da
	 *            dodaju svoje akcije u ovaj toolbar.
	 */
	public void registerToolbar(String toolbarCode);

	/**
	 * Dodavanje nove komponente u statusbar.
	 * 
	 * @param statusbarContent
	 *            Komponenta koja se dodaje.
	 */
	public void addStatusbarContent(ExGStatusbarContent statusbarContent);

	/**
	 * Uklanjanje komponente iz statusbar-a.
	 * 
	 * @param statusbarContent
	 *            Komponenta koja se uklanja.
	 */
	public void removeStatusbarContent(ExGStatusbarContent statusbarContent);

	/**
	 * Koristi se za dodavanje komponente koja služi za konfiguraciju
	 * komponente. Ovo će biti prikazano u posebnom tab-u u konfiguracionom
	 * prozoru.
	 * 
	 * @param configPane
	 *            GUI za konfiguraciju kompnente
	 */
	public void addConfigPane(ExGConfigPanel configPane);

	/**
	 * Koristi se za uklanjanje GUIa za konfiguraciju komponente.
	 * 
	 * @param configPane
	 *            GUI za konfiguraciju komponente. Mora biti ista instanca kao
	 *            ona koja je prosleđena metodom
	 *            {@link ICoreContext#addConfigPane(ExGConfigPanel)}
	 */
	public void removeConfigPane(ExGConfigPanel configPane);

	/**
	 * Dodaje {@link tk.exgerm.core.model.IGraph graf} u registar
	 * 
	 * @param graph
	 *            {@link tk.exgerm.core.model.IGraph Graf} koji se dodaje.
	 * 
	 * @throws ExGGraphExsistException
	 *             Ukoliko graf sa istim imenom već postoji u registru
	 * @throws ExGNotImplementedException
	 *             Ukoliko ne postoji registrovan graph register
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
	 * 
	 * @throws ExGNotImplementedException
	 *             Ukoliko ne postoji registrovan graph register
	 */
	public IGraph getGraph(String name);

	/**
	 * Uklanja graf iz registra na osnovu imena <em>name</em>.
	 * 
	 * @param name
	 *            Ime {@link tk.exgerm.core.model.IGraph grafa} koji se uklanja.
	 * @throws ExGGraphDoesNotExsistException
	 *             Ukoliko graf sa traženim imenom ne postoji.
	 * @throws ExGNotImplementedException
	 *             Ovo je namenjeno da proxy baca. Normalan graph registar ne
	 *             mora niti treba ovo da baca
	 */
	public void removeGraph(String name) throws ExGGraphDoesNotExsistException;

	/**
	 * Uklanja graf <em>graph<em> iz registra.
	 * 
	 * @param graph
	 *            {@link tk.exgerm.core.model.IGraph Graf} koji se uklanja
	 * 
	 * @throws ExGGraphDoesNotExsistException
	 *             Ukoliko graf sa traženim imenom ne postoji.
	 * @throws ExGNotImplementedException
	 *             Ukoliko ne postoji registrovan graph register
	 */
	public void removeGraph(IGraph graph) throws ExGGraphDoesNotExsistException;

	/**
	 * Vraća sve grafove iz registra. Ukoliko ne postoji ni jedan graf u
	 * registru treba da vrati <em>null</em>
	 * 
	 * @return {@link java.util.List Lista} svih
	 *         {@link tk.exgerm.core.model.IGraph grafova} iz registra.
	 * 
	 * @throws ExGNotImplementedException
	 *             Ukoliko ne postoji registrovan graph register
	 */
	public List<IGraph> getAllGraphs();

	/**
	 * Dodaje komandu u registar komandi.
	 * 
	 * @param command
	 *            Komanda koja se registruje.
	 */
	public void registerCommand(ExGCommand command)
			throws ExGCommandAlreadyExistException;

	/**
	 * Uklanja registrovanu komandu iz registra
	 * 
	 * @param command
	 *            Komanda koja se uklanja.
	 */
	public void removeCommand(ExGCommand command);

	/**
	 * Vraća instancu komande na osnovu njene ključne reči.
	 * 
	 * @param command
	 *            Komada sa ključnom reči command
	 */
	public ExGCommand getCommand(String command);

	/**
	 * Vraća sve registrovane komande.
	 * 
	 * @return Sve registrovane komande.
	 */
	public List<ExGCommand> getAllCommands();

	/**
	 * Dodaje dodatni {@link ExGGraphRegister graph register}.
	 * 
	 * @param register
	 *            Registar koji se dodaje
	 */
	public void registerGraphRegister(ExGGraphRegister register);

	/**
	 * Uklanja graph register iz sistema.
	 * 
	 * @param register
	 *            Registar koji se uklanja.
	 */
	public void removeGraphRegister(ExGGraphRegister register);

	/**
	 * <p>
	 * Na ovaj način komponenta može da postavi neku vrednost pod određenim
	 * ključem da bi je kasnije pokupila ili da bi je neka druga komponenta
	 * pokupila. Pored mehanizma event-ova ovo je način na koji komponente mogu
	 * da razmenjuju podatke. Ne treba mešati ovo i eventove jer ovde treba da
	 * se ostavljaju podaci koji se mogu preuzeti bilo kad, a ne kad se desi
	 * neki događaj.
	 * <p>
	 * Na primer, visualizer može da postavi active graph kad se on promeni, a
	 * svi algoritmi mogu da ga preuzmu da bi znali nad čime da se izvrše. <br/>
	 * Konzola može da postavi aktivan graf, a sve komande da ga preuzmu...
	 * 
	 * @param key
	 *            Pod kojim ključem se čuva vrednost
	 * @param value
	 *            Sama vrednost
	 */
	public void addData(String key, Object value);

	/**
	 * Preuzimanje vrednosti postavljene metodom
	 * {@link ICoreContext#addData(String, Object)}.
	 * 
	 * @param key
	 *            Ključ pod kojim se traži vrednost.
	 * @return Tražena vrednost ili null ako vrednost nije postavljena.
	 *         Odgovornost primaoca je da cast-uje vrednost u pravu klasu.
	 */
	public Object getData(String key);

	/**
	 * Registruje novi iterator.
	 * 
	 * @param iteratorClass
	 *            Klasa iteratora koji se registruje. Ovo se šalje, je novi
	 *            iterator mora da se instancira svaki put kad se zatraži.
	 * @throws ExGIteratorAlreadyExsistException
	 *             Ukolik iterator sa istim imenom već postoji
	 */
	public void registerIterator(ExGIterator iterator)
			throws ExGIteratorAlreadyExsistException;

	/**
	 * Uklanja postojeći iterator.
	 * 
	 * @param name
	 *            Ime iteratora koji se uklanja.
	 */
	public void removeIterator(String name);

	/**
	 * Postavlja parametar u konfiguraciju.
	 * 
	 * @param category
	 *            Katerocija u ini fajlu (trebalo bi da bude ime komponente)
	 * @param key
	 *            Ključ pod kojim se čuva parametar
	 * @param value
	 *            Parametar koji se postavlja
	 */
	public void putConfigData(String key, String value);

	/**
	 * Vraća podatak iz konfiguracije na osnovu kategorije i ključa.
	 * 
	 * @param category
	 *            Kategorija u ini fajlu (trebalo bi da bude ime komponente).
	 * @param key
	 *            Ključ za koji se traži vrednost
	 * @return Vrednost iz kategorije <em>category</em> sa ključem <em>key</em>
	 */
	public String getConfigData(String key);

	/**
	 * Uklanja podatak o konfiguraciji na osnovu kategorije i kljuca.
	 * 
	 * @param category
	 *            Kategorija u ini fajlu (trebalo bi da bude ime komponente).
	 * @param key
	 *            Ključ za koji se uklanja vrednost
	 * @return Vrednost iz kategorije <em>category</em> sa ključem <em>key</em>
	 *         koja je ukonjena
	 */
	public String removeConfigData(String key);

	/**
	 * Služi za registraciju Helpa komponente, tj. upisivanje URL-a helpa u Core
	 * i drugih podataka.
	 * 
	 * @param help
	 *            komponente
	 */
	public void registerHelp(ExGHelp help);

	/**
	 * Uklanja prethodno registrovan Help.
	 * 
	 * @param help
	 */
	public void removeHelp(ExGHelp help);

	/**
	 * Vraća help za određenu komponentu.
	 * 
	 * @param name
	 *            ime komponente
	 * @return help komponente
	 */
	public ExGHelp getHelp(String name);

	/**
	 * Vraća listu svih registrovanih helpova
	 * 
	 * @return lista svih registrovanih helpova
	 */
	public Map<String, ExGHelp> getAllHelps();

	/**
	 * Zaustavlja proces zatvaranja aplikacije. Predviđeno je da se poziva samo
	 * u listeneru eventa IComponent.APPLICATION_CLOSING. U drugim slučajvima
	 * neće imati efekta.
	 */
	public void preventClosing();

	/**
	 * Sakriva glavni prozor aplikacije
	 */
	public void hideMainWindow();

	/**
	 * Prikazuje glavni prozor aplikacije
	 */
	public void showMainWindow();

	/**
	 * Određuje da li se {@link MainWindow glavni prozor} prikazuje.
	 * 
	 * @return <code>true</code> ako je prikazan, <code>false</code> ako nije
	 */
	public boolean isMainWindowsShown();

	/**
	 * Vraća stanje u kome se {@link MainWindow glavni prozor} nalazi
	 * 
	 * @return stanje u kome se prozor nalazi
	 * 
	 * @see Frame
	 * @see java.awt.Frame.getExtendedState()
	 */
	public int getMainWindowState();

	/**
	 * Postavlja stanje {@link MainWindow glavnog prozora}
	 * 
	 * @param state
	 *            stanje koje se postavlja prozoru
	 * 
	 * @see Frame
	 * @see java.awt.Frame.setExtendedState(int state)
	 */
	public void setMainWindowState(int state);
}
