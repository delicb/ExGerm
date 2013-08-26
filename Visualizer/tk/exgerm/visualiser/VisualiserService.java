package tk.exgerm.visualiser;

import java.util.ArrayList;
import java.util.HashMap;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.actions.AddStateAction;
import tk.exgerm.visualiser.actions.BestFitAction;
import tk.exgerm.visualiser.actions.ClearSearchResultsAction;
import tk.exgerm.visualiser.actions.DirectedEdgeStateAction;
import tk.exgerm.visualiser.actions.EdgeStateAction;
import tk.exgerm.visualiser.actions.EraserStateAction;
import tk.exgerm.visualiser.actions.HelpAction;
import tk.exgerm.visualiser.actions.NewGraphAction;
import tk.exgerm.visualiser.actions.PointStateAction;
import tk.exgerm.visualiser.actions.PropertiesAction;
import tk.exgerm.visualiser.commands.ShowCommand;
import tk.exgerm.visualiser.config.VisualiserConfigWindow;
import tk.exgerm.visualiser.help.VisualiserHelp;
import tk.exgerm.visualiser.listeners.ActiveTabChangedListener;
import tk.exgerm.visualiser.listeners.ConfigChangedListener;
import tk.exgerm.visualiser.listeners.EdgeAddedListener;
import tk.exgerm.visualiser.listeners.EdgeDirectedChangedListener;
import tk.exgerm.visualiser.listeners.EdgeRemovedListener;
import tk.exgerm.visualiser.listeners.FocusedComponentChangedListener;
import tk.exgerm.visualiser.listeners.GraphAddedListener;
import tk.exgerm.visualiser.listeners.GraphDoubleClickedListener;
import tk.exgerm.visualiser.listeners.GraphRemovedListener;
import tk.exgerm.visualiser.listeners.GraphRenamedListener;
import tk.exgerm.visualiser.listeners.NewNodeListener;
import tk.exgerm.visualiser.listeners.NodeAddedListener;
import tk.exgerm.visualiser.listeners.NodeClickedListener;
import tk.exgerm.visualiser.listeners.NodeConnectListener;
import tk.exgerm.visualiser.listeners.NodeDoubleClickedListener;
import tk.exgerm.visualiser.listeners.NodeRemovedListener;
import tk.exgerm.visualiser.listeners.NodeRenamedListener;
import tk.exgerm.visualiser.listeners.SearchResultFoundListener;
import tk.exgerm.visualiser.listeners.TabClosedListener;
import tk.exgerm.visualiser.listeners.TreeEdgePropertiesListener;
import tk.exgerm.visualiser.listeners.TreeGraphPropertiesListener;
import tk.exgerm.visualiser.listeners.TreeNodePropertiesListener;
import tk.exgerm.visualiser.model.VisualiserModel;
import tk.exgerm.visualiser.navigator.NavigatorMenu;
import tk.exgerm.visualiser.view.VisualiserView;

@SuppressWarnings("unused")
public class VisualiserService implements IComponent {
	
	private ICoreContext coreContext;
	private BundleContext bundleContext;
	private ViewManager viewManager;
	
	private VisualiserHelp help;
	
	// Actions
	private ExGAction eraserStateAction;
	private ExGAction pointStateAction;
	private ExGAction addStateAction;
	private ExGAction edgeStateAction;
	private ExGAction directedEdgeStateAction;
	private ExGAction newGraphAction;
	private ExGAction propertiesAction;
	private ExGAction clearSearchResultsAction;
	private ExGAction bestFitAction;
	private ExGAction helpAction;
	
	//Navigator menu
	NavigatorMenu navigatorMenu;
	
	// Listeners
	private IListener graphAddedListener;
	private IListener nodeAddedListener;
	private IListener edgeAddedListener;
	private IListener nodeClickedListener;
	private IListener nodeDoubleClickedListener;
	private IListener graphDoubleClickedListener;
	private IListener nodeRemovedListener;
	private IListener edgeRemovedListener;
	private IListener focusedComponentChangedListener;
	private IListener activeTabChangedListener;
	private IListener tabClosedListener;
	private IListener graphRemovedListener;
	private IListener graphRenamedListener;
	private IListener nodeConnectListener;
	private IListener newNodeListener;
	private IListener searchResultFoundListener;
	private IListener edgeDirectedChangedListener;
	private IListener treeNodePropertiesListener;
	private IListener treeEdgePropertiesListener;
	private IListener treeGraphPropertiesListener;
	private IListener nodeRenamedListener;
	
