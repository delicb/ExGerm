package tk.exgerm.help.listeners;

import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.help.HelpService;
import tk.exgerm.help.HelpViewSrcoll;

public class HelpClosedListener implements IListener {

	private ICoreContext context;
	private HelpService service;

	public HelpClosedListener(ICoreContext context, HelpService service) {
		this.context = context;
		this.service = service;
	}

	@Override
	public void raise(String event, Object... params) {
		if (event.equals(ExGGraphicalComponent.TAB_CLOSED)) {			
			HelpViewSrcoll scroll = service.getScroll();
			context.removeGraphicalComponent(scroll);
			service.setScroll(null);
		}
	}
}
