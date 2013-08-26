package tk.exgerm.console.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.console.gui.Console;

@SuppressWarnings("serial")
public class CutAction extends AbstractAction{
	
	private Console console;

	public CutAction(Console console) {
		putValue(NAME, "Cut (CTRL-X)");
		putValue(SHORT_DESCRIPTION, "Cut selected text");
		this.console = console;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		console.doCut();
	}

}
