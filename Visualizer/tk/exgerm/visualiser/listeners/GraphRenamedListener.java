package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.view.VisualiserView;

public class GraphRenamedListener implements IListener {

	private ViewManager viewManager;
	
	public GraphRenamedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IGraph graph = (IGraph) parameters[0];
		String oldName = (String) parameters[1];
		
		boolean root = true;
		try{
			viewManager.getRootView(oldName);
		}catch(Exception e){
			root = false;
		}
		
		if(root){
			viewManager.renameRootView(oldName, graph.getName());
		}else{
			viewManager.renameSubView(oldName, graph.getName(), graph.getFinalRoot().getName(), graph.getLevel());
			
			VisualiserView view = viewManager.getSubView(graph.getFinalRoot().getName(), graph.getLevel()-1, graph.getGraph().getName());
			
			VisNode vn = view.getModel().getNodeReferences().get(oldName);
			vn.setName(graph.getName());
			view.getModel().getNodeReferences().remove(oldName);
			view.getModel().getNodeReferences().put(vn.getName(), vn);
			view.repaint();
		}
	}
}
