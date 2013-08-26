package tk.exgerm.visualiser.windows;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class GraphAttributesWindow extends NodeAttributeWindow {
	
	public GraphAttributesWindow(IGraph graph, ICoreContext context){
		super(graph, context);
		setTitle("Graph propertie");
		try {
			setIconImage(ImageIO.read(getClass().getResource("images/icon16.png")));
		} catch (IOException e1) {}
		getContentPane().remove(2);
		
		this.lblGraph = new JLabel("Parent graph:");
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 10);

		getContentPane().add(lblGraph, c);
	}

}
