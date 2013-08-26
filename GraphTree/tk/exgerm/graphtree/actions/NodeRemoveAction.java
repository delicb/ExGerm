package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NodeRemoveAction extends AbstractAction {

	@SuppressWarnings("unused")
	private ICoreContext context;
	private INode node;
	
	public NodeRemoveAction(ICoreContext context){
		putValue(NAME, "Delete"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Deletes the selected node from the graph."); //$NON-NLS-1$
		this.context = context;	
	}
	
	public void setNode(INode node){
		this.node = node;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.node != null){
			try {
				this.node.getGraph().removeNode(this.node);
			} catch (ExGNodeDoesNotExsistException e1) {
				e1.printStackTrace();
			}
		}
	}

}
