package tk.exgerm.ucsearch;

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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;

@SuppressWarnings("serial")
public class UCSearchChooser extends JDialog {

	private IGraph graph;
	private boolean dialogResult;

	private Container container = getContentPane();
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private Box okBox = Box.createHorizontalBox();
	private JComboBox cbFrom = new JComboBox();
	private JComboBox cbTo = new JComboBox();
	private JTextField tfAttribute = new JTextField(15);

	public UCSearchChooser(IGraph graph) {

		this.graph = graph;

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		setModal(true);
		setTitle("Uniform Cost Search");
		setLayout(new GridBagLayout());
		setResizable(false);
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/Search.png")));
		} catch (IOException e1) {}
		
		initializeBoxes();

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

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 20, 0, 10);

		container.add(new JLabel("Graph name:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20, 10, 0, 20);

		if (this.graph == null)
			container.add(new JLabel("<NO GRAPH SELECTED>"), c);
		else
			container.add(new JLabel(this.graph.getName()), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("From node:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(cbFrom, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("To node:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(cbTo, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Attribute:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 20);

		container.add(tfAttribute, c);
		
		tfAttribute.setToolTipText("Name of the attribute used as Weight");

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(20, 20, 20, 20);

		container.add(okBox, c);

		pack();
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
	}

	public String getFrom() {
		return ((INode) cbFrom.getSelectedItem()).getName();
	}

	public String getTo() {
		return ((INode) cbTo.getSelectedItem()).getName();
	}
	
	public String getAttribute(){
		return tfAttribute.getText();
	}

	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}

	public void enterPressed() {
		if (cbFrom.getSelectedIndex() == -1 || cbTo.getSelectedIndex() == -1) {
			JOptionPane
					.showMessageDialog(
							this,
							"You have not selected at least one of the destination nodes.\nPlease choose two different nodes.",
							"Attribute type missmatch",
							JOptionPane.WARNING_MESSAGE);
		} else if (getFrom().equals(getTo())) {
			JOptionPane
					.showMessageDialog(
							this,
							"You have selected the same node as the source and the destination of the search.\nPlease choose two different nodes.",
							"Attribute type missmatch",
							JOptionPane.WARNING_MESSAGE);
		} else {
			setDialogResult(true);
			setVisible(false);
		}
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}

	public void initializeBoxes() {

		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);

		if (this.graph != null) {
			for (int i = 0; i != this.graph.getNodeCount(); i++) {
				cbFrom.addItem(this.graph.getAllNodes().get(i));
				cbTo.addItem(this.graph.getAllNodes().get(i));
			}
		}
		cbFrom.setEditable(false);
		cbTo.setEditable(false);
	}

}
