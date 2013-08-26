package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

@SuppressWarnings("serial")
public class NodeConnectAction extends AbstractAction {

	private ICoreContext context;
	private INode node;

	public NodeConnectAction(ICoreContext context) {
		putValue(NAME, "Connect");
		putValue(SHORT_DESCRIPTION,"Opens a dialog in which user can choose to " +
				"which node he wants to connect the selected one.");
		this.context = context;
	}

	public void setNode(INode node) {
		this.node = node;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.node != null)
			this.context.raise(GraphTree.NODE_CONNECT, node);
	}

}
