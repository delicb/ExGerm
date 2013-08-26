package tk.exgerm.graphtree;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.actions.EdgePropertiesAction;
import tk.exgerm.graphtree.actions.EdgeRemoveAction;
import tk.exgerm.graphtree.actions.GraphPropertiesAction;
import tk.exgerm.graphtree.actions.GraphRemoveAction;
import tk.exgerm.graphtree.actions.HelpAction;
import tk.exgerm.graphtree.actions.NewNodeAction;
import tk.exgerm.graphtree.actions.NewSubgraphAction;
import tk.exgerm.graphtree.actions.NodeConnectAction;
import tk.exgerm.graphtree.actions.NodePropertiesAction;
import tk.exgerm.graphtree.actions.NodeRemoveAction;
import tk.exgerm.graphtree.model.Edge;
import tk.exgerm.graphtree.model.Graph;
import tk.exgerm.graphtree.model.GraphModel;
import tk.exgerm.graphtree.model.Node;
import tk.exgerm.graphtree.model.SubGraph;
import tk.exgerm.graphtree.model.Workspace;

@SuppressWarnings("serial")
public class GraphTree extends JTree implements TreeSelectionListener {

	/**
	 * Korenski čvor GraphTree-ja
	 */
	private Workspace root;
	private ICoreContext coreContext;
	
	/*Akcije za popup menije*/
	private NodePropertiesAction nodePropsAction;
	private EdgePropertiesAction edgePropsAction;
	private GraphPropertiesAction graphPropsAction;
	private NodeRemoveAction nodeRemoveAction;
	private EdgeRemoveAction edgeRemoveAction;
	private GraphRemoveAction graphRemoveAction;
	private NodeConnectAction nodeConnectAction;
	private NewNodeAction newNodeAction;
	private NewSubgraphAction newSubgraphAction;
	private HelpAction help;
	
	/*Popup meniji*/
	private JPopupMenu treePopGraph;
	private JPopupMenu treePopNode;
	private JPopupMenu treePopEdge;
	private JPopupMenu treePopSubgraph;
	
	/**
	 * Ime komponente. Konstanta sluđi za lakše slanje podataka u eventu 
	 * HELP_REQUESTED.
	 */
	private static String componentName = "GraphTree";

	/**
	 * Označava događaj da je na graphTree komponenti selektovan graf.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf koji je selektovan</li>
	 * </ol>
	 */
	public static final String GRAPH_SELECTED = "graphtree.graph_selected";

	/**
	 * Označava događaj da je na graphTree komponenti selektovan nod.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod koji je selektovan</li>
	 * </ol>
	 */
	public static final String NODE_SELECTED = "graphtree.node_selected";

	/**
	 * Označava događaj da je na graphTree komponenti selektovana veza
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - veza koja je selektovana</li>
	 * </ol>
	 */
	public static final String EDGE_SELECTED = "graphtree.edge_selected";

	/**
	 * Označava događaj da je kliknuto na graf na graphTree komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf na koji je kliknuto</li>
	 * </ol>
	 */
	public static final String GRAPH_CLICKED = "graphtree.graph_clicked";

	/**
	 * Označava događaj da je kliknuto na node na graphTree komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod na koji je kliknuto</li>
	 * </ol>
	 */
	public static final String NODE_CLICKED = "graphtree.node_clicked";

	/**
	 * Označava događaj da je kliknuto na vezu na graphTree komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - veza na koju je kliknuto</li>
	 * </ol>
	 */
	public static final String EDGE_CLICKED = "graphtree.edge_clicked";

	/**
	 * Označava događaj da je dvoklik uradjen nad grafom na graphTree
	 * komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf na koji je dvokliknuto</li>
	 * </ol>
	 */
	public static final String GRAPH_DOUBLECLICKED = "graphtree.graph_doubleclicked";

	/**
	 * Označava događaj da je dvoklik uradjen nad nodom na graphTree komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod na koji je dvokliknuto</li>
	 * </ol>
	 */
	public static final String NODE_DOUBLECLICKED = "graphtree.node_doubleclicked";

