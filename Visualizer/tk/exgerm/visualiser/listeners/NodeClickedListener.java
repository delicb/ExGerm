package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class NodeClickedListener implements IListener {
	
	private ViewManager viewManager;
	private VisualiserView view;
	
	public NodeClickedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[0];
		IGraph graph = node.getGraph();

		view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());
		
		if(Activator.visService.getVisualisers().containsKey(graph)){
			Activator.visService.addVisualiser(view);
		}
		
		view.centerNode(view.getModel().getNodeReferences().get(node.getName()));
		view.animation.start();
	}

}
