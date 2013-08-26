package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

public class NodeDoubleClickedListener implements IListener {
	
	private ViewManager viewManager;
	private VisualiserView view;
	
	public NodeDoubleClickedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[0];
		
		if(node instanceof IGraph){
			view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel(), node.getName());
			Activator.visService.addVisualiser(view);
		}

	}

}