	/**
	 * Označava događaj da je dvoklik uradjen nad vezom na graphTree komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - veza na koju je dvokliknuto</li>
	 * </ol>
	 */
	public static final String EDGE_DOUBLECLICKED = "graphtree.edge_doubleclicked";

	/**
	 * Označava događaj da se desio desni klik nad grafom na graphTree
	 * komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf nad kojim se desio desni klik</li>
	 * </ol>
	 */
	public static final String GRAPH_RIGHTCLICKED = "graphtree.graph_rightclicked";

	/**
	 * Označava događaj da se desio desni klik nad nodom na graphTree
	 * komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod nad kojim se desio desni klik</li>
	 * </ol>
	 */
	public static final String NODE_RIGHTCLICKED = "graphtree.node_rightclicked";

	/**
	 * Označava događaj da se desio desni klik nad vezom na graphTree
	 * komponenti.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - veza nad kojom se desio desni klik</li>
	 * </ol>
	 */
	public static final String EDGE_RIGHTCLICKED = "graphtree.edge_rightclicked";

	/**
	 * Označava događaj da se desio desni klik nad nodom i da je nad njim u
	 * popup meniju zatražen poperti prozor.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod za koji se traži propertz prozor</li>
	 * </ol>
	 */
	public static final String NODE_PROPERTIES = "graphtree.node_properties";

	/**
	 * Označava događaj da se desio desni klik nad edgeom i da je nad njim u
	 * popup meniju zatražen poperti prozor.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>edge: IEdge - nod za koji se traži properti prozor</li>
	 * </ol>
	 */
	public static final String EDGE_PROPERTIES = "graphtree.edge_properties";

	/**
	 * Označava događaj da se desio desni klik nad grafom i da je nad njim u
	 * popup meniju zatražen poperti prozor.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf za koji se traži properti prozor</li>
	 * </ol>
	 */
	public static final String GRAPH_PROPERTIES = "graphtree.graph_properties";

	/**
	 * Označava događaj da je na graphTree komponenti kliknuto na akciju za
	 * konektovanje noda sa nekim drugim nodom.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>node: INode - nod koji treba konektovati sa drugim nodom</li>
	 * </ol>
	 */
	public static final String NODE_CONNECT = "graphtree.node_connect";

	/**
	 * Označava događaj da je na graphTree komponenti kliknuto na akciju za
	 * dodavanje novog noda.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf u koji treba dodati novi nod.</li>
	 * </ol>
	 */
	public static final String NEW_NODE = "graphtree.new_node";

	/**
	 * Označava događaj da je na graphTree komponenti kliknuto na akciju za
	 * dodavanje novog podgrafa.
	 * 
	 * Parametri:
	 * <ol>
	 * <li>graph: IGraph - graf u koji treba dodati novi podgraf.</li>
	 * </ol>
	 */
	public static final String NEW_SUBGRAPH = "graphtree.new_subgraph";

