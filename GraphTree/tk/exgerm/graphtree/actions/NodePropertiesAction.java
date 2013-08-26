package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

@SuppressWarnings("serial")
public class NodePropertiesAction extends AbstractAction {
	
	private ICoreContext context;
	private INode node;
	
	public NodePropertiesAction(ICoreContext context){
		putValue(NAME, "Properties"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Shows properties for the selected node."); //$NON-NLS-1$
		this.context = context;		
	}
	
	public void setNode(INode node){
		this.node = node;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(node != null)
			context.raise(GraphTree.NODE_PROPERTIES, node);
	}

}
