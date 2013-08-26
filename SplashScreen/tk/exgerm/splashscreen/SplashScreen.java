package tk.exgerm.splashscreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JWindow;

@SuppressWarnings("serial")
public class SplashScreen extends JWindow implements Runnable {
	private List<String> activated = new ArrayList<String>();
	
	MediaTracker tracker;
	Image img;
	String loadinStatus = "";
	static boolean loaded;
	
	public SplashScreen() {		
		tracker = new MediaTracker(this);
		URL imageURL = getClass().getResource("images/logo.png");
		img = getToolkit().getImage(imageURL);
		tracker.addImage(img, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int width = img.getWidth(this);
		int height = img.getHeight(this);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(activated.size() <= 1)
					setVisible(false);
			}
		});
		setVisible(true);
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.drawImage(img, 0, 0, null);

		g2.setColor(new Color(87, 121, 26));
		g2.setFont(new Font(g2.getFont().getFamily(), Font.BOLD, g2.getFont().getSize()));
		g2.drawString(loadinStatus, 50, 253);
	}

	public void componentAdded(String component) {
		this.activated.add(component);
		loadinStatus = "Loading... " + component;
		
		toFront();
		repaint(50, 200, 300, 100);
	}

	public boolean isComponentStarted(String component) {
		return this.activated.contains(component);
	}

	@Override
	public void run() {
		while (true) {
			if (loaded == true) {
				setVisible(false);
				dispose();
				break;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
