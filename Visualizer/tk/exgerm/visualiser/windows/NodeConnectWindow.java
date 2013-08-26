package tk.exgerm.visualiser.windows;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NodeConnectWindow extends JDialog {

	@SuppressWarnings("unused")
	private ICoreContext context;
	private Container container = getContentPane();
	private INode node;
	private boolean dialogResult = false;
	
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private Box okBox = Box.createHorizontalBox();
	private JComboBox cbNodes = new JComboBox();
	private JCheckBox chbDirected = new JCheckBox("Directed");
	
	public NodeConnectWindow(INode node, ICoreContext context) {
		this.context = context;
		this.node = node;

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setModal(true);
		setTitle("Connect node");
		setLayout(new GridBagLayout());
		setResizable(false);
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 20, 0, 10);

		container.add(new JLabel("Node from:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 10, 0, 20);

		container.add(new JLabel(node.getName()), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Node to:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(cbNodes, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Type of edge:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 20);

		container.add(chbDirected, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 20, 20, 20);

		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);
		container.add(okBox, c);

		cbNodes.setEditable(false);
		
		ActionListener ok = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				enterPressed();
			}
		};
		
		ActionListener cancel = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				escapePressed();
			}
		};
		
		btnOK.addActionListener(ok);       

		btnCancel.addActionListener(cancel);
		
		btnOK.registerKeyboardAction(ok, "EnterKey", 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0 , false), 
				JComponent.WHEN_IN_FOCUSED_WINDOW); 
		
		btnCancel.registerKeyboardAction(cancel, "EscapeKey", 
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0 , false), 
				JComponent.WHEN_IN_FOCUSED_WINDOW); 

		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
		initializeCombos();
		pack();
	}
	
	public INode getNode(){
		return this.node;
	}
	
	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}

	public void enterPressed() {
		setDialogResult(true);
		try {
			node.getGraph().addEdge(node, getNodeToConnect(), getDirected());
		} catch (ExGNodeDoesNotExsistException e) {
			e.printStackTrace();
		}
		setVisible(false);
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}
	
	public boolean getDirected(){
		return chbDirected.isSelected();
	}
	
	public INode getNodeToConnect(){
		return (INode)cbNodes.getSelectedItem();
	}
	
	public void initializeCombos(){
		ArrayList<INode> nodes = new ArrayList<INode>(node.getGraph().getAllNodes());
		for (INode node : nodes) {
			cbNodes.addItem(node);
		}
	}
}
