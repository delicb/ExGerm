package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.persistance.PersistanceService;

public class GraphRemovedListener implements IListener {

	private PersistanceService service;
	
	public GraphRemovedListener(PersistanceService service) {
		this.service = service;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		service.graphRemoved(((IGraph)parameters[0]).getName());
	}

}