	private IListener configurationChangedListener;
	
	private VisualiserConfigWindow visualiserConfig;
	
	// Commands
	private ExGCommand showCommand;

	// Components
	private HashMap<IGraph, Visualiser> visualisers;
	
	private boolean isNewActionGraph;
	private ArrayList<IGraph> collector;

	public boolean isNewActionGraph() {
		return isNewActionGraph;
	}

	public void setNewActionGraph(boolean isNewActionGraph) {
		this.isNewActionGraph = isNewActionGraph;
	}
	
	public VisualiserService(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		this.visualisers = new HashMap<IGraph, Visualiser>();
		this.isNewActionGraph = false;
		this.collector = new ArrayList<IGraph>();
	}

	@Override
	public void setContext(ICoreContext context) {
		coreContext = context;
		viewManager = new ViewManager(coreContext);
		
		help = new VisualiserHelp();
		coreContext.registerHelp(help);

		VisualiserConfig.createConfiguration(context);
		
		// Actions
		eraserStateAction = new EraserStateAction(viewManager);
		pointStateAction = new PointStateAction(viewManager);
		addStateAction = new AddStateAction(viewManager);
		edgeStateAction = new EdgeStateAction(viewManager);
		directedEdgeStateAction = new DirectedEdgeStateAction(viewManager);
		newGraphAction = new NewGraphAction(coreContext);
		propertiesAction = new PropertiesAction(viewManager, coreContext);
		clearSearchResultsAction = new ClearSearchResultsAction(viewManager);
		bestFitAction = new BestFitAction(viewManager);
		helpAction = new HelpAction(coreContext);
		
		//Navigator menu
		navigatorMenu = new NavigatorMenu(viewManager);
		
		// Listeners
		graphAddedListener = new GraphAddedListener(viewManager, coreContext);
		nodeAddedListener = new NodeAddedListener(coreContext, viewManager);
		edgeAddedListener = new EdgeAddedListener(viewManager, coreContext);
		nodeClickedListener = new NodeClickedListener(viewManager);
		nodeDoubleClickedListener = new NodeDoubleClickedListener(viewManager);
		graphDoubleClickedListener = new GraphDoubleClickedListener(viewManager);
		nodeRemovedListener = new NodeRemovedListener(viewManager);
		edgeRemovedListener = new EdgeRemovedListener(viewManager);
		focusedComponentChangedListener = new FocusedComponentChangedListener(viewManager);
		activeTabChangedListener = new ActiveTabChangedListener(viewManager, coreContext);
		tabClosedListener = new TabClosedListener();
		graphRemovedListener = new GraphRemovedListener(viewManager);
		graphRenamedListener = new GraphRenamedListener(viewManager);
		nodeConnectListener = new NodeConnectListener(coreContext);
		newNodeListener = new NewNodeListener(coreContext);
		searchResultFoundListener = new SearchResultFoundListener(viewManager);
		edgeDirectedChangedListener = new EdgeDirectedChangedListener(viewManager);
		treeNodePropertiesListener = new TreeNodePropertiesListener(viewManager, coreContext);
		treeEdgePropertiesListener = new TreeEdgePropertiesListener(viewManager, coreContext);
		treeGraphPropertiesListener = new TreeGraphPropertiesListener(viewManager, coreContext);
		nodeRenamedListener = new NodeRenamedListener(viewManager);
		configurationChangedListener = new ConfigChangedListener(navigatorMenu, viewManager);
		
		// Commands
		showCommand = new ShowCommand(this.coreContext, this.viewManager);

		//Navigator menu
		coreContext.addSubmenu(navigatorMenu);
		
		setUpActions();
		setUpListeners();
		
		visualiserConfig = new VisualiserConfigWindow(context);
		coreContext.addConfigPane(visualiserConfig);

		try {
			setUpcommands();
		} catch (ExGCommandAlreadyExistException e) {
			e.printStackTrace();
		}
		
		VisualiserConfig.getInstanse().loadConfiguration();
	}
	
