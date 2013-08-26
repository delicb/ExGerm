package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.view.VisualiserView;

public class NodeRenamedListener implements IListener {
	
	private ViewManager viewManager;
	private VisualiserView view;
	
	public NodeRenamedListener(ViewManager _viewManager){
		this.viewManager = _viewManager;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode node = (INode) parameters[0];
		String oldName = (String) parameters[1]; 

		view = viewManager.getSubView(node.getFinalRoot().getName(), node.getLevel()-1, node.getGraph().getName());

		VisNode vn = view.getModel().getNodeReferences().get(oldName);
		vn.setName(node.getName());
		view.getModel().getNodeReferences().remove(oldName);
		view.getModel().getNodeReferences().put(vn.getName(), vn);
		view.repaint();
	}

}
