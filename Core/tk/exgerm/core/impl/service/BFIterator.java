package tk.exgerm.core.impl.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;
/**
 *Iterator koji kroz graf prolazi principom <em>prvi u širinu</em>.</br>
 *
 * <em>Dodatak: posle ovog iteratora je neophodno potrebno zvati {@link tk.exgerm.core.plugin.ExGIterator#clear() clear()} metodu</em>
 */
public class BFIterator implements ExGIterator {
	
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
	 * Red u koji se smestaju nodovi kako bi se pamtio redosled iteracije
	 */
	Queue<INode> queue = new LinkedList<INode>();

	ArrayList<INode> done = new ArrayList<INode>();

	/**
	 * Da li iterator uzima u obzir usmerenje grana.
	 */
	private boolean respectEdgeDirection = false;

	private boolean firstDone = false;

	@Override
	public String getName() {
		return ExGIterator.BREADTH_FIRST;
	}

	@Override
	public boolean hasNext() {
		// ako se ne postuje onda mora da prodje krzo sve
		if (!respectEdgeDirection){
			return !(!(done.size() < graph.getAllNodes().size()) && queue.isEmpty());
		// ako se postuje prolazi samo kroz povezane
		}else{
			if(firstDone){
				return !queue.isEmpty();
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
		/*
		 * ima vishe slucajeva treba na sve obratiti paznju
		 */
		if (!firstDone) {
			if (start == null)
				start = graph.getAllNodes().get(0);
			firstDone = true;
			queue.add(start);
			done.add(start);
		}

		if (queue.isEmpty() && done.size() < graph.getAllNodes().size()
				&& firstDone && !respectEdgeDirection) {
			// trazim prvi nepovezani i ubacujemo ga u queue
			for (INode node : graph.getAllNodes()) {
				if (!done.contains(node)) {
					queue.add(node);
					done.add(node);
					break;
				}

			}
		}

		INode current = queue.poll();

		if (respectEdgeDirection) { // pazi se na usmerenje
			for (IEdge edge : current.getConnectedEdges()) {
				if (edge.isDirected()) { // usmerene grane
					if (edge.getFrom() == current
							&& !done.contains(edge.getTo())) {
						edge.getTo().setData(PARENT, current);
						queue.add(edge.getTo());
						done.add(edge.getTo());
					}
				} else { // neusmerene grane
					if (edge.getFrom() == current
							&& !done.contains(edge.getTo())) {
						edge.getTo().setData(PARENT, current);
						queue.add(edge.getTo());
						done.add(edge.getTo());
					}
					if (edge.getTo() == current
							&& !done.contains(edge.getFrom())) {
						edge.getFrom().setData(PARENT, current);
						queue.add(edge.getFrom());
						done.add(edge.getFrom());
					}
				}
			}
		} else { // ne pazi se -> dodajemo sve...
			for (IEdge edge : current.getConnectedEdges()) {
				if (edge.getFrom() == current
						&& !done.contains(edge.getTo())) {
					edge.getTo().setData(PARENT, current);
					queue.add(edge.getTo());
					done.add(edge.getTo());
				}
				if (edge.getTo() == current
						&& !done.contains(edge.getFrom())) {
					edge.getFrom().setData(PARENT, current);
					queue.add(edge.getFrom());
					done.add(edge.getFrom());
				}
			}
		}
		return current;
	}

	@Override
	public void setGraph(IGraph graph) {
			this.graph = graph;
	}

	@Override
	public void setRespectEdgeDirections(boolean respect) {
		this.respectEdgeDirection = respect;
	}

	@Override
	public void setStart(INode node) throws ExGNodeDoesNotExsistException {
		if (graph.getNode(node.getName()) != null) {
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
