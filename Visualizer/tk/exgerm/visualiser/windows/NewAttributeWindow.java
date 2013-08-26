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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class NewAttributeWindow extends JDialog {

	private ArrayList<String> existingProps = new ArrayList<String>();
	private boolean dialogResult = false;
	
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel"); 
	private Box okBox = Box.createHorizontalBox();
	
	private JRadioButton tb1 = new JRadioButton("String");
	private JRadioButton tb2 = new JRadioButton("Boolean");
	private JRadioButton tb3 = new JRadioButton("Integer");
	private JRadioButton tb4 = new JRadioButton("Double");
	private JTextField taName = new JTextField(20);
	private ButtonGroup btnGroup = new ButtonGroup();
	
	public NewAttributeWindow(ArrayList<String> props){
		this.existingProps = props;
		
		setModal(true);
		setTitle("New attribute");
		setLayout(new GridBagLayout());
		setResizable(false);
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getCenterPoint();
		
		Container container = getContentPane();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,20,0,10);
		
		container.add(new JLabel("Name of property:"),c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,20,0,10);
		
		container.add(taName,c);
		
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,20,0,10);
		
		container.add(new JLabel("Type of property:"),c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,5,0,20);
		
		container.add(tb1,c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,5,0,20);
		
		container.add(tb2,c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,5,0,20);
		
		container.add(tb3,c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,5,0,20);
		
		container.add(tb4,c);
		
		btnGroup.add(tb1);
		btnGroup.add(tb2);
		btnGroup.add(tb3);
		btnGroup.add(tb4);
		
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10,10,20,20);
		
		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);
		container.add(okBox,c);
		
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
		
		pack();
		setLocation(center.x - getSize().width / 2, center.y - getSize().height
				/ 2);
	}
	
	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}
	
	public String getType(){
		if(tb1.isSelected()) return tb1.getText();
		if(tb2.isSelected()) return tb2.getText();
		if(tb3.isSelected()) return tb3.getText();
		if(tb4.isSelected()) return tb4.getText();
		return null;
	}
	
	public String getAttName(){
		return taName.getText();
	}
	
	public void enterPressed() {
		boolean OK = true;
		if(taName.getText().length() == 0){
			JOptionPane.showMessageDialog(this
					,"You haven't entered the name of the attribute.\nPlease enter a name."
					,"Attribute name missing",
					JOptionPane.INFORMATION_MESSAGE);
			OK = false;
		}else if(!(tb1.isSelected() || tb2.isSelected() || tb3.isSelected() || tb4.isSelected())){
			OK = false;
			JOptionPane.showMessageDialog(this
					,"You haven't chosen the type of the attribute.\nPlease choose the type from the list below."
					,"Attribute type missing",
					JOptionPane.INFORMATION_MESSAGE);
		}else{
			for(String s : existingProps){
				if(s.equals(taName.getText())){
					JOptionPane.showMessageDialog(this
							,"An attribute with this name already exists.\nChoose another name."
							,"Attribute exists",
							JOptionPane.INFORMATION_MESSAGE);
					taName.setText("");
					OK = false;
				}
			}	
		}
		if(OK){
			setDialogResult(true);
			setVisible(false);
		}	
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}
	
}
