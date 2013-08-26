package tk.exgerm.core.impl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.Core;
import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeNotConnectedException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IVisitor;

public class Node implements INode {

	/**
	 * Ime noda. Svaki nod mora da ima ime.
	 */
	protected String name;

	/**
	 * Mapa svih atributa koje ima čvor.
	 */
	protected Map<String, Object> attributes;

	/**
	 * Lista svih grana koje su nakačene na čvor.
	 */
	protected List<IEdge> connectedEdges;

	/**
	 * Graf kome pripada čvor.
	 */
	protected IGraph graph;

	/**
	 * Podaci koje različiti delovi programa mogu da ostavljaju jedni drugim.
	 */
	protected Map<String, Object> data;

	public Node(String name) {
		this.name = name;
		this.attributes = new HashMap<String, Object>();
		this.connectedEdges = new ArrayList<IEdge>();
		this.data = new HashMap<String, Object>();
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void setAttribute(String attr, Object value) {
		Object o = attributes.get(attr);
		attributes.put(attr, value);
		if (o == null)
			if (this instanceof IGraph)
				Core.getInstance().getEventDispatcher().raiseEvent(
						IGraph.GRAPH_ATTRIBUTE_ADDED, this, attr);
			else
				Core.getInstance().getEventDispatcher().raiseEvent(
						INode.NODE_ATTRIBUTE_ADDED, this, attr);
		else if (this instanceof IGraph)
			Core.getInstance().getEventDispatcher().raiseEvent(
					IGraph.GRAPH_ATTRIBUTE_CHANGED, this, attr, o);
		else
			Core.getInstance().getEventDispatcher().raiseEvent(
					INode.NODE_ATTRIBUTE_CHANGED, this, attr, o);
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
	public void removeAttribute(String attr) {
		Object o = attributes.get(attr);
		attributes.remove(attr);
		if (this instanceof IGraph)
			Core.getInstance().getEventDispatcher().raiseEvent(
					IGraph.GRAPH_ATTRIBUTE_REMOVED, this, attr, o);
		else
			Core.getInstance().getEventDispatcher().raiseEvent(
					INode.NODE_ATTRIBUTE_REMOVED, this, attr, o);
	}

	@Override
	public void link(IEdge edge) throws ExGNodeNotConnectedException {
		if (edge.getTo() != this && edge.getFrom() != this)
			throw new ExGNodeNotConnectedException(
					"Node is not connected to required edge.");
		connectedEdges.add(edge);
		Core.getInstance().getEventDispatcher().raiseEvent(INode.NODE_LINKED,
				this, edge);
	}

	@Override
	public IEdge link(INode node) throws ExGNodeDoesNotExsistException {
		IEdge edge = new Edge(this, node);
		Core.getInstance().getEventDispatcher().raiseEvent(INode.NODE_LINKED,
				this, edge);
		return edge;
	}

	@Override
	public void unlink(IEdge edge) {
		this.connectedEdges.remove(edge);
		Core.getInstance().getEventDispatcher().raiseEvent(INode.NODE_UNLINKED,
				this, edge);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) throws ExGNameConflictException {
		String oldName = this.name;
		// ako nod jos uvek nije u grafu, nema sta da proveravamo
		if (this.graph == null) {
			this.name = newName;
		} else {
			IGraph g = getGraph();
			if (g.getNode(newName) != null)
				throw new ExGNodeAlreadyExsistException(
						"Unable to change name, because node with name "
								+ newName + " already exists in graph"
								+ getName());
			else {
				this.name = newName;
				((Graph) getGraph()).renameNode(this, oldName);
			}
		}
		Core.getInstance().getEventDispatcher().raiseEvent(
				INode.NODE_NAME_CHANGED, this, oldName);
	}

	@Override
	public IGraph getGraph() {
		return graph;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.graph = graph;
	}

	@Override
	public List<IEdge> getConnectedEdges() {
		return this.connectedEdges;
	}

	public boolean isChildOf(IGraph graph) {
		INode node = this.getGraph();
		while (node != null) {
			if (node == graph)
				return true;
			node = node.getGraph();
		}
		return false;
	}

	@Override
	public IGraph getFinalRoot() {
		// ako nema nadgrafa, onda sam ja final root
		if (getGraph() == null)
			return (IGraph) this;
		
		IGraph root = getGraph();
		while (root.getGraph() != null) {
			root = root.getGraph();
		}
		return root;
	}

	@Override
	public int getLevel() {
		if (getGraph() == null)
			return 0;
		else {
			int counter = 1;
			INode root = getGraph();
			while ((root = root.getGraph()) != null)
				counter++;
			return counter;
		}
	}

	@Override
	public String toString() {
		return "Node: " + getName();
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
