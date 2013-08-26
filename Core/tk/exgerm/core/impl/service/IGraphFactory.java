package tk.exgerm.core.impl.service;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;

/**
 * Predstavlja fabriku grafova i elemenata grafa. 
 *  
 * @author Tim 2
 */
public interface IGraphFactory {
	
	/**
	 * Kreira novu {@link tk.exgerm.core.model.IEdge granu} sa zadtim izvornim i
	 * odredišnim čvorom.
	 * 
	 * @param from Izvorni {@link tk.exgerm.core.model.INode čvor}
	 * @param to Odredišni {@link tk.exgerm.core.model.INode čvor}
	 * 
	 * @return Novokreiranu granu
	 */
	public IEdge newEdge(INode from, INode to);
	
	/**
	 * Kreira {@link tk.exgerm.core.model.IGraph graf} sa imenom <em>name</em>
	 * 
	 * @param name
	 *            Ime novog grafa
	 * @return Novokreriani graf
	 */
	public IGraph newGraph(String name);

	/**
	 * Kreira {@link tk.exgerm.core.model.INode čvor} sa imenom <em>name</em>
	 * 
	 * @param name
	 *            Ime novog čvora.
	 * @return Novokreirani čvor.
	 */
	public INode newNode(String name);

}