package tk.exgerm.eventtracker;

import tk.exgerm.core.plugin.IListener;

public class TabClosedListener implements IListener {

	EventTrackerService trackerService;
	EventTracker tracker;
	
	public TabClosedListener(EventTracker tracker, EventTrackerService trackerService) {
		this.tracker = tracker;
		this.trackerService = trackerService;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		if (parameters[0] == tracker) {
			trackerService.tabClosed();
		}
	}

}
