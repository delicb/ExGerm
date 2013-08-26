package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;

public class NodeChangedListener implements IListener {

	private PersistanceService service;
	private ICoreContext context;

	public NodeChangedListener(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		INode n = (INode) parameters[0];
		if (n != null) {
			IGraph g = n.getFinalRoot();
			if (context.getGraph(g.getName()) != null)
				service.graphChanged(g);
		}
	}

}
