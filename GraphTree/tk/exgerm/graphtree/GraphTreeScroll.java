package tk.exgerm.graphtree;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import tk.exgerm.core.plugin.ExGGraphicalComponent;

@SuppressWarnings("serial")
public class GraphTreeScroll extends JScrollPane implements ExGGraphicalComponent{

	public GraphTreeScroll(GraphTree tree){
		super(tree);
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public int getPosition() {
		return ExGGraphicalComponent.WEST;
	}

	@Override
	public String getName() {
		return "GraphTree";
	}

	@Override
	public boolean isTabNameChangeable() {
		return true;
	}
}
