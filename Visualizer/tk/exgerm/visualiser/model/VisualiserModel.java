package tk.exgerm.visualiser.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import tk.exgerm.visualiser.Activator;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.model.nodes.VisNodeDefault;
import tk.exgerm.visualiser.view.VisualiserView;

public class VisualiserModel {

	private VisualiserView view;
	private ICoreContext context;
	private IGraph graph;

	private Random rnd;
	private int nodeCount;
	private Point2D mousePosition;
	private boolean mouseAddedNode;

	private ArrayList<VisNode> visNodes;
	private ArrayList<VisEdge> visEdges;

	private ArrayList<VisNode> selectedNodes;

	private HashMap<String, VisNode> nodeReferences;
	private HashMap<Integer, VisEdge> edgeReferences;

	private ArrayList<Object> searchResults;
	private ArrayList<VisNode> searchNodes;
	private ArrayList<VisEdge> searchEdges;

	// allTheEdgesWithTheSameSourceAndDestinationNodes

	public VisualiserModel(ICoreContext _c) {
		this.context = _c;
		this.rnd = new Random();
		this.visNodes = new ArrayList<VisNode>();
		this.visEdges = new ArrayList<VisEdge>();
		this.nodeReferences = new HashMap<String, VisNode>();
		this.edgeReferences = new HashMap<Integer, VisEdge>();
		this.selectedNodes = new ArrayList<VisNode>();
		this.nodeCount = 0;
		this.searchResults = new ArrayList<Object>();
		this.searchEdges = new ArrayList<VisEdge>();
		this.searchNodes = new ArrayList<VisNode>();
	}

	public void setView(VisualiserView view) {
		this.view = view;
	}

	// ----------------- Getters & Setters -----------------//

	public void setSearchResults(ArrayList<Object> searchResults) {
		this.searchResults = searchResults;
	}

	public ArrayList<VisNode> getSearchNodes() {
		return searchNodes;
	}

	public ArrayList<VisEdge> getSearchEdges() {
		return searchEdges;
	}

	public ICoreContext getContext() {
		return this.context;
	}

	public ArrayList<Object> getSearchResults() {
		return searchResults;
	}

	public ArrayList<VisNode> getSelectedNodes() {
		return selectedNodes;
	}

	public HashMap<String, VisNode> getNodeReferences() {
		return nodeReferences;
	}

	public HashMap<Integer, VisEdge> getEdgeReferences() {
		return edgeReferences;
	}

	public ArrayList<VisNode> getVisNodes() {
		return visNodes;
	}

	public ArrayList<VisEdge> getVisEdges() {
		return visEdges;
	}

	public VisNode getNodeAt(int i) {
		return (VisNode) visNodes.get(i);
	}

	public IGraph getGraph() {
		return graph;
	}

	public void setGraph(IGraph graph) {
		this.graph = graph;
	}

	// ----------------- Model Functions -----------------//

	/**
	 * Služi za dodavanje vizuelne predstave stvarnog node-a.
	 * 
	 * @param nodeName
	 *            - ime čvora koji se predstavlja
	 */
	public void addNode(String nodeName, boolean isSubGraph, String finalRoot,
			int level) {
		VisNode n;

		if (!mouseAddedNode) {
			n = VisNodeDefault.createDefault((Point2D) new Point(rnd
					.nextInt(4000) - 2000, rnd.nextInt(4000) - 2000), 0);
		} else {
			n = VisNodeDefault.createDefault(mousePosition, 0);
		}

		n.setName(nodeName);
		n.setFinalRoot(finalRoot);
		n.setSubGraph(isSubGraph);
		n.setLevel(level);

		visNodes.add(n);
		nodeReferences.put(nodeName, n);
		nodeCount++;

		removeSearchResults();

		view.repaint();
	}

	/**
	 * Služi za dodavanje vizuelne predstave stvarnog edge-a.
	 * 
	 * @param sourceName
	 *            - ime početnog čvora
	 * @param destName
	 *            - ime krajnjeg čvora
	 * @param id
	 *            - stvarni identifikator veze
	 * @param directed
	 *            - da li je veza usmerena
	 */
	public void addEdge(String sourceName, String destName, int id,
			boolean directed) {
		VisEdge e = new VisEdge(new BasicStroke(10.0f), new Color(70, 70, 255),
				nodeReferences.get(sourceName), nodeReferences.get(destName));
		e.setDirected(directed);
		e.setId(id);

		visEdges.add(e);
		edgeReferences.put(id, e);

		removeSearchResults();

		setEdgeDivs(e, 0);

		view.repaint();
	}

