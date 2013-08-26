package tk.exgerm.visualiser.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.windows.NewNodeWindow;

public class NewNodeListener implements IListener {
private ICoreContext context;
	
	public NewNodeListener(ICoreContext context){
		this.context = context;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if(event.equals("graphtree.new_node")){
			NewNodeWindow window = new NewNodeWindow(context, (IGraph)parameters[0], 0);
			window.setVisible(true);
		}
		else if(event.equals("graphtree.new_subgraph")){
			NewNodeWindow window = new NewNodeWindow(context, (IGraph)parameters[0], 1);
			window.setVisible(true);
		}
	}
}
