package tk.exgerm.core.impl.service;

import java.util.ArrayList;
import java.util.Stack;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;

/**
 *Iterator koji kroz graf prolazi principom <em>prvi u dubinu</em>.</br>
 *
 * <em>Dodatak: posle ovog iteratora je neophodno potrebno zvati {@link tk.exgerm.core.plugin.ExGIterator#clear() clear()} metodu</em>
 */
public class DFIterator implements ExGIterator {

	/**
	 * {@link tk.exgerm.core.model.IGraph Graf} kroz koji se trenutno iterira
	 */
	private IGraph graph;

	/**
	 * {@link tk.exgerm.core.model.INode Node} koji je postavljen kao početak
	 * iteracije
	 */
	private INode start;

	/**
	 * Da li iterator uzima u obzir usmerenje grana.
	 */
	private boolean respectEdgeDirection = false;

	Stack<INode> stack = new Stack<INode>();

	ArrayList<INode> done = new ArrayList<INode>();

	private boolean started = false, firstDone = false;

	@Override
	public String getName() {
		return ExGIterator.DEPTH_FIRST;
	}

	@Override
	public boolean hasNext() {
		started = true;
		// ako se ne postuje onda mora da prodje krzo sve
		if (!respectEdgeDirection){
			return !(!(done.size() < graph.getAllNodes().size()) && stack.isEmpty());
		// ako se postuje prolazi samo kroz povezane
		}else{
			if(firstDone){
				return !stack.isEmpty();
			}
			else{
				if(start == null){
					return graph.getAllNodes().size()!=0;
				}
				else{
					return true;
				}
			}
		}
	}

	@Override
	public INode next() {
		started = true;
		if (!hasNext())
			return null;
		/*
		 * ima vishe slucajeva treba na sve obratiti paznju
		 */
		if (firstDone == false) {
			if (start == null)
				start = graph.getAllNodes().get(0);
			firstDone = true;
			stack.push(start);
			done.add(start);
		}

		if (stack.isEmpty() && done.size() < graph.getAllNodes().size()
				&& firstDone && !respectEdgeDirection) {
			// trazim prvi nepovezani i ubacujemo ga u queue
			for (INode node : graph.getAllNodes()) {
				if (!done.contains(node)) {
					stack.push(node);
					done.add(node);
					break;
				}

			}
		}

		INode current = stack.pop();

		if (respectEdgeDirection) { // pazi se na usmerenje
			for (IEdge edge : current.getConnectedEdges()) {
				if (edge.isDirected()) { // usmerene grane
					if (edge.getFrom() == current
							&& !done.contains(edge.getTo())) {
						edge.getTo().setData(PARENT, current);
						stack.push(edge.getTo());
						done.add(edge.getTo());
					}
				} else { // neusmerene grane
					if (edge.getFrom() == current
							&& !done.contains(edge.getTo())) {
						edge.getTo().setData(PARENT, current);
						stack.push(edge.getTo());
						done.add(edge.getTo());
					}
					if (edge.getTo() == current
							&& !done.contains(edge.getFrom())) {
						edge.getFrom().setData(PARENT, current);
						stack.push(edge.getFrom());
						done.add(edge.getFrom());
					}
				}
			}
		} else { // ne pazi se -> dodajemo sve...
			for (IEdge edge : current.getConnectedEdges()) {
				if (edge.getFrom() == current
						&& !done.contains(edge.getTo())) {
					edge.getTo().setData(PARENT, current);
					stack.push(edge.getTo());
					done.add(edge.getTo());
				}
				if (edge.getTo() == current
						&& !done.contains(edge.getFrom())) {
					edge.getFrom().setData(PARENT, current);
					stack.push(edge.getFrom());
					done.add(edge.getFrom());
				}
			}
		}
		return current;
	}

	@Override
	public void setGraph(IGraph graph) {
		if (!started)
			this.graph = graph;
	}

	@Override
	public void setRespectEdgeDirections(boolean respect) {
		this.respectEdgeDirection = respect;
	}

	@Override
	public void setStart(INode node) throws ExGNodeDoesNotExsistException {
		if (graph.getNode(node.getName()) != null) {
			if (!started)
				this.start = node;
		} else
			throw new ExGNodeDoesNotExsistException();
	}

	@Override
	public void clear() {
		for(INode node : graph.getAllNodes()){
			node.setData(ExGIterator.PARENT, null);
		}
		
	}

}
