package tk.exgerm.visualiser.windows;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NewNodeWindow extends NewGraphWindow {

	private IGraph graph;
	private int index;
	
	public NewNodeWindow(ICoreContext context, IGraph graph, int index){
		super(context);
		this.graph = graph;
		this.index = index;
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		
		setTitle("New node / subgraph");
		
		Container container = this.getContentPane();
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel(this.graph.getName()), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(new JLabel("Node name:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		container.add(tfName, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(15, 20, 20, 10);

		okBox.add(btnOK);
		okBox.add(Box.createHorizontalStrut(10));
		okBox.add(btnCancel);
		container.add(okBox,c);
		
		pack();
		
	}

	@Override
	public void enterPressed() {
		
		if(getTfName().length() == 0){
			JOptionPane.showMessageDialog(this,
					"You have not entered the name of the graph.\nThe node must have a name.",
					"Name missing", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(graph.getNode(getTfName()) != null){
			JOptionPane.showMessageDialog(this,
					"A node or a subgraph with this name already exists.\nPlease choose another name.",
					"Name exists", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		setDialogResult(true);
		if(index == 0)
			try {
				this.graph.addNode(context.newNode(getTfName()));
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		else if (index == 1)
			try {
				this.graph.addNode(context.newGraph(getTfName()));
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		setVisible(false);
	}
	
}
