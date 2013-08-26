package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.windows.GraphAttributesWindow;

public class TreeGraphPropertiesListener implements IListener {

	private ViewManager viewManager;
	private ICoreContext context;
	
	public TreeGraphPropertiesListener(ViewManager _viewManager, ICoreContext _context){
		this.viewManager = _viewManager;
		this.context = _context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IGraph graph = (IGraph) parameters[0];
		
		String graphName = graph.getName();
		
		Activator.visService.addVisualiser(viewManager.getRootView(graphName));

		GraphAttributesWindow window = new GraphAttributesWindow(graph, context);
		window.setVisible(true);
	}

}
