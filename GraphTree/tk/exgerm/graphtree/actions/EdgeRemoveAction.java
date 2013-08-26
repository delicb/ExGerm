package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class EdgeRemoveAction extends AbstractAction {

	@SuppressWarnings("unused")
	private ICoreContext context;
	private IEdge edge;
	
	public EdgeRemoveAction(ICoreContext context){
		putValue(NAME, "Delete"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Deletes the selected edge from the graph."); //$NON-NLS-1$
		this.context = context;	
	}
	
	public void setEdge(IEdge edge){
		this.edge = edge;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.edge != null){
			this.edge.getGraph().removeEdge(this.edge);
		}

	}

}
