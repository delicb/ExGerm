package tk.exgerm.core.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;

import tk.exgerm.core.plugin.ExGStatusbarContent;

public class DefaultStatusbarLabel implements ExGStatusbarContent {

	JLabel label = new JLabel("Welcome to ExGerm");
	
	@Override
	public JComponent getContent() {
		return label;
	}

	@Override
	public int getPosition() {
		return ExGStatusbarContent.LEFT;
	}
	
	public void setMessage(String message) {
		this.label.setText(message);
	}

}
