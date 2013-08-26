package tk.exgerm.help;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import tk.exgerm.help.gui.HelpNode;

@SuppressWarnings("serial")
public class exGERMIconRenderer extends DefaultTreeCellRenderer {

	public exGERMIconRenderer() {
	}

	/**
	 * Ova metoda setuje ikonicu za nodove u JTree u Help komponenti
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (value instanceof HelpNode) {

			if (((HelpNode) value).getParent() == null)
				setIcon(new ImageIcon(getClass()
						.getResource("gui/icons/icon16.png")));
			else if(((HelpNode) value).isLeaf())
				setIcon(new ImageIcon(getClass()
						.getResource("gui/icons/papir.png")));
			else
				setIcon(new ImageIcon(getClass()
						.getResource("gui/icons/knjizica.png")));

		}

		return this;
	}

}
