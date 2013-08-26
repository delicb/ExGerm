package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;

@SuppressWarnings("serial")
public class GraphPropertiesAction extends AbstractAction {

	private ICoreContext context;
	private IGraph graph;
	
	public GraphPropertiesAction(ICoreContext context){
		putValue(NAME, "Properties"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Shows properties for the selected graph."); //$NON-NLS-1$
		this.context = context;		
	}
	
	public void setGraph(IGraph graph){
		this.graph = graph;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.graph != null)
			this.context.raise(GraphTree.GRAPH_PROPERTIES, graph);
	}

}
