package tk.exgerm.persistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.actions.OpenAction;
import tk.exgerm.persistance.actions.OpenAsAction;
import tk.exgerm.persistance.actions.SaveAction;
import tk.exgerm.persistance.actions.SaveAsAction;
import tk.exgerm.persistance.builder.Builder;
import tk.exgerm.persistance.builder.Serializer;
import tk.exgerm.persistance.commands.LoadCommand;
import tk.exgerm.persistance.commands.SaveCommand;
import tk.exgerm.persistance.help.PersistanceHelp;
import tk.exgerm.persistance.listeners.ApplicationClosingListener;
import tk.exgerm.persistance.listeners.EdgeChangedListener;
import tk.exgerm.persistance.listeners.GraphAddedListener;
import tk.exgerm.persistance.listeners.GraphChangedListener;
import tk.exgerm.persistance.listeners.GraphRemovedListener;
import tk.exgerm.persistance.listeners.GraphRenamedListener;
import tk.exgerm.persistance.listeners.NodeChangedListener;
import tk.exgerm.persistance.parser.InternalParseException;
import tk.exgerm.persistance.parser.Parser;

/**
 * Vodi računa o svim aspektima koji se koriste za persistance. Čuva sve komande
 * i akcije za kasnije čišćenje i pamti koji graf je iz kog fajla otvoren da
 * može da ga sačuva u isti file kasnije...
 * 
 * @author Tim 2
 */
public class PersistanceService implements IComponent {

	/**
	 * Za komunikaciju sa Core-om
	 */
	protected ICoreContext context;

	/**
	 * Sve komande, za čišćenje
	 */
	protected List<ExGCommand> commands = new ArrayList<ExGCommand>();

	/**
	 * Sve akcije, za čišćenje
	 */
	protected List<ExGAction> actions = new ArrayList<ExGAction>();

	/**
	 * Svi listeneri, za čišćenje
	 */
	protected List<IListener> listeners = new ArrayList<IListener>();
	
	/**
	 * Help, za čišćenje
	 */
	ExGHelp help;

	/**
	 * Za serijalizovanje grafova u fajlove
	 */
	protected Serializer serializer = new Serializer();

	/**
	 * Parovi <em>ime grafa</em> -
	 * <em>puna putanja do fajla iz koga je učitan</em>
	 */
	protected Map<String, String> loadedGraphs = new HashMap<String, String>();

	public PersistanceService() {

	}

	@Override
	public void setContext(ICoreContext context) {
		this.context = context;
		initializeDirtyFlags();
		registerCommands();
		registerActions();
		registerListeners();
		registerHelp();
	}

	/**
	 * Uklanja sve tragove postojanja persistance-a iz programa
	 */
	public void stop() {
		unregisterCommands();
		removeActions();
		removeListeners();
		removeHelp();
	}

	/**
	 * Parsira file, kreira graf i učitava ga u graf registar.
	 * 
	 * @param parser
	 *            Konkretna instance parsera koji se koristi u ovom konkretnom
	 *            slučaju.
	 * @param f
	 *            File koji se parsira. Ovo mora da bude postojeći file, u
	 *            suprotnom će se samo ispisati greška na System.err i ništa
	 *            neće biti urađeno. Odgovornost je klijenta da proveri da li
	 *            file postoji.
	 * 
	 * @throws ExGNameConflictException
	 *             Ukoliko <em>graphName</em> ne može biti upotrebljeno jer graf
	 *             sa istim imenom već postoji
	 * @throws InternalParseException
	 *             Ukoliko dođe do greške u parsiranju
	 */
	public IGraph parseFile(Parser parser, File f, boolean as_syntax)
			throws ExGNameConflictException, InternalParseException {
		try {
			IGraph g = parser.parse();
			if (!as_syntax) {
				loadedGraphs.put(g.getName(), f.getCanonicalPath());
			}
			context.addGraph(g);
			return g;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InternalParseException e) {
			throw new InternalParseException(e.getMessage());
		} catch (IOException e) {
			throw new ExGNameConflictException(e.getMessage());
		}
		return null;
	}

