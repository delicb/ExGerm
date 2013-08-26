package tk.exgerm.persistance.listeners;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.persistance.PersistanceService;

public class ApplicationClosingListener implements IListener {

	private PersistanceService service;
	
	public ApplicationClosingListener(PersistanceService service) {
		this.service = service;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		service.applicationClosing();
	}
}
