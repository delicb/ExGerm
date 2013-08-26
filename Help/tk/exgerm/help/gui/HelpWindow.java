package tk.exgerm.help.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Position;
import javax.swing.tree.TreePath;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.help.exGERMIconRenderer;

@SuppressWarnings("serial")
public class HelpWindow extends JFrame {

	private ICoreContext context;

	private JSplitPane splitPane = new JSplitPane();
	private JScrollPane spTree = new JScrollPane();
	private JScrollPane spEditor = new JScrollPane();
	private JTree tree = new JTree(new HelpNode("exGERM", null, null));
	private JEditorPane editorPane = new JEditorPane();
	private int position = -1;
	private ArrayList<String> visitedURLs = new ArrayList<String>();
	private JLabel btnFwd = new JLabel(new ImageIcon(getClass().getResource("icons/forward.png")));
	private JLabel btnBwd = new JLabel(new ImageIcon(getClass().getResource("icons/back.png")));
	private Box btnBox = Box.createHorizontalBox();
	private JPanel panel = new JPanel();

	public HelpWindow(ICoreContext context) {
		super();
		this.context = context;

		try {
			URL helpIcon = getClass().getResource("icons/Help.png");
			if (helpIcon != null)
				setIconImage(ImageIO.read(helpIcon));
		} catch (Exception ex) {}
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setTitle("Help");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		editorPane.setEditable(false);
		editorPane.setContentType("text/html");

		class MyHyperlinkListener implements HyperlinkListener {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						editorPane.setPage(evt.getURL());
						HelpWindow.this.selectLinkedPage(evt.getURL()
								.toString());
						visitedURLs.add(evt.getURL().toString());
						position++;
						if (position >= 1)
							btnBwd.setEnabled(true);
					} catch (IOException e) {
					}
				}
			}
		}
		editorPane.addHyperlinkListener(new MyHyperlinkListener());
		editorPane.setDoubleBuffered(true);
		tree.setEditable(false);
		tree.setRootVisible(true);
		tree.setCellRenderer(new exGERMIconRenderer());
		spTree.setViewportView(tree);
		spEditor.setViewportView(editorPane);
		spEditor.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		initializePanel();
		splitPane.setLeftComponent(spTree);
		splitPane.setRightComponent(panel);

		splitPane.getLeftComponent().setPreferredSize(new Dimension(200, 400));
		splitPane.getRightComponent().setPreferredSize(new Dimension(400, 400));

		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] {
				100, 0, 200, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 0, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] {
				1.0, 0.0, 1.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] {
				1.0, 1.0E-4 };
		contentPane.add(splitPane, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		pack();

		getHelpContents();
		this.tree.addMouseListener(listener);
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
		
		btnFwd.addMouseListener(listenerFwd);
		btnBwd.addMouseListener(listenerBwd);
		
		btnBwd.setEnabled(false);
		btnFwd.setEnabled(false);
	}

	public JEditorPane getEditor() {
		return this.editorPane;
	}

	/**
	 * Metoda pribavlja od Core komponente sve registrovane helpove ostalih 
	 * komponenti i formira drvo help stranica.
	 */
	public void getHelpContents() {
		HelpNode parent;
		HelpNode node;
		for (String component : context.getAllHelps().keySet()) {
			ExGHelp help = context.getHelp(component);

			parent = (HelpNode) tree.getModel().getRoot();
			node = new HelpNode(component, parent, "");

			parent.addChild(node);
			parent = node;
			for (String title : help.getHelpMap().keySet()) {
				node = new HelpNode(title, parent, help.getHelpMap().get(title));
				parent.addChild(node);
			}
		}
	}

	/**
	 * Metoda selektuje TreeNode na drvetu helpa ukoliko je kliknuto na link sa
	 * url-om istim kao i neka od postojećih stranica u helpu.
	 * @param url
	 */
	public void selectLinkedPage(String url) {
		HelpNode root = (HelpNode) tree.getModel().getRoot();
		for (HelpNode node : new ArrayList<HelpNode>(root.getChildren())) {
			if (node.getUrl().equals(url)) {
				this.tree.setSelectionPath(tree.getNextMatch(node.toString(),
						0, Position.Bias.Forward));
			}
			for (HelpNode child : new ArrayList<HelpNode>(node.getChildren())) {
				if (child.getUrl().equals(url)) {
					this.tree.setSelectionPath(tree.getNextMatch(child
							.toString(), 0, Position.Bias.Forward));
				}
			}
		}
	}

	MouseListener listener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			TreePath selPath = HelpWindow.this.tree.getPathForLocation(
					e.getX(), e.getY());
			if (e.getClickCount() == 1) {
				if (selPath != null) {
					Object node = selPath.getLastPathComponent();
					if (e.getButton() == MouseEvent.BUTTON1) {
						if (node instanceof HelpNode) {
							try {
								String url = ((HelpNode) node).getUrl();
								if (url == null || url.length() == 0)
									return;
								HelpWindow.this.editorPane.setPage(url);
								HelpWindow.this.visitedURLs
										.add(((HelpNode) node).getUrl());
								HelpWindow.this.position++;
								if (position >= 1)
									btnBwd.setEnabled(true);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	};
	
	MouseListener listenerFwd = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 1) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if(btnFwd.isEnabled()){
						goForeward();
						if (position == visitedURLs.size() - 1)
							btnFwd.setEnabled(false);
						else
							btnFwd.setEnabled(true);
						btnBwd.setEnabled(true);
					}
				}
			}
		}
	};
	
	MouseListener listenerBwd = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 1) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if(btnBwd.isEnabled()){
						goBackward();
						if (position == 0)
							btnBwd.setEnabled(false);
						else
							btnBwd.setEnabled(true);
						btnFwd.setEnabled(true);
					}
				}
			}
		}
	};

	/**
	 * Ukoliko history postoji ova metoda će otvoriti prvi sledeći url iz liste
	 * prethodno posećenih url-ova helpa
	 */
	public void goForeward() {
		try {
			position++;
			this.editorPane.setPage(visitedURLs.get(position));
			selectLinkedPage(visitedURLs.get(position));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ukoliko histori postoji ova metoda će otvoriti prvi prethodni url iz liste
	 * prethodno posećenih url-ova helpa
	 */
	public void goBackward() {
		try {
			position--;
			this.editorPane.setPage(visitedURLs.get(position));
			selectLinkedPage(visitedURLs.get(position));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda inicijalizuje sve grafičke komponente na glavnom panelu ovog prozora.
	 */
	public void initializePanel() {
		this.panel.setLayout(new GridBagLayout());
		((GridBagLayout) panel.getLayout()).columnWidths = new int[] { 0, 0, 0,
				0 };
		((GridBagLayout) panel.getLayout()).rowHeights = new int[] { 0, 0, 0 };
		((GridBagLayout) panel.getLayout()).columnWeights = new double[] { 1.0,
				0.0, 0.0, 1.0E-4 };
		((GridBagLayout) panel.getLayout()).rowWeights = new double[] { 0.0,
				1.0, 1.0E-4 };

		btnBox.add(btnBwd);
		btnBox.add(Box.createHorizontalStrut(10));
		btnBox.add(btnFwd);

		panel.add(btnBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		panel.add(spEditor, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

	}

}
