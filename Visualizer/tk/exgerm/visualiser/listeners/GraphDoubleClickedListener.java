package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;

public class GraphDoubleClickedListener implements IListener {
	
	private ViewManager viewManager;
	
	public GraphDoubleClickedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IGraph graph = (IGraph) parameters[0];
		
		String graphName = graph.getName();
		
		Activator.visService.addVisualiser(viewManager.getRootView(graphName));
	}

}