	private void setUpListeners(){
		coreContext.listenEvent(ExGGraphRegister.GRAPH_ADDED, graphAddedListener);
		coreContext.listenEvent(ExGGraphRegister.GRAPH_REMOVED, graphRemovedListener);
		coreContext.listenEvent(IGraph.GRAPH_NODE_ADDED, nodeAddedListener);
		coreContext.listenEvent(IGraph.GRAPH_EDGE_ADDED, edgeAddedListener);
		coreContext.listenEvent("graphtree.node_clicked", nodeClickedListener);
		coreContext.listenEvent("graphtree.graph_doubleclicked", graphDoubleClickedListener);
		coreContext.listenEvent(IGraph.GRAPH_NODE_REMOVED, nodeRemovedListener);
		coreContext.listenEvent(IGraph.GRAPH_EDGE_REMOVED, edgeRemovedListener);
		coreContext.listenEvent(ExGGraphicalComponent.FOCUSED_COMPONENT_CHANGED, focusedComponentChangedListener);
		coreContext.listenEvent(ExGGraphicalComponent.ACTIVE_TAB_CHANGED, activeTabChangedListener);
		coreContext.listenEvent(ExGGraphicalComponent.TAB_CLOSED, tabClosedListener);
		coreContext.listenEvent("graphtree.node_doubleclicked", nodeDoubleClickedListener);
		coreContext.listenEvent("graphtree.node_connect", nodeConnectListener);
		coreContext.listenEvent("graphtree.new_node", newNodeListener);
		coreContext.listenEvent("graphtree.new_subgraph", newNodeListener);
		coreContext.listenEvent(ExGIterator.SEARCH_RESULT_FOUND, searchResultFoundListener);
		coreContext.listenEvent(IEdge.EDGE_DIRECTED_CHANGED, edgeDirectedChangedListener);
		coreContext.listenEvent("graphtree.node_properties", treeNodePropertiesListener);
		coreContext.listenEvent("graphtree.edge_properties", treeEdgePropertiesListener);
		coreContext.listenEvent("graphtree.graph_properties", treeGraphPropertiesListener);
		coreContext.listenEvent(INode.NODE_NAME_CHANGED, nodeRenamedListener);
		coreContext.listenEvent(ICoreContext.CONFIGURATION_CHANGED, configurationChangedListener);
		coreContext.listenEvent(IGraph.GRAPH_NAME_CHANGED, graphRenamedListener);
	}
	
	private void setUpActions(){
		
		coreContext.addAction(newGraphAction);
		coreContext.addAction(pointStateAction);
		coreContext.addAction(addStateAction);
		coreContext.addAction(edgeStateAction);
		coreContext.addAction(directedEdgeStateAction);
		coreContext.addAction(eraserStateAction);
		coreContext.addAction(bestFitAction);
		coreContext.addAction(propertiesAction);
		coreContext.addAction(clearSearchResultsAction);
		coreContext.addAction(helpAction);
	}
	
	private void setUpcommands() throws ExGCommandAlreadyExistException{
		coreContext.registerCommand(showCommand);
	}
	
	public void turnOff(){
		// Remove actions
		coreContext.removeAction(newGraphAction);
		coreContext.removeAction(pointStateAction);
		coreContext.removeAction(addStateAction);
		coreContext.removeAction(edgeStateAction);
		coreContext.removeAction(directedEdgeStateAction);
		coreContext.removeAction(eraserStateAction);
		coreContext.removeAction(clearSearchResultsAction);
		coreContext.removeAction(propertiesAction);
		coreContext.removeAction(bestFitAction);
		coreContext.removeAction(helpAction);
		
		//Navigator menu
		coreContext.removeSubmenu(navigatorMenu);
		
		// Remove listeners
		coreContext.removeListener(graphAddedListener);
		coreContext.removeListener(graphRemovedListener);
		coreContext.removeListener(nodeAddedListener);
		coreContext.removeListener(edgeAddedListener);
		coreContext.removeListener(nodeClickedListener);
		coreContext.removeListener(nodeDoubleClickedListener);
		coreContext.removeListener(graphDoubleClickedListener);
		coreContext.removeListener(edgeRemovedListener);
		coreContext.removeListener(nodeRemovedListener);
		coreContext.removeListener(focusedComponentChangedListener);
		coreContext.removeListener(activeTabChangedListener);
		coreContext.removeListener(tabClosedListener);
		coreContext.removeListener(nodeConnectListener);
		coreContext.removeListener(newNodeListener);
		coreContext.removeListener(searchResultFoundListener);
		coreContext.removeListener(edgeDirectedChangedListener);
		coreContext.removeListener(treeNodePropertiesListener);
		coreContext.removeListener(treeEdgePropertiesListener);
		coreContext.removeListener(treeGraphPropertiesListener);
		coreContext.removeListener(nodeRenamedListener);
		coreContext.removeListener(graphRenamedListener);
		
		coreContext.removeListener(configurationChangedListener);
		
		coreContext.removeConfigPane(visualiserConfig);
		// Remove commands
		coreContext.removeCommand(showCommand);
		
		// Remove components
		clearAllVisualisers();
		
		coreContext.removeHelp(help);
	}
	
