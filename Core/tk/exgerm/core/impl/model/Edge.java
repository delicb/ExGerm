package tk.exgerm.core.impl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.Core;
import tk.exgerm.core.exception.ExGNodeNotConnectedException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IVisitor;

/**
 * <p>
 * Predstavlja vezu između dva {@link tk.exgerm.core.model.INode noda}
 * <p>
 * Za više detalja pogledati dokumentaciju {@link tk.exgerm.core.model.IEdge
 * interfejsa} koji implementira.
 * 
 * @author Tim 2
 * 
 */
public class Edge implements IEdge {

	/**
	 * Mapa svih atributa koje ima grana.
	 */
	private Map<String, Object> attributes;

	/**
	 * Izvorni čvor.
	 */
	private INode from;

	/**
	 * Odredišni čvor.
	 */
	private INode to;

	/**
	 * Da li je grana usmerena
	 */
	private boolean directed;

	/**
	 * ID grane
	 */
	private int ID;

	/**
	 * Graf kome pripada.
	 */
	private IGraph graph;
	
	/**
	 * Podaci koje različiti delovi programa mogu da ostavljaju jedni drugim.
	 */
	protected Map<String, Object> data;

	@Override
	public int getID() {
		return this.ID;
	}

	public Edge(INode from, INode to) {
		this.from = from;
		this.to = to;
		try {
			from.link(this);
			to.link(this);
		} catch (ExGNodeNotConnectedException e) {
			// Ne bi trebalo da se desi
			e.printStackTrace();
		}
		this.attributes = new HashMap<String, Object>();
		this.data = new HashMap<String, Object>();
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Map<String, Object> getAllAttributes() {
		return attributes;
	}

	@Override
	public Object getAttribute(String attr) {
		return attributes.get(attr);
	}

	@Override
	public void setAttribute(String attr, Object value) {
		Object o = attributes.get(attr);
		attributes.put(attr, value);
		if (o == null)
			Core.getInstance().getEventDispatcher().raiseEvent(
					IEdge.EDGE_ATTRIBUTE_ADDED, this, attr);
		else
			Core.getInstance().getEventDispatcher().raiseEvent(
					IEdge.EDGE_ATTRIBUTE_CHANGED, this, attr, o);
	}

	@Override
	public void removeAttribute(String attr) {
		Object o = attributes.get(attr);
		attributes.remove(attr);
		Core.getInstance().getEventDispatcher().raiseEvent(
				IEdge.EDGE_ATTRIBUTE_REMOVED, this, attr, o);
	}

	@Override
	public INode getTo() {
		return to;
	}

	@Override
	public INode getFrom() {
		return from;
	}

	@Override
	public List<INode> getNodes() {
		ArrayList<INode> nodes = new ArrayList<INode>();
		nodes.add(from);
		nodes.add(to);
		return nodes;
	}

	@Override
	public IGraph getGraph() {
		return this.graph;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.graph = graph;
	}

	@Override
	public boolean isDirected() {
		return this.directed;
	}

	@Override
	public void setDirected(boolean directed) {
		this.directed = directed;
		Core.getInstance().getEventDispatcher().raiseEvent(
				IEdge.EDGE_DIRECTED_CHANGED, this);
	}

	@Override
	public String toString() {
		return "Edge: " + from.getName() + (directed ? "->" : "--")
				+ to.getName();
	}

	/**
	 * Pošto grani može da se dodeli ID tek kad se uvezuje u neki graf, u
	 * konstruktoru to ne može da se uradi. Zbog toga postoji ova metoda da
	 * setuje ID.
	 * 
	 * Metoda nije javna i ne može da joj se pristupi preko {@link IEdge
	 * interfejsa}, tako da ne bi trebalo da bude problema sa klijentima koji
	 * ručno pokušavaju ID da postave. ID i dalje ostaje readonly za sve koji
	 * koriste model.
	 */
	protected void setID(int ID) {
		this.ID = ID;
	}

	@Override
	public Object getData(String key) {
		return this.data.get(key);
	}

	@Override
	public void setData(String key, Object value) {
		this.data.put(key, value);
	}
}
