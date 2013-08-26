package tk.exgerm.graphtree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import tk.exgerm.graphtree.model.Edge;
import tk.exgerm.graphtree.model.EdgeSeparator;
import tk.exgerm.graphtree.model.Graph;
import tk.exgerm.graphtree.model.Node;
import tk.exgerm.graphtree.model.NodeSeparator;
import tk.exgerm.graphtree.model.SubGraph;
import tk.exgerm.graphtree.model.Workspace;

@SuppressWarnings("serial")
public class exGERMIconRenderer extends DefaultTreeCellRenderer {

	public exGERMIconRenderer() {}
	
	/**
	 * Ova metoda setuje ikonicu za nodove u GraphTree komponenti u zavisnosti
	 * od klase noda koji trenutno obradjuje
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		if (value instanceof Workspace) {
			setIcon(((Workspace)value).getIcon());
		}
		
		else if (value instanceof Graph) {
			setIcon(((Graph)value).getIcon());
		}
		
		else if (value instanceof Edge) {
			setIcon(((Edge)value).getIcon());
		}
		
		else if (value instanceof NodeSeparator) {
			setIcon(((NodeSeparator)value).getIcon());
		}
		
		else if (value instanceof EdgeSeparator) {
			setIcon(((EdgeSeparator)value).getIcon());
		}
		
		else if (value instanceof Node) {
			setIcon(((Node)value).getIcon());
		}

		if (value instanceof SubGraph) {
			setIcon(((SubGraph)value).getIcon());
		}

		return this;
	}
	
}
