package tk.exgerm.persistance.listeners;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.persistance.PersistanceService;

public class GraphRenamedListener implements IListener {

	private PersistanceService service;
	
	public GraphRenamedListener(PersistanceService service) {
		this.service = service;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		IGraph g = (IGraph) parameters[0];
		String oldName = (String) parameters[1];
		if (g != null && oldName != null) {
			service.graphRenamed(oldName, g.getName());
			service.graphChanged(g);
		}
	}

}
