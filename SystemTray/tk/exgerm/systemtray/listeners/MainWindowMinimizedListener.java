package tk.exgerm.systemtray.listeners;

import javax.swing.JFrame;

import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.systemtray.SystemTrayIcon;

public class MainWindowMinimizedListener implements IListener {

	private ICoreContext coreContext;
	SystemTrayIcon systemTrayIcon;

	public MainWindowMinimizedListener(ICoreContext coreContext,
			SystemTrayIcon systemTrayIcon) {
		this.coreContext = coreContext;
		this.systemTrayIcon = systemTrayIcon;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if (parameters[0].equals(JFrame.MAXIMIZED_BOTH | JFrame.ICONIFIED)
				|| parameters[0].equals(JFrame.ICONIFIED)) {
			
			systemTrayIcon.setMainWindowState((Integer) parameters[1]);
			coreContext.hideMainWindow();
		}
	}

}
