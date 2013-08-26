package tk.exgerm.graphtree;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.actions.NewGraphTreeAction;
import tk.exgerm.graphtree.help.GraphTreeHelp;
import tk.exgerm.graphtree.listeners.EdgeCreatedListener;
import tk.exgerm.graphtree.listeners.EdgeDirectedListener;
import tk.exgerm.graphtree.listeners.EdgeRemovedListener;
import tk.exgerm.graphtree.listeners.GraphCreatedListener;
import tk.exgerm.graphtree.listeners.GraphRemovedListener;
import tk.exgerm.graphtree.listeners.GraphRenamedListener;
import tk.exgerm.graphtree.listeners.GraphTreeClosedListener;
import tk.exgerm.graphtree.listeners.GraphTreeOpenListener;
import tk.exgerm.graphtree.listeners.NodeCreatedListener;
import tk.exgerm.graphtree.listeners.NodeRemovedListener;
import tk.exgerm.graphtree.listeners.NodeRenamedListener;

public class GraphTreeService implements IComponent {

	ICoreContext coreContext;
	BundleContext bundleContext;
	GraphTree graphTree;
	
	/*SrcollPane u kom se nalazi GraphTree*/
	GraphTreeScroll graphTreeScroll;
	
	/*Listeneri GraphTree komponente*/
	IListener graphCreated;
	IListener graphRemoved;
	IListener nodeCreated;
	IListener nodeRemoved;
	IListener edgeCreated;
	IListener edgeREmoved;
	IListener nodeRenamed;
	IListener graphRenamed;
	IListener edgeDirected;
	IListener treeClosed;
	IListener treeOpened;
	
	/*Help koji komponenta prijavljuje*/
	GraphTreeHelp help;
	
	/*Konfiguracioni panel komponente
	 * Biće inicijalizovan zavisno od potrebe*/
	GraphTreeConfiguration config;
	
	/*Akcija za dodavanje nove GraphTree komponente*/
	NewGraphTreeAction newTreeAction;
	
	public GraphTreeService(BundleContext context) {
		this.bundleContext = context;
	}
	
	@Override
	public void setContext(ICoreContext coreContext) {
		
		this.coreContext = coreContext;		
		this.graphTree = new GraphTree(this.coreContext);
		initializeActions();
		this.coreContext.addAction(newTreeAction);	
		initializeListeners(graphTree);
		listenEvents();
		this.graphTreeScroll = new GraphTreeScroll(graphTree);
		this.coreContext.addGraphicalComponent(graphTreeScroll);
		this.help = new GraphTreeHelp();
		this.coreContext.registerHelp(help);
	}
	
	/**
	 * Metoda koja čisti sve svoje za sobom. Uklanja sve grafičke komponente,
	 * listenere, akcije, help, konfiguracioni panel.
	 */
	public void shutDown(){
		coreContext.removeGraphicalComponent(graphTreeScroll);
		removeListeners();
		coreContext.removeAction(newTreeAction);
		coreContext.removeHelp(help);
	}

	/**
	 * Metoda inicijalizuje sve listere za grafičku komponentu koja joj je 
	 * prosleđena.
	 * 
	 * @param tree - GraphTree kom treba inicijalizovati listenere
	 */
	public void initializeListeners(GraphTree tree){
		graphCreated = new GraphCreatedListener(tree, coreContext);
		graphRemoved = new GraphRemovedListener(tree, coreContext);
		nodeCreated = new NodeCreatedListener(tree, coreContext);
		nodeRemoved = new NodeRemovedListener(tree, coreContext);
		edgeCreated = new EdgeCreatedListener(tree, coreContext);
		edgeREmoved = new EdgeRemovedListener(tree, coreContext);
		nodeRenamed = new NodeRenamedListener(tree, coreContext);
		graphRenamed = new GraphRenamedListener(tree, coreContext);
		edgeDirected = new EdgeDirectedListener(tree, coreContext);
		treeClosed = new GraphTreeClosedListener(tree, coreContext, newTreeAction);
		treeOpened = new GraphTreeOpenListener(tree, coreContext, newTreeAction);
	}
	
	/**
	 * Metoda služi da ukloni sve listenere sa ugašene komponente u programu i 
	 * ponovo ih inicijalizuje za prosleđenu komponentu
	 * 
	 * @param tree - komponenta za koji treba ponovo inicijalizovati listenere
	 */
	public void reinitializeListeners(GraphTree tree){
		removeListeners();
		initializeListeners(tree);
		listenEvents();
	}
	
	/**
	 * Metoda inicijalizuje sve akcije ove komponente
	 */
	public void initializeActions(){
		newTreeAction = new NewGraphTreeAction(coreContext, this);
	}
	
	/**
	 * Metoda uklanja sve listenre ove komponente iz CoreContexta u kom su bili
	 * prijavljeni.
	 */
	public void removeListeners(){
		coreContext.removeListener(graphCreated);
		coreContext.removeListener(graphRemoved);
		coreContext.removeListener(nodeCreated);
		coreContext.removeListener(nodeRemoved);
		coreContext.removeListener(edgeCreated);
		coreContext.removeListener(edgeREmoved);
		coreContext.removeListener(nodeRenamed);
		coreContext.removeListener(graphRenamed);
		coreContext.removeListener(edgeDirected);
		coreContext.removeListener(treeClosed);
		coreContext.removeListener(treeOpened);	
	}
	
	/**
	 * Metoda CoreContextu prijavljuje koje će sve eventove ova kompoenenta 
	 * slušati.
	 */
	public void listenEvents(){
		this.coreContext.listenEvent(ExGGraphRegister.GRAPH_ADDED,graphCreated);
		this.coreContext.listenEvent(IGraph.GRAPH_NODE_ADDED,nodeCreated);
		this.coreContext.listenEvent(IGraph.GRAPH_EDGE_ADDED,edgeCreated);
		this.coreContext.listenEvent(ExGGraphRegister.GRAPH_REMOVED,graphRemoved);
		this.coreContext.listenEvent(IGraph.GRAPH_NODE_REMOVED,nodeRemoved);
		this.coreContext.listenEvent(IGraph.GRAPH_EDGE_REMOVED,edgeREmoved);
		this.coreContext.listenEvent(IGraph.GRAPH_NAME_CHANGED,graphRenamed);
		this.coreContext.listenEvent(INode.NODE_NAME_CHANGED,nodeRenamed);
		this.coreContext.listenEvent(IEdge.EDGE_DIRECTED_CHANGED,edgeDirected);
		this.coreContext.listenEvent(ExGGraphicalComponent.TAB_CLOSED,treeClosed);
		this.coreContext.listenEvent(ExGGraphicalComponent.ACTIVE_TAB_CHANGED,treeOpened);
	}
	
}
