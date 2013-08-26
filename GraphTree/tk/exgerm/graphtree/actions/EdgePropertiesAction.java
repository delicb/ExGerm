package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

@SuppressWarnings("serial")
public class EdgePropertiesAction extends AbstractAction {

	private ICoreContext context;
	private IEdge edge;
		
	public EdgePropertiesAction(ICoreContext context){
		putValue(NAME, "Properties"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Shows properties for the selected edge."); //$NON-NLS-1$
		this.context = context;		
	}
	
	public void setEdge(IEdge edge){
		this.edge = edge;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(edge != null)
			context.raise(GraphTree.EDGE_PROPERTIES, edge);
	}

}
