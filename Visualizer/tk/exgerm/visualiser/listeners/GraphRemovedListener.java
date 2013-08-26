package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class GraphRemovedListener implements IListener {

	private ViewManager viewManager;
	
	public GraphRemovedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		
		IGraph graph = (IGraph) parameters[0];
			
		if(Activator.visService.getVisualisers().containsKey((IGraph)graph)){
			VisualiserView v = viewManager.getRootView(graph.getName());
			Activator.visService.removeVisualiser(v);
			v.animation.stop();
			v.visualization.stop();
		}
		
	}

}
