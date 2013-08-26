package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.model.VisualiserModel;
import tk.exgerm.visualiser.view.VisualiserView;

public class NodeAddedListener implements IListener {
	
	private ViewManager viewManager;
	private ICoreContext context;
	
	public NodeAddedListener(ICoreContext _context, ViewManager _viewManager){
		this.viewManager = _viewManager;
		this.context = _context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[1];
		
		if(context.getGraph(node.getFinalRoot().getName()) == null) return; // ako nema grafa u registru, kraj...

		VisualiserView view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());
		if(node instanceof IGraph){
			view.getModel().addNode(node.getName(), true, node.getFinalRoot().getName(), node.getLevel());
			
			VisualiserModel m = new VisualiserModel(context);
			VisualiserView v = new VisualiserView(m);
			m.setView(v);
			v.setName(node.getName());
			v.setFinalRoot(node.getFinalRoot().getName());
			v.setLevel(node.getLevel());
			v.getModel().setGraph((IGraph) node);
			
			viewManager.addSubView(node.getFinalRoot().getName(), node.getLevel(), node.getName(), v);

		}else{
			view.getModel().addNode(node.getName(), false, null, node.getLevel());
		}
		
	}
}
