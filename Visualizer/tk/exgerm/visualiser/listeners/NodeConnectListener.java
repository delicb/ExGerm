package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.windows.NodeConnectWindow;

public class NodeConnectListener implements IListener {

	private ICoreContext context;
	
	public NodeConnectListener(ICoreContext context){
		this.context = context;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals("graphtree.node_connect"));{
			NodeConnectWindow window = new NodeConnectWindow((INode)parameters[0], context);
			window.setVisible(true);
		}
	}

}
