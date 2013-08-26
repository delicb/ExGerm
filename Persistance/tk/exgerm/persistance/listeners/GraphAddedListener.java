package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.persistance.PersistanceService;

public class GraphAddedListener implements IListener {

	private PersistanceService service;
	
	public GraphAddedListener(PersistanceService service) {
		this.service = service;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		service.graphAdded((IGraph)parameters[0]);
	}

}
