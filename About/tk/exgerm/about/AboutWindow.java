package tk.exgerm.about;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class AboutWindow extends JDialog {

	private JLabel lblGERMProgram = new JLabel("*exGERM*");
	private JLabel lblInfo = new JLabel(
			"This project evolved from the initial ExGATE project "
					+ "(Extensible Graph Analisys and Transformation Engine)");
	private JLabel lblLink = new JLabel("http://www.exgerm.tk");
	private boolean reallyClose;

	// Programeri
	private JLabel lblDelInfo = new JLabel(
			"<HTML>Bojan Delić e11510<BR>Mail: delicb@gmail.com</HTML>");
	private JLabel lblWikiInfo = new JLabel(
			"<HTML>Viktor Bek e11482<BR>Mail: wiktorns@yahoo.com</HTML>");
	private JLabel lblZeljoInfo = new JLabel(
			"<HTML>Željko Vrbaški e11442<BR>Mail: vrbaskiz@gmail.com</HTML>");
	private JLabel lblCacheInfo = new JLabel(
			"<HTML>Dušan Krivošija e11484<BR>Mail: dusankrivosija@gmail.com</HTML>");
	private JLabel lblKedzaInfo = new JLabel(
			"<HTML>Nemanja Kedžić e11360<BR>Mail: kedzic@gmail.com</HTML>");
	private JLabel lblDedaInfo = new JLabel(
			"<HTML>Igor Dedić e11500<BR>Mail: zwerrr@gmail.com</HTML>");

	private JLabel lblDelPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/bojan.jpg")));
	private JLabel lblWikiPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/viktor.jpg")));
	private JLabel lblZeljoPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/zeljko.jpg")));
	private JLabel lblCachePicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/dusan.jpg")));
	private JLabel lblKedzaPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/nemanja.jpg")));
	private JLabel lblDedaPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/igor.jpg")));
	private JLabel lblGERMPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/logo.png")));
	private JLabel lblexGERMTeamPicture = new JLabel(new ImageIcon(getClass()
			.getResource("images/Team2.jpg")));
	private JLabel lblTeamInfo = new JLabel("***Team 2***");

	private JButton btnClose = new JButton("Close");
	private Box box4 = Box.createHorizontalBox();
	private Box box2 = Box.createHorizontalBox();
	private Box box1 = Box.createHorizontalBox();
	private Box box3 = Box.createHorizontalBox();
	private Box box5 = Box.createHorizontalBox();
	private Box box6 = Box.createHorizontalBox();
	private Dimension pctSize = new Dimension(80, 80);
	private JTabbedPane tabs = new JTabbedPane();
	private JPanel pGerm = new JPanel(new GridBagLayout());
	private JPanel pTeamPct = new JPanel(new GridBagLayout());
	private JPanel pTeamInfo = new JPanel(new GridBagLayout());

	private ArrayList<Box> boxes;
	private Random rnd = new Random();
	GridBagConstraints g = new GridBagConstraints();

	public AboutWindow() {
		setTitle("About exGERM");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(new GridBagLayout());
		setResizable(false);
		initializeBoxes();
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		reallyClose = false;
		boxes = new ArrayList<Box>();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escapePressed();
			}
		});

		tabs.addTab("exGERM", pGerm);
		tabs.addTab("Team Info", pTeamInfo);
		tabs.addTab("Team", pTeamPct);

		g.gridx = 0;
		g.gridy = 0;
		g.anchor = GridBagConstraints.CENTER;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.weightx = 1;
		g.insets = new Insets(20, 20, 0, 20);

		pGerm.add(lblGERMPicture, g);

		g.gridx = 0;
		g.gridy = 1;
		g.anchor = GridBagConstraints.CENTER;
		g.fill = GridBagConstraints.NONE;
		g.weightx = 1;
		g.insets = new Insets(20, 20, 0, 20);

		pGerm.add(lblGERMProgram, g);

		g.gridx = 0;
		g.gridy = 2;
		g.anchor = GridBagConstraints.CENTER;
		g.fill = GridBagConstraints.NONE;
		g.weightx = 1;
		g.insets = new Insets(20, 20, 0, 20);

		pGerm.add(lblInfo, g);

		g.gridx = 0;
		g.gridy = 3;
		g.anchor = GridBagConstraints.CENTER;
		g.fill = GridBagConstraints.NONE;
		g.weightx = 1;
		g.insets = new Insets(0, 20, 10, 20);

		pGerm.add(lblLink, g);

		lblLink.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() > 0) {
					URLOpener.openURL("http://www.exgerm.tk");
				}
			}
		});

		lblLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		insertBoxes();

		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);

		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE
						&& e.getID() == KeyEvent.KEY_PRESSED) {
					escapePressed();
				}
				return false;
			}
		});
	}

	/**
	 * Funkcija popunjava horizontalBox-ove sa adektvatnim slikama i
	 * informacijama o autorima programa.
	 */
	public void initializeBoxes() {
		lblZeljoPicture.setMaximumSize(pctSize);
		lblWikiPicture.setMaximumSize(pctSize);
		lblCachePicture.setMaximumSize(pctSize);
		lblDelPicture.setMaximumSize(pctSize);
		lblKedzaPicture.setMaximumSize(pctSize);
		lblDedaPicture.setMaximumSize(pctSize);

		box1.add(lblZeljoPicture);
		box1.add(Box.createHorizontalStrut(15));
		box1.add(lblZeljoInfo);

		box2.add(lblWikiPicture);
		box2.add(Box.createHorizontalStrut(15));
		box2.add(lblWikiInfo);

		box3.add(lblCachePicture);
		box3.add(Box.createHorizontalStrut(15));
		box3.add(lblCacheInfo);

		box4.add(lblDelPicture);
		box4.add(Box.createHorizontalStrut(15));
		box4.add(lblDelInfo);

		box5.add(lblKedzaPicture);
		box5.add(Box.createHorizontalStrut(15));
		box5.add(lblKedzaInfo);

		box6.add(lblDedaPicture);
		box6.add(Box.createHorizontalStrut(15));
		box6.add(lblDedaInfo);

	}

	/**
	 * Funkcija definise reakciju prozora na esc dugme na tastaturi ili pak
	 * pritisak na dugme "Close". Prozor se sakriva.
	 */
	private void escapePressed() {
		if (reallyClose)
			setVisible(false);
		else {
			btnClose.setText("Are you sure?");
			pTeamInfo.removeAll();
			insertBoxes();
			reallyClose = true;
		}
	}

	/**
	 * Vraća nasumično odabran box jednog od autora programa :)
	 * 
	 * @return random Box autora
	 */
	private Box getRandomBox() {
		Box result = null;
		while (result == null) {
			if (boxes.size() == 0)
				return null;
			try {
				int number = rnd.nextInt(6);
				result = boxes.get(number);
				boxes.remove(number);
			} catch (IndexOutOfBoundsException e) {
			}
		}
		return result;
	}

	private void insertBoxes() {
		boxes.add(box1);
		boxes.add(box2);
		boxes.add(box3);
		boxes.add(box4);
		boxes.add(box5);
		boxes.add(box6);

		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(10, 20, 0, 0);

		pTeamInfo.add(getRandomBox(), g);

		g.gridx = 1;
		g.gridy = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.EAST;
		g.insets = new Insets(10, 20, 0, 20);

		pTeamInfo.add(getRandomBox(), g);

		g.gridx = 0;
		g.gridy = 2;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(0, 20, 10, 0);

		pTeamInfo.add(getRandomBox(), g);

		g.gridx = 1;
		g.gridy = 2;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.EAST;
		g.insets = new Insets(0, 20, 10, 20);

		pTeamInfo.add(getRandomBox(), g);

		g.gridx = 0;
		g.gridy = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(0, 20, 10, 0);

		pTeamInfo.add(getRandomBox(), g);

		g.gridx = 1;
		g.gridy = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.EAST;
		g.insets = new Insets(0, 20, 10, 20);

		pTeamInfo.add(getRandomBox(), g);
		
		g.gridx = 0;
		g.gridy = 0;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(20, 20, 0, 20);

		lblexGERMTeamPicture.setMaximumSize(new Dimension(200, 80));
		pTeamPct.add(lblexGERMTeamPicture, g);
		
		g.gridx = 0;
		g.gridy = 1;
		g.fill = GridBagConstraints.NONE;
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(10, 20, 10, 20);
		
		pTeamPct.add(lblTeamInfo, g);

		Container container = getContentPane();

		g.gridx = 0;
		g.gridy = 0;
		g.anchor = GridBagConstraints.WEST;
		g.fill = GridBagConstraints.NONE;
		g.insets = new Insets(20, 20, 0, 20);

		container.add(tabs, g);

		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 1;
		g.anchor = GridBagConstraints.EAST;
		g.fill = GridBagConstraints.NONE;
		g.insets = new Insets(0, 30, 10, 20);

		container.add(btnClose, g);

		this.pack();
	}
}
