package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;
import tk.exgerm.visualiser.windows.EdgeAttributesWindow;

public class TreeEdgePropertiesListener implements IListener {

	private ViewManager viewManager;
	private ICoreContext context;
	private VisualiserView view;
	
	public TreeEdgePropertiesListener(ViewManager _viewManager, ICoreContext _context){
		this.viewManager = _viewManager;
		this.context = _context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IEdge edge = (IEdge) parameters[0];
		INode node = edge.getFrom();
		
		view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());
		
		Activator.visService.addVisualiser(view);

		EdgeAttributesWindow window = new EdgeAttributesWindow(edge, context);
		window.setVisible(true);
	}

}
