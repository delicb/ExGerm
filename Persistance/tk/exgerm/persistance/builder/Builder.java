package tk.exgerm.persistance.builder;

import tk.exgerm.core.model.IGraph;

/**
 * Apstrakcija bildera. Konkretni bileri treba da na osnovu prosleđenog grafa
 * kreiraju string koji će kasnije moći da se isparsira i da se od njega dobije
 * isti graf.
 * 
 * @author Tim 2
 */
public interface Builder {
	/**
	 * Kreira stringovsku reprezentaciju grafa. 
	 * @param graph Graf koji se serijalizuje
	 */
	public void buildGraph(IGraph graph);

	/**
	 * Vraća reprezentaciju grafa.
	 * @return Reprezentacija grafa u string-u... 
	 */
	public String getResult();
}
