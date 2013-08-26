package tk.exgerm.help.listeners;

import java.io.IOException;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.help.HelpService;
import tk.exgerm.help.HelpView;
import tk.exgerm.help.HelpViewSrcoll;
import tk.exgerm.help.gui.HelpWindow;

public class HelpRequestListener implements IListener {

	private ICoreContext context;
	private HelpService service;

	public HelpRequestListener(ICoreContext context, HelpService service) {
		this.context = context;
		this.service = service;
	}

	@Override
	public void raise(String event, Object... params) {
		if (event.equals(ExGHelp.HELP_REQUESTED)) {
			String url;
			try {
				url = context.getHelp((String) params[0]).getHelpMap().get(
						(String) params[1]);
			} catch (NullPointerException n) {
				url = getClass().getResource("help_template.htm").toString();
			}
			// Otvaranje unutar desnog taba
			if (params.length == 2
					|| (params.length == 3 && params[2].equals(false))) {
				HelpViewSrcoll scroll = service.getScroll();
				if (scroll == null) {
					scroll = new HelpViewSrcoll(new HelpView());
					context.addGraphicalComponent(scroll);
					service.setScroll(scroll);
				}
				try {
					scroll.getHelpView().setPage(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			// Otvaranja u posebnom, eksternom prozoru
			} else if (params.length == 3 && params[2].equals(true)) {
				HelpWindow win = new HelpWindow(context);
				try {
					win.getEditor().setPage(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
				win.setVisible(true);
			}
		}
	}
}
