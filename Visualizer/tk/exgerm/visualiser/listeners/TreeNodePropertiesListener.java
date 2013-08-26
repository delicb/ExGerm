package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;
import tk.exgerm.visualiser.windows.GraphAttributesWindow;
import tk.exgerm.visualiser.windows.NodeAttributeWindow;

public class TreeNodePropertiesListener implements IListener {

	private ViewManager viewManager;
	private ICoreContext context;
	private VisualiserView view;
	
	public TreeNodePropertiesListener(ViewManager _viewManager, ICoreContext _context){
		this.viewManager = _viewManager;
		this.context = _context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[0];

		view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());
		
		Activator.visService.addVisualiser(view);
		
		view.centerNode(view.getModel().getNodeReferences().get(node.getName()));
		view.animation.start();
		
		if(node instanceof IGraph){
			GraphAttributesWindow window = new GraphAttributesWindow((IGraph)node, context);
			window.setVisible(true);
		}else{
			NodeAttributeWindow window = new NodeAttributeWindow(node, context);
			window.setVisible(true);
		}
	}

}
