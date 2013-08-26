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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NewGraphWindow extends JDialog {

	protected boolean dialogResult = false;

	protected Container container = getContentPane();
	protected JButton btnOK = new JButton("OK");
	protected JButton btnCancel = new JButton("Cancel");
	protected Box okBox = Box.createHorizontalBox();
	protected JTextField tfName = new JTextField(20);
	
	protected ICoreContext context;

	public NewGraphWindow(ICoreContext _context) {
		this.context = _context;

		setModal(true);
		setTitle("New graph");
		setLayout(new GridBagLayout());
		setResizable(false);
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Graph name:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(tfName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(15, 20, 20, 10);

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

	public String getTfName() {
		return tfName.getText();
	}

	public boolean isDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(boolean dialogResult) {
		this.dialogResult = dialogResult;
	}

	public void enterPressed() {
	
		if(getTfName().length() == 0){
			JOptionPane.showMessageDialog(this,
					"You have not entered the name of the graph.\nThe graph must have a name.",
					"Name missing", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(context.getGraph(getTfName().toString()) != null){
			JOptionPane.showMessageDialog(this,
					"A graph with this name already exists.\nPlease choose another name.",
					"Name exists", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		setDialogResult(true);
		setVisible(false);
	}

	public void escapePressed() {
		setDialogResult(false);
		setVisible(false);
	}

}
