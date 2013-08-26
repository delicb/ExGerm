package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

@SuppressWarnings("serial")
public class NewSubgraphAction extends AbstractAction {

	private ICoreContext context;
	private IGraph graph;
	
	public NewSubgraphAction(ICoreContext context){
		putValue(NAME, "New subgraph"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Adds a new subgraph to the selected graph."); //$NON-NLS-1$
		this.context = context;		
	}
	
	public void setGraph(IGraph graph){
		this.graph = graph;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.graph != null)
			this.context.raise(GraphTree.NEW_SUBGRAPH, this.graph);
	}

}
