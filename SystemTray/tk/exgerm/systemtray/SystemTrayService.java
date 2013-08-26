package tk.exgerm.systemtray;

import java.awt.AWTException;
import java.awt.SystemTray;

import javax.swing.JOptionPane;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.systemtray.listeners.MainWindowMinimizedListener;

public class SystemTrayService implements IComponent {

	BundleContext bundleContext;
	ICoreContext coreContext;

	IListener mainWindowMinimizedListener;

	SystemTrayIcon systemTrayIcon;
	final SystemTray tray = SystemTray.getSystemTray();

	public SystemTrayService(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void setContext(ICoreContext context) {
		coreContext = context;

		try {
			systemTrayIcon = new SystemTrayIcon(coreContext);
		} catch (Exception e) {
		}
		
		mainWindowMinimizedListener = new MainWindowMinimizedListener(
				coreContext, systemTrayIcon);
	}

	public void turnOn() {
		
		if(!SystemTray.isSupported()){
			JOptionPane.showMessageDialog(null, "SystemTray is not supported.");
			return;
		}
		
		try {
			tray.add(systemTrayIcon);
		} catch (AWTException e) {
			return;
		}

		coreContext.listenEvent(ICoreContext.MAIN_WINDOW_STATE_CHANGED,
				mainWindowMinimizedListener);
	}

	public void turnOff() {
		tray.remove(systemTrayIcon);
		coreContext.removeListener(mainWindowMinimizedListener);
	}
}