	/**
	 * Serijalizuje i čuva graf u file.
	 * 
	 * @param graph
	 *            Graf koji se čuva.
	 * @param builder
	 *            Builder koji će biti upotrebljen.
	 * @param f
	 *            File u koji se čuva
	 * @throws ExGNameConflictException
	 *             Ukoliko ne moze od fajla da se preuzme putanja...
	 */
	public void saveGraph(IGraph graph, Builder builder, File f)
			throws ExGNameConflictException {
		serializer.buildDocument(graph, builder, f);
		try {
			loadedGraphs.put(graph.getName(), f.getCanonicalPath());

			IGraph g = graph.getFinalRoot();
			g.setData(IGraph.DIRTY_FLAG, false);
			context.raise(IGraph.GRAPH_DIRTY_FLAG_CHANGED, g, false);

		} catch (IOException e) {
			throw new ExGNameConflictException(e.getMessage());
		}
	}

	/**
	 * Proverava da li je graf učitan iz fajla.
	 * 
	 * @param graphName
	 *            Ima grafa za koji se vrši provera.
	 * @return <em>true<em> ako je graf učitan iz fajla, <em>false<em> inače
	 */
	public boolean isGraphLoaded(String graphName) {
		return this.loadedGraphs.containsKey(graphName);
	}

	/**
	 * Vraća fajl iz koga je graf učitan.
	 * 
	 * @param graphName
	 *            Graf za koga se traži file iz koga je učitan.
	 * @return Putanju do fajla iz koga je graf učitan, ili <em>null</em> ako
	 *         graf nije učitan iz fajla.
	 */
	public String getGraphFile(String graphName) {
		return this.loadedGraphs.get(graphName);
	}

	public void graphChanged(IGraph g) {
		g.setData(IGraph.DIRTY_FLAG, true);
		context.raise(IGraph.GRAPH_DIRTY_FLAG_CHANGED, g, true);
	}
	
	public void graphAdded(IGraph g) {
		// ako je dodat graf koji smo učitali... 
		// ovo smem da radim, jer ce se ovo pozvati samo ako se graf učita
		// ili ako se napravi novi graf na akciju, i ni u jednom drugom slucaju
		if (isGraphLoaded(g.getName())) {
			g.setData(IGraph.DIRTY_FLAG, false);
		}
		else {
			g.setData(IGraph.DIRTY_FLAG, true);
		}
	}

	public void graphRenamed(String oldName, String newName) {
		String graphFile = loadedGraphs.get(oldName);
		loadedGraphs.remove(oldName);
		loadedGraphs.put(newName, graphFile);
	}

	public void graphRemoved(String graph) {
		this.loadedGraphs.remove(graph);
	}

	public void applicationClosing() {
		List<IGraph> dirtyGraphs = getDirtyGraphs();
		if (dirtyGraphs.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("There are unsaved graphs:\n");
			for (IGraph g : dirtyGraphs) {
				buffer.append(g.getName() + "\n");
			}
			buffer
					.append("To save them click Cancel and save them and then close the application.\n"
							+ "To discard them, click OK.");

			int res = JOptionPane.showConfirmDialog(null, new String(buffer),
					"Unsaved graphs", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (res == JOptionPane.CANCEL_OPTION)
				context.preventClosing();
		}
	}

	private void registerCommands() {
		registerCommand(new LoadCommand(this, context));
		registerCommand(new SaveCommand(this, context));
	}

	private void registerCommand(ExGCommand command) {
		commands.add(command);
		try {
			context.registerCommand(command);
		} catch (ExGCommandAlreadyExistException e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Persistance is trying to register already register command",
							"Persistance starting error",
							JOptionPane.ERROR_MESSAGE);
		}
	}

	private void unregisterCommands() {
		for (ExGCommand command : commands) {
			context.removeCommand(command);
		}
	}

	private void registerActions() {
		registerAction(new OpenAction(this, context));
		registerAction(new OpenAsAction(this, context));
		registerAction(new SaveAction(this, context));
		registerAction(new SaveAsAction(this, context));
	}