	/**
	 * Uklanja vizelnu predstavu veze iz visualisera.
	 * 
	 * @param id
	 *            - identifikator veze
	 */
	public void removeEdge(int id) {
		VisEdge edge = edgeReferences.get(id);

		visEdges.remove(edge);
		edgeReferences.remove(id);

		removeSearchResults();

		if (edge != null)
			setEdgeDivs(edge, -1);

		view.repaint();
	}

	/**
	 * Uklanja vizelnu predstavu čvora iz visualisera.
	 * 
	 * @param nodeName
	 */
	public void removeNode(String nodeName) {
		VisNode node = nodeReferences.get(nodeName);

		visNodes.remove(node);
		nodeReferences.remove(nodeName);

		removeSearchResults();

		view.repaint();
	}

	/**
	 * Preko kontexta briše stvarnu vezu u grafu.
	 * 
	 * @param e
	 *            - veza koje se briše
	 */
	public void deleteEdge(VisEdge e) {
		IEdge edge = graph.getEdge(e.getId());

		graph.removeEdge(edge);
	}

	/**
	 * Preko kontexta briše stvarni čvor u grafu.
	 * 
	 * @param n
	 *            - čvor koji se briše
	 */
	public void deleteNode(VisNode n) {
		INode node = graph.getNode(n.getName());

		try {
			graph.removeNode(node);
		} catch (ExGNodeDoesNotExsistException e) {

		}
	}

	/**
	 * Preko kontexta dodaje stvarni čvor u grafu.
	 * 
	 * @param pos
	 *            - pozicija koja će biti iskorišćena za pozicioniranje
	 *            vizuelnog čvora
	 */
	public void newNode(Point2D pos) {
		INode node = context.newNode("Node " + Integer.toString(nodeCount + 1));

		mousePosition = pos;
		mouseAddedNode = true;

		try {
			graph.addNode(node);
		} catch (ExGNodeAlreadyExsistException e) {
			mouseAddedNode = false;
		}
	}

	/**
	 * Preko kontexta dodaje stvarnu vezu u grafu. Dodaje uvek običnu
	 * (neusmerenu) vezu
	 * 
	 * @param n1
	 *            - početni čvor
	 * @param n2
	 *            - krajnji čvor
	 */
	public void newEdge(VisNode n1, VisNode n2, boolean isDirected) {
		INode source = graph.getNode(n1.getName());
		INode dest = graph.getNode(n2.getName());

		try {
			graph.addEdge(source, dest, isDirected);
		} catch (ExGNodeDoesNotExsistException e) {

		}
	}

	/**
	 * Prilagođena za korišćenje erasera. Proverava da li pogođena vizelna veza
	 * i briše stvarn vezu iz grafa.
	 * 
	 * @param point
	 *            - koordinata (trenutna pozicija miša)
	 */
	public void eraseEdge(Point2D point) {
		for (VisEdge e : visEdges) {

			if (e.getPainter().isElementAt(point)) {
				deleteEdge(e);
				return;
			}

			Line2D line = new Line2D.Double(e.getSource().getPosition().getX()
					+ (int) (e.getSource().getSize().width / 2), e.getSource()
					.getPosition().getY()
					+ (int) (e.getSource().getSize().height / 2), e
					.getDestination().getPosition().getX()
					+ (int) (e.getDestination().getSize().width / 2), e
					.getDestination().getPosition().getY()
					+ (int) (e.getDestination().getSize().height / 2));

			if (isBetween(line, point)) {
				if (line.ptLineDist(point) < 15.0) {
					deleteEdge(e);
					return;
				}
			}
		}
	}

	public VisEdge cathcEdge(Point2D point) {
		for (VisEdge e : visEdges) {

			if (e.getPainter().isElementAt(point)) {
				return e;
			}

			Line2D line = new Line2D.Double(e.getSource().getPosition().getX()
					+ (int) (e.getSource().getSize().width / 2), e.getSource()
					.getPosition().getY()
					+ (int) (e.getSource().getSize().height / 2), e
					.getDestination().getPosition().getX()
					+ (int) (e.getDestination().getSize().width / 2), e
					.getDestination().getPosition().getY()
					+ (int) (e.getDestination().getSize().height / 2));

			if (isBetween(line, point)) {
				if (line.ptLineDist(point) < 15.0) {
					return e;
				}
			}
		}
		return null;
	}

