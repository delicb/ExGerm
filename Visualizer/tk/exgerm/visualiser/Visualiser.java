package tk.exgerm.visualiser;

import javax.swing.JComponent;

import tk.exgerm.core.plugin.ExGGraphicalComponent;

public class Visualiser implements ExGGraphicalComponent {

	JComponent component;
	
	public Visualiser(JComponent component) {
		this.component = component;
	}
	
	@Override
	public JComponent getComponent() {
		return this.component;
	}

	@Override
	public int getPosition() {
		return ExGGraphicalComponent.CENTER;
	}

	@Override
	public boolean isTabNameChangeable() {
		return true;
	}

}