	public GraphTree(ICoreContext context) {
		this.coreContext = context;
		root = new Workspace("Workspace");
		this.setModel(new GraphModel(root));
		setName("GraphTree");
		addMouseListener(listener);
		addTreeSelectionListener(this);
		setScrollsOnExpand(true);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		setCellRenderer(new exGERMIconRenderer());
		initializeGraphs();
		initializeActions();
		initializePopups();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_F1
								&& e.getID() == KeyEvent.KEY_PRESSED
								&& GraphTree.this.hasFocus()) {
							e.consume();
							GraphTree.this.coreContext.raise(
									ExGHelp.HELP_REQUESTED,
									GraphTree.componentName,
									GraphTree.componentName);
						}
						return false;
					}
				});
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Object node = e.getPath().getLastPathComponent();
		expandPath(e.getPath());
		if (node instanceof Graph) {
			coreContext.raise(GRAPH_SELECTED, ((Graph) node).getGraph());
		} else if (node instanceof Node) {
			coreContext.raise(NODE_SELECTED, ((Node) node).getNode());
		} else if (node instanceof Edge) {
			coreContext.raise(EDGE_SELECTED, ((Edge) node).getEdge());
		}
	}

	public Workspace getRoot() {
		return root;
	}

	/**
	 * Metoda osvežava prikaz komponente. U slučaju bilo kakve izmene na modelu
	 * treba pozvati ovu metodu da osveži prikaz modela.
	 */
	public void updateUI() {
		super.updateUI();
	}

	MouseListener listener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			TreePath selPath = getPathForLocation(e.getX(), e.getY());
			if (e.getClickCount() == 2) {
				if (selPath != null) {
					Object node = selPath.getLastPathComponent();
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (node instanceof Node) {
							coreContext.raise(NODE_DOUBLECLICKED, ((Node) node)
									.getNode());
						} else if (node instanceof Edge) {
							coreContext.raise(EDGE_DOUBLECLICKED, ((Edge) node)
									.getEdge());
						} else if (node instanceof Graph) {
							coreContext.raise(GRAPH_DOUBLECLICKED,
									((Graph) node).getGraph());
						}
					}
				}
			} else if (e.getClickCount() == 1) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (selPath != null) {
						Object node = selPath.getLastPathComponent();
						if (node instanceof Node) {
							coreContext.raise(NODE_CLICKED, ((Node) node)
									.getNode());
						} else if (node instanceof Edge) {
							coreContext.raise(EDGE_CLICKED, ((Edge) node)
									.getEdge());
						} else if (node instanceof Graph) {
							coreContext.raise(GRAPH_CLICKED, ((Graph) node)
									.getGraph());
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					if (selPath != null) {
						Object node = selPath.getLastPathComponent();
						GraphTree.this.setSelectionPath(selPath);
						if (node instanceof SubGraph) {
							coreContext.raise(GRAPH_RIGHTCLICKED,
									((SubGraph) node).getNode());
							setSubgraphToActions((IGraph) (((SubGraph) node)
									.getNode()));
							GraphTree.this.getTreePop(3).show(GraphTree.this,
									e.getX(), e.getY());
						} else if (node instanceof Node) {
							coreContext.raise(NODE_RIGHTCLICKED, ((Node) node)
									.getNode());
							setNodeToActions(((Node) node).getNode());
							GraphTree.this.getTreePop(1).show(GraphTree.this,
									e.getX(), e.getY());
						} else if (node instanceof Edge) {
							coreContext.raise(EDGE_RIGHTCLICKED, ((Edge) node)
									.getEdge());
							setEdgeToActions(((Edge) node).getEdge());
							GraphTree.this.getTreePop(2).show(GraphTree.this,
									e.getX(), e.getY());
						} else if (node instanceof Graph) {
							coreContext.raise(GRAPH_RIGHTCLICKED,
									((Graph) node).getGraph());
							setGraphToActions(((Graph) node).getGraph());
							GraphTree.this.getTreePop(0).show(GraphTree.this,
									e.getX(), e.getY());
						}
					}
				}
			}
		}
	};

	/**
	 * Metoda učitava sve grafove koji postoje u registru i popunjava komponentu
	 * njima.
	 */
	public void initializeGraphs() {
		ArrayList<IGraph> graphs = (ArrayList<IGraph>) this.coreContext
				.getAllGraphs();

		if (graphs != null) {
			for (IGraph g : graphs) {
				this.root.addGraph(new Graph(g));
			}
		}
		this.updateUI();
	}

	/**
	 * Metoda inicijalizuje sve interne akcije ove komponente
	 */
	public void initializeActions() {
		this.nodePropsAction = new NodePropertiesAction(coreContext);
		this.nodeRemoveAction = new NodeRemoveAction(coreContext);
		this.edgePropsAction = new EdgePropertiesAction(coreContext);
		this.edgeRemoveAction = new EdgeRemoveAction(coreContext);
		this.graphPropsAction = new GraphPropertiesAction(coreContext);
		this.graphRemoveAction = new GraphRemoveAction(coreContext);
		this.nodeConnectAction = new NodeConnectAction(coreContext);
		this.newNodeAction = new NewNodeAction(coreContext);
		this.newSubgraphAction = new NewSubgraphAction(coreContext);
		this.help = new HelpAction(coreContext);
	}

	/**
	 * Metoda inicijalizuje sve popup menije za ovu komponentu. Popunjava ih
	 * adekvatnim akcijama.
	 */
	public void initializePopups() {

		treePopNode = new JPopupMenu();
		treePopNode.add(nodeConnectAction);
		treePopNode.add(nodeRemoveAction);
		treePopNode.addSeparator();
		treePopNode.add(nodePropsAction);
		treePopNode.addSeparator();
		treePopNode.add(help);

		treePopGraph = new JPopupMenu();
		JMenuItem newNode2 = new JMenuItem(newNodeAction);
		JMenuItem newSubGraph2 = new JMenuItem(newSubgraphAction);
		JMenu menuNew2 = new JMenu("New...");
		menuNew2.add(newNode2);
		menuNew2.add(newSubGraph2);
		treePopGraph.add(menuNew2);
		treePopGraph.add(graphRemoveAction);
		treePopGraph.addSeparator();
		treePopGraph.add(graphPropsAction);
		treePopGraph.addSeparator();
		treePopGraph.add(help);

		treePopEdge = new JPopupMenu();
		treePopEdge.add(edgeRemoveAction);
		treePopEdge.addSeparator();
		treePopEdge.add(edgePropsAction);
		treePopEdge.addSeparator();
		treePopEdge.add(help);

		treePopSubgraph = new JPopupMenu();
		JMenuItem newNode1 = new JMenuItem(newNodeAction);
		JMenuItem newSubGraph1 = new JMenuItem(newSubgraphAction);
		JMenu menuNew1 = new JMenu("New...");
		menuNew1.add(newNode1);
		menuNew1.add(newSubGraph1);
		treePopSubgraph.add(menuNew1);
		treePopSubgraph.add(nodeConnectAction);
		treePopSubgraph.add(nodeRemoveAction);
		treePopSubgraph.addSeparator();
		treePopSubgraph.add(nodePropsAction);
		treePopSubgraph.addSeparator();
		treePopSubgraph.add(help);

	}

	public JPopupMenu getTreePop(int index) {
		switch (index) {
		case 0:
			return treePopGraph;
		case 1:
			return treePopNode;
		case 2:
			return treePopEdge;
		case 3:
			return treePopSubgraph;
		default:
			return null;
		}
	}

	/**
	 * Metoda setuje svim potrebnim akcijama na popup meniju koji node je
	 * trenutno selektovan i za koji nod bi trebala da se izvrsi pokrenuta
	 * akcija.
	 * 
	 * @param node - nod koji je trenutno selektovan na GraphTree komponenti
	 */
	public void setNodeToActions(INode node) {
		this.nodePropsAction.setNode(node);
		this.nodeRemoveAction.setNode(node);
		this.nodeConnectAction.setNode(node);
	}

	/**
	 * Metoda setuje svim potrebnim akcijama na popup meniju koji edge je
	 * trenutno selektovan i za koji edge bi trebala da se izvrsi pokrenuta
	 * akcija.
	 * 
	 * @param edge - edge koji je trenutno selektovan na GraphTree komponenti
	 */
	public void setEdgeToActions(IEdge edge) {
		this.edgePropsAction.setEdge(edge);
		this.edgeRemoveAction.setEdge(edge);
	}

	/**
	 * Metoda setuje svim potrebnim akcijama na popup meniju koji graph je
	 * trenutno selektovan i za koji graph bi trebala da se izvrsi pokrenuta
	 * akcija.
	 * 
	 * @param graph - graph koji je trenutno selektovan na GraphTree komponenti
	 */
	public void setGraphToActions(IGraph graph) {
		this.graphPropsAction.setGraph(graph);
		this.graphRemoveAction.setGraph(graph);
		this.newNodeAction.setGraph(graph);
		this.newSubgraphAction.setGraph(graph);
	}

	/**
	 * Metoda setuje svim potrebnim akcijama na popup meniju koji subgraph je
	 * trenutno selektovan i za koji subgraph bi trebala da se izvrsi pokrenuta
	 * akcija.
	 * 
	 * @param graph - subgraph koji je trenutno selektovan na GraphTree komponenti
	 */
	public void setSubgraphToActions(IGraph graph) {
		this.newNodeAction.setGraph(graph);
		this.newSubgraphAction.setGraph(graph);
		this.nodePropsAction.setNode(graph);
		this.nodeRemoveAction.setNode(graph);
		this.nodeConnectAction.setNode(graph);
	}
}
