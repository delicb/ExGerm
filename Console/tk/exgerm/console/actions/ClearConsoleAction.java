package tk.exgerm.console.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.console.gui.Console;

@SuppressWarnings("serial")
public class ClearConsoleAction extends AbstractAction {

	private Console console;

	public ClearConsoleAction(Console console) {
		putValue(NAME, "Clear Console");
		putValue(SHORT_DESCRIPTION, "Clears this console window");
		this.console = console;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		console.clearConsole();
	}

}
