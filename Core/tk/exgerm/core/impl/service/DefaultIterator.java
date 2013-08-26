package tk.exgerm.core.impl.service;

import java.util.Iterator;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;

/**
 * Podrazumenvani iterator kroz graf.</br>
 * 
 * <em>Dodatak: posle ovog iteratora nije potrebno zvati {@link tk.exgerm.core.plugin.ExGIterator#clear() clear()} metodu</em>
 * 
 */
public class DefaultIterator implements ExGIterator {

	/**
	 * {@link tk.exgerm.core.model.IGraph Graf} kroz koji se trenutno iterira
	 */
	private IGraph graph;
	private Iterator<INode> iter;

	int count = 0;

	@Override
	public String getName() {
		return ExGIterator.DEFAULT;
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public INode next() {
		return iter.next();
	}

	@Override
	public void setGraph(IGraph graph) {
		this.graph = graph;
		iter = graph.getAllNodes().iterator();
	}

	@Override
	public void setRespectEdgeDirections(boolean respect) {
		/*
		 * DefaultIterator nikada ne brine o usmerenju grana.
		 */
	}

	@Override
	public void setStart(INode node) throws ExGNodeDoesNotExsistException {
		/*
		 * DefaultIterator nema mogucnost psotavljanja startnog noda.
		 */

	}

	@Override
	public void clear() {
		for (INode node : graph.getAllNodes()) {
			node.setData(ExGIterator.PARENT, null);
		}

	}

}
