package tk.exgerm.console;

import javax.swing.JComponent;

import tk.exgerm.core.plugin.ExGGraphicalComponent;

public class ConsoleView implements ExGGraphicalComponent {

	JComponent component;
	int ID;

	public ConsoleView(JComponent component, int ID) {
		this.component = component;
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}

	@Override
	public JComponent getComponent() {
		return this.component;
	}

	@Override
	public int getPosition() {
		return ExGGraphicalComponent.SOUTH;
	}

	@Override
	public boolean isTabNameChangeable() {
		return true;
	}
}
