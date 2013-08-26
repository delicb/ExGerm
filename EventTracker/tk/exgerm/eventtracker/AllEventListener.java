package tk.exgerm.eventtracker;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import tk.exgerm.core.plugin.IListener;

public class AllEventListener implements IListener {

	PrintStream out;
	EventTrackerService tracker;

	public AllEventListener(PrintStream out, EventTrackerService tracker) {
		this.out = out;
		this.tracker = tracker;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if (!tracker.isVisible()) {
			tracker.firstRun();
		}
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("KK:mm:ss a");
		String time = sdf.format(now);
		out.print("[ " + time + "] Event: " + event);
		out.print(" [\t");
		for (Object o : parameters) {
			if (o != null) {
				String tmp = o.toString();
				if (tmp.length() > 30)
					tmp = tmp.substring(0, 30);
				out.print(tmp);
				out.print("\t");
			}
			else {
				out.print("\t" + o + "\t");
			}
		}
		out.print("]");
		out.println();
	}

}
