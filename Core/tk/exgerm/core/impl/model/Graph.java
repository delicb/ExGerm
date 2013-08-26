package tk.exgerm.core.impl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.Core;
import tk.exgerm.core.exception.ExGEdgeDoesNotExsistException;
import tk.exgerm.core.exception.ExGIteratorDoesNotExsistException;
import tk.exgerm.core.exception.ExGNameConflictException;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeNotConnectedException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.register.GraphRegisterProxy;

public class Graph extends Node implements IGraph {

	/**
	 * Mapa svih nodova koji su u sastavu ovog grafa.
	 */
	private Map<String, INode> nodes;

	/**
	 * Mapa svih grana koje su u sastavu ovog grafa.
	 */
	private Map<Integer, IEdge> edges;

	/**
	 * Generator ID-jeva za grane.
	 */
	private IDGenerator idGenerator;

	public Graph(String name) {
		super(name);
		this.nodes = new HashMap<String, INode>();
		this.edges = new HashMap<Integer, IEdge>();
		idGenerator = new IDGenerator();
	}

	@Override
	public IEdge addEdge(INode from, INode to, boolean directed)
			throws ExGNodeDoesNotExsistException {
		if (!nodes.containsKey(from.getName())
				|| !nodes.containsKey(to.getName())) {
			throw new ExGNodeDoesNotExsistException();
		} else {
			Edge edge = new Edge(from, to);
			edge.setID(idGenerator.generateID());
			edge.setDirected(directed);
			edge.setGraph(this);
			edges.put(edge.getID(), edge);
			Core.getInstance().getEventDispatcher().raiseEvent(
					IGraph.GRAPH_EDGE_ADDED, this, edge);
			return edge;
		}
	}

	@Override
	public void addNode(INode node) throws ExGNodeAlreadyExsistException {
		if (nodes.containsKey(node.getName()))
			throw new ExGNodeAlreadyExsistException();
		else {
			node.setGraph(this);
			this.nodes.put(node.getName(), node);
			Core.getInstance().getEventDispatcher().raiseEvent(
					IGraph.GRAPH_NODE_ADDED, this, node);
		}
	}

	@Override
	public int getEdgeCount() {
		return this.edges.size();
	}

	@Override
	public int getNodeCount() {
		return this.nodes.size();
	}

	@Override
	public void removeNode(INode node) throws ExGNodeDoesNotExsistException {
		if (node == null)
			throw new ExGNodeDoesNotExsistException();
		// pravimo novu listu zbog ConcurrentModificationException
		List<IEdge> edges = new ArrayList<IEdge>(node.getConnectedEdges());
		Iterator<IEdge> it = edges.iterator();
		while (it.hasNext()) {
			removeEdge(it.next());
		}
		this.nodes.remove(node.getName());
		Core.getInstance().getEventDispatcher().raiseEvent(
				IGraph.GRAPH_NODE_REMOVED, this, node);
	}

	@Override
	public void removeNode(String node) throws ExGNodeDoesNotExsistException {
		removeNode(this.nodes.get(node));
	}

	@Override
	public void addEdge(IEdge edge) {
		try {
			if (!edge.getFrom().getConnectedEdges().contains(edge))
				edge.getFrom().link(edge);
			if (!edge.getTo().getConnectedEdges().contains(edge))
				edge.getTo().link(edge);
		} catch (ExGNodeNotConnectedException e) {
			// Ne bi trebalo da se desi
			e.printStackTrace();
		}
		edge.setGraph(this);
		((Edge) edge).setID(idGenerator.generateID());
		this.edges.put(edge.getID(), edge);
		Core.getInstance().getEventDispatcher().raiseEvent(
				IGraph.GRAPH_EDGE_ADDED, this, edge);
	}

	@Override
	public void removeEdge(IEdge edge) {
		this.edges.remove(edge.getID());
		if (edge.getFrom().getConnectedEdges().contains(edge))
			edge.getFrom().unlink(edge);
		if (edge.getTo().getConnectedEdges().contains(edge))
			edge.getTo().unlink(edge);
		Core.getInstance().getEventDispatcher().raiseEvent(
				IGraph.GRAPH_EDGE_REMOVED, this, edge);
	}

	@Override
	public void removeEdge(int ID) throws ExGEdgeDoesNotExsistException {
		if (!edges.containsKey(ID))
			throw new ExGEdgeDoesNotExsistException("Edge with ID " + ID
					+ "does not exist in graph " + getName());
		IEdge e = edges.get(ID);
		removeEdge(e);
	}

	@Override
	public void setName(String newName) throws ExGNameConflictException {
		GraphRegisterProxy grp = Core.getInstance().getServiceRegister()
				.getGraphRegisterProxy();
		if (getGraph() == null) {
			if (grp.getGraph(newName) != null) {
				throw new ExGNameConflictException(
						"Unable to change name, because graph with name "
								+ newName
								+ " already exsists in Graph Registry");
			} else {
				String oldName = this.name;

				this.name = newName;
				grp.renameGraph(this, oldName);

				Core.getInstance().getEventDispatcher().raiseEvent(
						IGraph.GRAPH_NAME_CHANGED, this, oldName);
			}
		} else {
			IGraph g = getGraph();
			if (g.getNode(newName) != null) {
				throw new ExGNameConflictException(
						"Unable to change name, because node with name "
								+ newName + " already exsists in garph"
								+ getGraph().getName());
			} else {
				String oldName = this.name;
				this.name = newName;
				((Graph) g).renameNode(this, oldName);
				Core.getInstance().getEventDispatcher().raiseEvent(
						IGraph.GRAPH_NAME_CHANGED, this, oldName);
			}
		}
	}

	@Override
	public List<IEdge> getAllEdges() {
		return new ArrayList<IEdge>(edges.values());
	}

	@Override
	public List<INode> getAllNodes() {
		return new ArrayList<INode>(nodes.values());
	}

	@Override
	public INode getNode(String name) {
		return nodes.get(name);
	}

	@Override
	public IEdge getEdge(int ID) {
		return edges.get(ID);
	}

	@Override
	public String toString() {
		return "Graph: " + getName();
	}

	@Override
	public ExGIterator getIterator(String name)
			throws ExGIteratorDoesNotExsistException {
		ExGIterator it = Core.getInstance().getServiceRegister()
				.getIteratorRegister().getIterator(name);
		it.setGraph(this);
		return it;
	}

	protected void renameNode(INode node, String oldName) {
		if (nodes.values().contains(node)) {
			nodes.remove(oldName);
			nodes.put(node.getName(), node);
		}
	}

}
