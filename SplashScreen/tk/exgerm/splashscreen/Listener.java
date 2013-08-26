package tk.exgerm.splashscreen;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class Listener implements BundleListener {

	BundleContext context;
	SplashScreen splash;
	
	public Listener(BundleContext context, SplashScreen splash) {
		this.context = context;
		this.splash = splash;
	}
	
	@Override
	public void bundleChanged(BundleEvent event) {
		Bundle b = event.getBundle();
		splash.componentAdded(b.getSymbolicName());
	}

}
