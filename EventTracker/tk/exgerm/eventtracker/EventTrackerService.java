package tk.exgerm.eventtracker;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;

public class EventTrackerService implements IComponent {

	EventTracker tracker;
	BundleContext context;
	ICoreContext coreContext;
	AllEventListener allEventsListener;
	TabClosedListener tabClosedListener;
	boolean isVisible = false;

	public EventTrackerService(BundleContext context) {
		this.context = context;
		this.tracker = new EventTracker();
		this.allEventsListener = new AllEventListener(tracker.getPrintStream(), this);
		this.tabClosedListener = new TabClosedListener(tracker, this);
	}

	@Override
	public void setContext(ICoreContext context) {
		this.coreContext = context;
		
		this.coreContext.listenEvent(IListener.ALL_EVENTS, allEventsListener);
		this.coreContext.listenEvent(ExGGraphicalComponent.TAB_CLOSED, tabClosedListener);
	}
	
	// čistimo za sobom
	public void stop() {
		coreContext.removeGraphicalComponent(tracker);
		coreContext.removeListener(allEventsListener);
		coreContext.removeListener(tabClosedListener);
		isVisible = false;
	}
	
	public void firstRun() {
		this.coreContext.addGraphicalComponent(tracker);
		isVisible = true;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void tabClosed() {
		this.isVisible = false;
	}

}
