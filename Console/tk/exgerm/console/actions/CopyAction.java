package tk.exgerm.console.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.console.gui.Console;

@SuppressWarnings("serial")
public class CopyAction extends AbstractAction{
	
	private Console console;

	public CopyAction(Console console) {
		putValue(NAME, "Copy (CTRL-C)");
		putValue(SHORT_DESCRIPTION, "Copy selected text");
		this.console = console;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		console.doCopy();
	}

}
