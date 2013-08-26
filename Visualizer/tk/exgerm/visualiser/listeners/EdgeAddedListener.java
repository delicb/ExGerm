package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class EdgeAddedListener implements IListener {
	
	private ViewManager viewManager;
	private ICoreContext context;
	
	public EdgeAddedListener(ViewManager _viewManager, ICoreContext _context){
		this.viewManager = _viewManager;
		this.context = _context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IEdge edge = (IEdge) parameters[1];
		
		if(context.getGraph(edge.getFrom().getFinalRoot().getName()) == null) return; // ako nema grafa u registru, kraj...
		
		VisualiserView view = viewManager.getSubView(edge.getFrom().getFinalRoot().getName(), edge.getFrom().getLevel()-1, edge.getFrom().getGraph().getName());
		
		String sourceNodeName = edge.getFrom().getName();
		String destinationNodeName = edge.getTo().getName();
		Integer id = edge.getID();
		boolean directed = edge.isDirected();
		
		view.getModel().addEdge(sourceNodeName, destinationNodeName, id, directed);
		
	}
}
