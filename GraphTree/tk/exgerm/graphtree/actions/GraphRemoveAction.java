package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class GraphRemoveAction extends AbstractAction {

	private ICoreContext context;
	private IGraph graph;
	
	public GraphRemoveAction(ICoreContext context){
		putValue(NAME, "Delete"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Deletes the selected graph from the workspace."); //$NON-NLS-1$
		this.context = context;	
	}
	
	public void setGraph(IGraph graph){
		this.graph = graph;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.graph != null){
			try {
				this.context.removeGraph(this.graph);
			} catch (ExGGraphDoesNotExsistException e1) {
				e1.printStackTrace();
			}
		}
	}

}
