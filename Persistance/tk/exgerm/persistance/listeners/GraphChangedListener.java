package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;

public class GraphChangedListener implements IListener {

	private PersistanceService service;
	private ICoreContext context;

	public GraphChangedListener(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IGraph g = (IGraph) parameters[0];
		Object n = parameters[1];
		if (g != null && context.getGraph(g.getName()) != null)
			service.graphChanged(g.getFinalRoot());
	}

}