	private void registerAction(ExGAction action) {
		actions.add(action);
		context.addAction(action);
	}

	private void removeActions() {
		for (ExGAction action : actions) {
			context.removeAction(action);
		}
	}

	private void initializeDirtyFlags() {
		for (IGraph g : context.getAllGraphs()) {
			// na početku svi grafovi imaju dirty flag true,
			// jer ni jedan nije učitan iz fajla (Persistance se tek digao
			// pa nije ni bilo moguće učitavanje iz fajla.
			g.setData(IGraph.DIRTY_FLAG, true);
		}
	}

	private void registerListeners() {
		GraphChangedListener graphChanged = new GraphChangedListener(this,
				context);
		this.listeners.add(graphChanged);
		context.listenEvent(IGraph.GRAPH_NODE_ADDED, graphChanged);
		context.listenEvent(IGraph.GRAPH_NODE_REMOVED, graphChanged);
		context.listenEvent(IGraph.GRAPH_EDGE_ADDED, graphChanged);
		context.listenEvent(IGraph.GRAPH_EDGE_REMOVED, graphChanged);
		context.listenEvent(IGraph.GRAPH_ATTRIBUTE_ADDED, graphChanged);
		context.listenEvent(IGraph.GRAPH_ATTRIBUTE_CHANGED, graphChanged);
		context.listenEvent(IGraph.GRAPH_ATTRIBUTE_REMOVED, graphChanged);

		NodeChangedListener nodeChanged = new NodeChangedListener(this, context);
		this.listeners.add(nodeChanged);
		context.listenEvent(INode.NODE_NAME_CHANGED, nodeChanged);
		context.listenEvent(INode.NODE_ATTRIBUTE_ADDED, nodeChanged);
		context.listenEvent(INode.NODE_ATTRIBUTE_CHANGED, nodeChanged);
		context.listenEvent(INode.NODE_ATTRIBUTE_REMOVED, nodeChanged);

		EdgeChangedListener edgeChanged = new EdgeChangedListener(this, context);
		this.listeners.add(edgeChanged);
		context.listenEvent(IEdge.EDGE_ATTRIBUTE_ADDED, edgeChanged);
		context.listenEvent(IEdge.EDGE_ATTRIBUTE_CHANGED, edgeChanged);
		context.listenEvent(IEdge.EDGE_ATTRIBUTE_REMOVED, edgeChanged);

		GraphAddedListener graphAdded = new GraphAddedListener(this);
		this.listeners.add(graphAdded);
		context.listenEvent(ExGGraphRegister.GRAPH_ADDED, graphAdded);

		GraphRenamedListener graphRename = new GraphRenamedListener(this);
		this.listeners.add(graphRename);
		context.listenEvent(IGraph.GRAPH_NAME_CHANGED, graphRename);

		GraphRemovedListener graphRemoved = new GraphRemovedListener(this);
		this.listeners.add(graphRemoved);
		context.listenEvent(ExGGraphRegister.GRAPH_REMOVED, graphRemoved);

		ApplicationClosingListener closing = new ApplicationClosingListener(
				this);
		this.listeners.add(closing);
		context.listenEvent(IComponent.APPLICATION_CLOSING, closing);
	}

	private void removeListeners() {
		for (IListener l : listeners) {
			this.context.removeListener(l);
		}
	}

	private List<IGraph> getDirtyGraphs() {
		List<IGraph> allGraphs = context.getAllGraphs();
		List<IGraph> res = new ArrayList<IGraph>();
		for (IGraph g : allGraphs) {
			Boolean dirty = (Boolean) g.getData(IGraph.DIRTY_FLAG);
			// ni jedan graf iz registrija ne bi trbalo da ima null
			// za dirty flag, ali ko zna kakve brljotine mogu da se dese :)
			if (dirty == null || dirty)
				res.add(g);
		}
		return res;
	}
	
	private void registerHelp() {
		help = new PersistanceHelp();
		this.context.registerHelp(help);
	}
	
	private void removeHelp() {
		this.context.removeHelp(help);
	}

}
