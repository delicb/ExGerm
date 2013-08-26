package tk.exgerm.splashscreen;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

public class FrameworkStartLevelListener implements FrameworkListener {
	SplashScreen screen;

	public FrameworkStartLevelListener(SplashScreen screen) {
		this.screen = screen;
	}

	@Override
	public void frameworkEvent(FrameworkEvent e) {
		if (e.getType() == FrameworkEvent.STARTLEVEL_CHANGED)
			SplashScreen.loaded = true;
	}
}
