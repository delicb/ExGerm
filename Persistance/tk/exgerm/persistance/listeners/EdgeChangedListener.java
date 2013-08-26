package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.persistance.PersistanceService;

public class EdgeChangedListener implements IListener {

	private PersistanceService service;
	private ICoreContext context;

	public EdgeChangedListener(PersistanceService service, ICoreContext context) {
		this.service = service;
		this.context = context;
	}

	@Override
	public void raise(String event, Object... parameters) {
		IEdge e = (IEdge) parameters[0];
		if (e != null) {
			IGraph g = e.getFrom().getFinalRoot();
			if (context.getGraph(g.getName()) != null)
				service.graphChanged(g);
		}

	}

}
