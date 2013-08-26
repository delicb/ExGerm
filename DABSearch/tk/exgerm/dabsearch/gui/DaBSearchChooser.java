package tk.exgerm.dabsearch.gui;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;

@SuppressWarnings("serial")
public class DaBSearchChooser extends JDialog {
	
	private IGraph graph;
	private boolean dialogResult;
	
	private Container container = getContentPane();
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private Box okBox = Box.createHorizontalBox();
	private JComboBox cbFrom = new JComboBox();
	private JComboBox cbTo = new JComboBox();
	private JRadioButton rbBS = new JRadioButton("Bredth search");
	private JRadioButton rbDS = new JRadioButton("Depth search");
	private ButtonGroup btnGroup = new ButtonGroup();
	private Box radioBox = Box.createVerticalBox();
	
	public DaBSearchChooser(IGraph graph){
		
		this.graph = graph;
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getCenterPoint();
		
		setModal(true);
		setTitle("DaB Search");
		setLayout(new GridBagLayout());
		setResizable(false);	
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/Find.png")));
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
		c.insets = new Insets(20,20,0,10);
		
		container.add(new JLabel("Graph name:"),c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(20,10,0,20);
		
		if(this.graph == null)
			container.add(new JLabel("NO GRAPH SELECTED!"),c);
		else
			container.add(new JLabel(this.graph.getName()),c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,20,0,10);
		
		container.add(new JLabel("From node:"),c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,0,20);
		
		container.add(cbFrom,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10,20,0,10);
		
		container.add(new JLabel("To node:"),c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,0,20);
		
		container.add(cbTo,c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(10,20,0,10);
		
		container.add(new JLabel("Using search:"),c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,10,0,20);
		
		container.add(radioBox,c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20,20,20,20);
		
		container.add(okBox,c);
		
		pack();
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
	}
	
	public String getFrom(){
		return ((INode)cbFrom.getSelectedItem()).getName();
	}
	
	public String getTo(){
		return ((INode)cbTo.getSelectedItem()).getName();
	}
	
	public String getSearch(){
		if(rbBS.isSelected()) return ExGIterator.BREADTH_FIRST;
		else if (rbDS.isSelected()) return ExGIterator.DEPTH_FIRST;
		else return null;
	}
	
	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}
	
	public void enterPressed() {
		if(cbFrom.getSelectedIndex() == -1 || cbTo.getSelectedIndex() == -1){
			JOptionPane.showMessageDialog(this
					,"You have not selected at least one of the destination nodes.\nPlease choose two different nodes."
					,"Attribute type missmatch",
					JOptionPane.WARNING_MESSAGE);
		}else if(getFrom().equals(getTo())){
			JOptionPane.showMessageDialog(this
					,"You have selected the same node as the source and the destination of the search.\nPlease choose two different nodes."
					,"Attribute type missmatch",
					JOptionPane.WARNING_MESSAGE);
		}else if(!rbBS.isSelected() && !rbDS.isSelected()){
			JOptionPane.showMessageDialog(this
					,"You have not selected the search algorithm.\nPlease choose one of the offered."
					,"Attribute type missmatch",
					JOptionPane.WARNING_MESSAGE);
		}else{
			setDialogResult(true);
			setVisible(false);
		}
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}

	public void initializeBoxes(){
		
		btnGroup.add(rbBS);
		btnGroup.add(rbDS);
		
		radioBox.add(rbBS);
		radioBox.add(Box.createVerticalStrut(5));
		radioBox.add(rbDS);
		
		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);
		
		if(this.graph != null){
			for(int i = 0; i != this.graph.getNodeCount(); i++){
				cbFrom.addItem(this.graph.getAllNodes().get(i));
				cbTo.addItem(this.graph.getAllNodes().get(i));
			}
		}
		cbFrom.setEditable(false);
		cbTo.setEditable(false);
	}
	
}
