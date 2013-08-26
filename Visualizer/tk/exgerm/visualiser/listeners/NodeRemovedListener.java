package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class NodeRemovedListener implements IListener {
	
	private ViewManager viewManager;
	private VisualiserView view;

	public NodeRemovedListener(ViewManager _vm){
		viewManager = _vm;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[1];
		
		view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());
		view.getModel().removeNode(node.getName());
		
		if(node instanceof IGraph){
			if(Activator.visService.getVisualisers().containsKey((IGraph)node)){
				VisualiserView v = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel(), node.getName());
				Activator.visService.removeVisualiser(v);
				v.animation.stop();
				v.visualization.stop();
			}
		}

	}
}