	public boolean isBetween(Line2D l, Point2D p) {
		double x1, x2, y1, y2;
		if (l.getX1() <= l.getX2()) {
			x1 = l.getX1();
			x2 = l.getX2();
		} else {
			x1 = l.getX2();
			x2 = l.getX1();
		}
		if (l.getY1() <= l.getY2()) {
			y1 = l.getY1();
			y2 = l.getY2();
		} else {
			y1 = l.getY2();
			y2 = l.getY1();
		}
		if (x1 > p.getX())
			return false;
		if (x2 < p.getX())
			return false;
		if (y1 > p.getY())
			return false;
		if (y2 < p.getY())
			return false;
		return true;
	}

	/**
	 * Proverava šta se nalazi ispod tačke point. Ako je tu čvor, vraća njegov
	 * index u listi, ako nije, vraća -1.
	 * 
	 * @param point
	 *            - koordinata (trenutna pozicija miša)
	 * @return - index u listi ili -1
	 */
	public int getNodeAtPosition(Point2D point) {
		for (int i = visNodes.size() - 1; i >= 0; i--) {
			VisNode node = getNodeAt(i);
			if (node.getPainter().isElementAt(point)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Proverava šta se nalazi ispod tačke point. Ako je tu čvor, selektuje ga i
	 * vraća njegov index u listi, ako nije, vraća -1.
	 * 
	 * @param point
	 *            - koordinata (trenutna pozicija miša)
	 * @return - index u listi ili -1
	 */
	public int selectNodeAtPosition(Point2D point) {
		for (int i = visNodes.size() - 1; i >= 0; i--) {
			VisNode node = getNodeAt(i);
			if (node.getPainter().isElementAt(point)) {
				if (!selectedNodes.contains(node)) {
					selectedNodes.add(node);
					node.setPaint(new GradientPaint(0, 0,
							new Color(128, 0, 128), 100, 0, new Color(70, 70,
									255)));
					view.repaint();
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Vraća difoltnu boju čvoru
	 * 
	 * @param node
	 *            - čvor
	 */
	public void setDefaultNodePaint(VisNode node) {
		node.setPaint(node.getNodeDefaultPaint());
	}

	/**
	 * Deselektuje sve čvorove
	 */
	public void deselectAllNodes() {
		for (VisNode n : selectedNodes) {
			setDefaultNodePaint(n);
		}
		setPositions(); // TODO naci bolje resenje za ovo...
		selectedNodes.clear();
		view.repaint();
	}

	/**
	 * Selektuje konkretni čvor i vraća potvrdu uspešnosti
	 * 
	 * @param node
	 *            - čvor
	 * @return - true ako je selektovanje uspelo, odnosno false
	 */
	public boolean selectNode(VisNode node) {
		if (!selectedNodes.contains(node)) {
			selectedNodes.add(node);
			node.setPaint(new GradientPaint(0, 0, new Color(128, 0, 128), 100,
					0, new Color(70, 70, 255)));
			view.repaint();
			return true;
		}
		return false;
	}

	/**
	 * Deselektuje konkretni čvor i vraća potvrdu uspešnosti
	 * 
	 * @param node
	 *            - čvor
	 * @return - true ako je selektovanje uspelo, odnosno false
	 */
	public boolean deselectNode(VisNode node) {
		if (selectedNodes.contains(node)) {
			setDefaultNodePaint(node);
			selectedNodes.remove(node);
			view.repaint();
			return true;
		}
		return false;
	}

	public void handleSearch() {
		if (searchResults.size() != 0) {
			searchNodes.clear();
			searchEdges.clear();
			for (Object o : searchResults) {
				if (o instanceof INode) {
					searchNodes.add(nodeReferences.get(((INode) o).getName()));
				}
				if (o instanceof IEdge) {
					searchEdges.add(edgeReferences.get(((IEdge) o).getID()));
				}
			}
			if (searchEdges.size() == 0)
				handleSearchEdges();
		}
	}

	private void handleSearchEdges() {
		VisNode n1;
		VisNode n2;

		for (int i = 0; i < searchNodes.size() - 1; i++) {
			n1 = searchNodes.get(i);
			n2 = searchNodes.get(i + 1);

			for (VisEdge e : visEdges) {
				if (((e.getSource() == n1) && (e.getDestination() == n2))
						|| ((e.getSource() == n2) && (e.getDestination() == n1))) {
					searchEdges.add(e);
				}
			}

		}
	}

	public void removeSearchResults() {
		searchResults.clear();
		searchNodes.clear();
		searchEdges.clear();
		view.setDrawSearchResults(false);
	}

	public void updateNodePosition(int node, Point2D newPosition) {
		getNodeAt(node).setPosition(newPosition);
	}

	public void updateNodePosition(VisNode node, Point2D newPosition) {
		node.setPosition(newPosition);
	}

	// ----------------- Force-Based Visualisation Functions -----------------//

	/**
	 * Gravitaciona konstanta
	 */
	double G = 6.673E-11;

	/**
	 * Konstanta elasticnosti
	 */
	double k = 0.1;

	/**
	 * Optimalna duzina opruge (veze medju nodovima)
	 */
	double optimumSpring = 150;

	/**
	 * Pošto su mase nodova konstante, ova veličina je uvek ista
	 */
	static final double gravityConst = (6.673E-11) * 2100000 * 2100000;

	/**
	 * Glavna radna funkcija - za svaki cvor i za svaki drugi cvor u odnosu na
	 * tekuci racuna sile i azurira pozicije cvorova
	 */
	public void inLoop() {
		for (VisNode n : visNodes) {

			if (!selectedNodes.contains(n)) {
				// for each other node
				for (VisNode other_n : visNodes) {
					if (other_n != n) {
						moveNodeUsingForce(n, other_n, (-1)
								* gravity(n, other_n));
					}
				}
				// for each link connected to this node
				for (VisEdge e : visEdges) {
					if (e.getSource() == n) {
						moveNodeUsingForce(e.getSource(), e.getDestination(),
								hookeAttraction(n, e));
					} else if (e.getDestination() == n) {
						moveNodeUsingForce(e.getDestination(), e.getSource(),
								hookeAttraction(n, e));
					}
				}
			}
			renewPositions();
		}
	}

	/**
	 * Reprazentacija Njutnovog zakona gravitacije
	 * 
	 * @param thisNode
	 *            - tekuci cvor
	 * @param otherNode
	 *            - drugi cvor od koga se racuna sila
	 * @return - intenzitet sile
	 */
	public double gravity(VisNode thisNode, VisNode otherNode) {
		// F = G*m1*m2/r^2
		double r = nodeNodeDistance(thisNode, otherNode) / 100;
		// return G*thisNode.mass*otherNode.mass/(r*r);
		return gravityConst / (r * r);
	}

	/**
	 * Reprazentacija Hukovog zakona
	 * 
	 * @param thisNode
	 *            - tekuci cvor
	 * @param otherNode
	 *            - drugi cvor od koga se racuna sila
	 * @return - intenzitet sile
	 */
	public double hookeAttraction(VisNode thisNode, VisEdge spring) {
		// F = -kx
		double x = optimumSpring - spring.Length();
		double force = k * Math.abs(x) / spring.getDiv();
		if (x > 0)
			return (-1) * force;
		if (x < 0)
			return force;
		if (x == 0)
			return 0;

		return 0;
	}

	/**
	 * Racuna razmak izmedju cvorova
	 * 
	 * @param n1
	 *            - cvor 1
	 * @param n2
	 *            - cvor 2
	 * @return - udaljenost
	 */
	public double nodeNodeDistance(VisNode n1, VisNode n2) {
		Point p1 = (Point) n1.getPosition();
		Point p2 = (Point) n2.getPosition();

		double x1 = p1.x;
		double y1 = p1.y;

		double x2 = p2.x;
		double y2 = p2.y;

		double x = x1 - x2;
		double y = y1 - y2;

		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Izjednacava trenutne i nove pozicije cvorova (neophodno za sada)
	 */
	public void setPositions() {
		for (VisNode n : visNodes) {
			if (selectedNodes.contains(n)) {
				Point newPos = new Point();
				newPos = (Point) n.getPosition();
				n.setPositionNew(newPos);
			}
		}
	}

	/**
	 * Postavlja nove pozicije cvorova (sacuvane u cvoru u newPosition)
	 */
	public void renewPositions() {
		for (VisNode n : visNodes) {
			if (!selectedNodes.contains(n)) {
				Point newPos = new Point();
				newPos = (Point) n.getPositionNew();
				n.setPosition(newPos);
			}
		}
	}

	/**
	 * Racuna gde ce biti pomeren tekuci cvor silom drugog cvora i rezultat
	 * smesta u newPosition cvora
	 * 
	 * @param thisNode
	 *            - tekuci cvor
	 * @param otherNode
	 *            - drugi cvor
	 * @param force
	 *            - sila
	 */
	public double moveNodeUsingForce(VisNode thisNode, VisNode otherNode,
			double force) {
		Point p1 = (Point) thisNode.getPosition();
		Point p2 = (Point) otherNode.getPosition();

		double x1 = p1.x;
		double y1 = p1.y;

		double x2 = p2.x;
		double y2 = p2.y;

		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);

		double alpha = Math.atan(x / y);

		Point newPos = new Point();

		if ((thisNode.getPosition().getX() < otherNode.getPosition().getX())
				&& (thisNode.getPosition().getY() < otherNode.getPosition()
						.getY())) {

			newPos.x = (int) (thisNode.getPositionNew().getX() + force
					* Math.sin(alpha));
			newPos.y = (int) (thisNode.getPositionNew().getY() + force
					* Math.cos(alpha));

			thisNode.setPositionNew(newPos);
		}
		if ((thisNode.getPosition().getX() > otherNode.getPosition().getX())
				&& (thisNode.getPosition().getY() > otherNode.getPosition()
						.getY())) {

			newPos.x = (int) (thisNode.getPositionNew().getX() - force
					* Math.sin(alpha));
			newPos.y = (int) (thisNode.getPositionNew().getY() - force
					* Math.cos(alpha));

			thisNode.setPositionNew(newPos);
		}
		if ((thisNode.getPosition().getX() < otherNode.getPosition().getX())
				&& (thisNode.getPosition().getY() > otherNode.getPosition()
						.getY())) {

			newPos.x = (int) (thisNode.getPositionNew().getX() + force
					* Math.sin(alpha));
			newPos.y = (int) (thisNode.getPositionNew().getY() - force
					* Math.cos(alpha));

			thisNode.setPositionNew(newPos);
		}
		if ((thisNode.getPosition().getX() > otherNode.getPosition().getX())
				&& (thisNode.getPosition().getY() < otherNode.getPosition()
						.getY())) {

			newPos.x = (int) (thisNode.getPositionNew().getX() - force
					* Math.sin(alpha));
			newPos.y = (int) (thisNode.getPositionNew().getY() + force
					* Math.cos(alpha));

			thisNode.setPositionNew(newPos);
		}

		if (thisNode.getPosition().getX() == otherNode.getPosition().getX()) {
			if (thisNode.getPosition().getY() < otherNode.getPosition().getY()) {
				newPos.x = (int) thisNode.getPositionNew().getX();
				newPos.y = (int) (thisNode.getPositionNew().getY() + force);

				thisNode.setPositionNew(newPos);
			}
			if (thisNode.getPosition().getY() > otherNode.getPosition().getY()) {
				newPos.x = (int) thisNode.getPositionNew().getX();
				newPos.y = (int) (thisNode.getPositionNew().getY() - force);

				thisNode.setPositionNew(newPos);
			}
		}

		if (thisNode.getPosition().getY() == otherNode.getPosition().getY()) {
			if (thisNode.getPosition().getX() < otherNode.getPosition().getX()) {
				newPos.x = (int) (thisNode.getPositionNew().getX() + force);
				newPos.y = (int) thisNode.getPositionNew().getY();

				thisNode.setPositionNew(newPos);
			}
			if (thisNode.getPosition().getX() > otherNode.getPosition().getX()) {
				newPos.x = (int) (thisNode.getPositionNew().getX() - force);
				newPos.y = (int) thisNode.getPositionNew().getY();

				thisNode.setPositionNew(newPos);
			}
		}
		return force;
	}

	/**
	 * Umanjuje silu opruga (veza) koje su izmedju istih nodova
	 * 
	 * @param edge
	 *            - dadata ili sklonjena veza
	 * @param remove
	 *            - proslediti 0 ako je dodata, ili -1 ako je veza sklonjena
	 */
	public void setEdgeDivs(VisEdge edge, int remove) {
		ArrayList<VisEdge> temp = new ArrayList<VisEdge>();
		edge.setDiv(1);

		for (VisEdge e : visEdges) {
			if (((edge.getSource() == e.getSource()) && (edge.getDestination() == e
					.getDestination()))
					|| ((edge.getSource() == e.getDestination()) && (edge
							.getDestination() == e.getSource()))) {
				edge.raiseDiv();
				temp.add(e);
			}
		}

		for (VisEdge e : temp) {
			e.setDiv(edge.getDiv() + remove);
		}

	}

	/**
	 * Zahteva od Core-a da prikaže traženi help
	 * 
	 * @param help
	 *            Help koji treba prikazati
	 */
	public void showHelp(String help) {
		context.raise(ExGHelp.HELP_REQUESTED, Activator.name, help);
	}
}
