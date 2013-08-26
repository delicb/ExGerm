package tk.exgerm.console.listeners;

import tk.exgerm.console.ConsoleService;
import tk.exgerm.console.ConsoleView;
import tk.exgerm.console.gui.Console;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IListener;

/**
 * Obrađuje događaje koji su u vezi sa tabovima (promena imena, zatvaranje). Ove
 * događaje ispaljuje TabManager, za razliku od ActiveTabChangedListener-a čije
 * događaje ispaljuju komande!
 * 
 */
public class TabEventsListener implements IListener {

	private ConsoleService service;

	public TabEventsListener(ConsoleService service) {
		this.service = service;
	}

	@Override
	public void raise(String event, Object... parameters) {
		ConsoleView consoleView = null;
		for (ConsoleView cv : service.getConsoleViews()) {
			if (cv.getComponent() == parameters[0])
				consoleView = cv;
		}

		if (consoleView == null)
			return; // zaštita od bacanja faličnog eventa :)

		if (event.equals(ExGGraphicalComponent.TAB_CLOSED)) {
			((Console) consoleView.getComponent()).removeListeners();
			service.getConsoleViews().remove(consoleView);
		} else if (event.equals(ExGGraphicalComponent.TAB_TITLE_CHANGED))
			consoleView.getComponent().setName(parameters[1].toString());
	}
}
