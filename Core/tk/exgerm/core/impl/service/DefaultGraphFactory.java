package tk.exgerm.core.impl.service;

import tk.exgerm.core.impl.model.Edge;
import tk.exgerm.core.impl.model.Graph;
import tk.exgerm.core.impl.model.Node;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;

/**
 * Fabrika koja kreira i vraća {@link tk.exgerm.core.model.INode čvorove},
 * {@link tk.exgerm.core.model.IEdge grane} i
 * {@link tk.exgerm.core.model.IGraph grafove}.
 * 
 * @author Tim 2
 * 
 */
public class DefaultGraphFactory implements IGraphFactory {

	@Override
	public IGraph newGraph(String name) {
		return new Graph(name);
	}

	@Override
	public INode newNode(String name) {
		return new Node(name);
	}

	@Override
	public IEdge newEdge(INode from, INode to) {
		return new Edge(from, to);
	}

}
