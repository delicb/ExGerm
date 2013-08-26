package tk.exgerm.systemtray;

import java.awt.Image;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;

import tk.exgerm.core.service.ICoreContext;

/**
 * Predstavlja ikonicu u SystemTray-u
 * 
 * @author Tim 2
 *
 */
public class SystemTrayIcon extends TrayIcon implements MouseListener{
	private ICoreContext coreContext;
	private int mainWindowState;
	
	public SystemTrayIcon(ICoreContext coreContext) throws Exception{
		super(createImage("images/icon16.png", "tray icon"));
        this.coreContext = coreContext;
        addMouseListener(this);
	}

    protected static Image createImage(String path, String description) {
        URL imageURL = Activator.class.getResource(path);
        
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

	public int getMainWindowState() {
		return mainWindowState;
	}

	public void setMainWindowState(int mainWindowState) {
		this.mainWindowState = mainWindowState;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(coreContext.isMainWindowsShown()){
			mainWindowState = coreContext.getMainWindowState();
			coreContext.hideMainWindow();
		}else {
			coreContext.showMainWindow();
			coreContext.setMainWindowState(mainWindowState);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