	public void turnOn(){
		ArrayList<IGraph> allGraphs = (ArrayList<IGraph>) coreContext.getAllGraphs();
		
		for(IGraph graph : allGraphs){
			collector.add(graph); // !!
			
			String graphName = graph.getName();
			
			VisualiserModel model = new VisualiserModel(coreContext);
			VisualiserView view = new VisualiserView(model);
			model.setView(view);
			view.setName(graphName);
			view.getModel().setGraph(graph);
			
			viewManager.addRootView(graphName, view);

			graphCrawler((ArrayList<INode>) graph.getAllNodes(), (ArrayList<IEdge>) graph.getAllEdges());
		}
		
		returnVisualiser();
	}
	
	private void graphCrawler(ArrayList<INode> nodes, ArrayList<IEdge> edges){
		VisualiserView view;
		try{
			view = viewManager.getSubView(nodes.get(0).getFinalRoot().getName(), nodes.get(0).getLevel()-1, nodes.get(0).getGraph().getName());
		}catch(Exception e){
			return;
		}
		
		for(INode node : nodes){
			if(node instanceof IGraph){
				collector.add((IGraph) node); // !!

				view.getModel().addNode(node.getName(), true, node.getFinalRoot().getName(), node.getLevel());
				
				VisualiserModel m = new VisualiserModel(coreContext);
				VisualiserView v = new VisualiserView(m);
				m.setView(v);
				v.setName(node.getName());
				v.setFinalRoot(node.getFinalRoot().getName());
				v.setLevel(node.getLevel());
				v.getModel().setGraph((IGraph) node);
				
				viewManager.addSubView(node.getFinalRoot().getName(), node.getLevel(), node.getName(), v);
				
				graphCrawler((ArrayList<INode>) ((IGraph)node).getAllNodes(), (ArrayList<IEdge>) ((IGraph)node).getAllEdges());
			}else{
				view.getModel().addNode(node.getName(), false, null, node.getLevel());
			}
		}

		for(IEdge e : edges){
			view.getModel().addEdge(e.getFrom().getName(), e.getTo().getName(), e.getID(), e.isDirected());
		}
	}
	
	private void returnVisualiser(){
		ArrayList<IGraph> g = new ArrayList<IGraph>();

		for (Object key : visualisers.keySet()) {
			if(!collector.contains(key)){
				g.add((IGraph) key);
			}
		}
		
		for(IGraph gr : g){
			visualisers.remove(gr);
		}

		for (Object key : visualisers.keySet()) {
			coreContext.addGraphicalComponent(visualisers.get(key));
		}
		
		collector.clear();
	}
	
	public HashMap<IGraph, Visualiser> getVisualisers() {
		return visualisers;
	}

	public void addVisualiser(VisualiserView view){
		Visualiser vis = new Visualiser(view);
		visualisers.put(view.getModel().getGraph(), vis);
		coreContext.addGraphicalComponent(vis);
	}
	
	public void removeVisualiser(VisualiserView view){
		coreContext.removeGraphicalComponent(visualisers.get(view.getModel().getGraph()));
		visualisers.remove(view.getModel().getGraph());
		if(visualisers.size() == 0){
			viewManager.setNullActiveView();
		}
	}
	
	public Visualiser getVisualiser(VisualiserView view){
		return visualisers.get(view.getModel().getGraph());
	}
	
	public void clearAllVisualisers(){
		for (Object key : visualisers.keySet()) {
			coreContext.removeGraphicalComponent(visualisers.get(key));
		}
	}
}
