package tk.exgerm.core.plugin;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.INode;

/**
 * <p>
 * Način obrade {@link tk.exgerm.core.model.INode čvorova} (VisitorPatern).
 * 
 * Klijenti koji žele da obave neke radnje nad čvorom treba da implementiraju
 * ovaj interface i da pozovu
 * {@link tk.exgerm.core.model.INode#accept(IVisitor) accept metodu čvora} ili
 * {@link tk.exgerm.core.model.IEdge#accept(IVisitor) accept metodu grane}
 * <p>
 * Primer koda koji radi istu stvar nad svakim elementom kroz koji prolazi
 * {@link tk.exgerm.core.plugin.Iterator iterator}: <code>
 * <pre>
 * Iterator it;
 * IVisitor v;
 * while (it.hasNext()) {
 * 	it.next().accept(v);
 * }
 * </pre>
 * </code>
 * 
 * @author Tim 2
 * 
 */
public interface IVisitor {

	/**
	 * Obrađuje {@link tk.exgerm.core.model.INode čvor}
	 * 
	 * @param node
	 *            Čvor koji se obrađuje
	 */
	public void visit(INode node);

	/**
	 * Obrađuje {@link tk.exgerm.core.model.IEdge granu}
	 * 
	 * @param node
	 *            Grana koja se obrađuje
	 */
	public void visit(IEdge edge);
}
