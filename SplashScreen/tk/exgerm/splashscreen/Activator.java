package tk.exgerm.splashscreen;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkListener;

public class Activator implements BundleActivator {

	BundleListener bundleListener;
	FrameworkListener frameworkListener;
	SplashScreen screen;

	public void start(BundleContext context) throws Exception {
		if (screen == null) 
			screen = new SplashScreen();
		new Thread(screen).start();
		if (bundleListener == null)
			bundleListener = new Listener(context, screen);
		
		if (frameworkListener == null)
			frameworkListener = new FrameworkStartLevelListener(screen);
		
		context.addBundleListener(bundleListener);
		context.addFrameworkListener(frameworkListener);
	}

	public void stop(BundleContext context) throws Exception {
		context.removeBundleListener(bundleListener);
		context.removeFrameworkListener(frameworkListener);
	}

}
