package tk.exgerm.console.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.console.gui.Console;

@SuppressWarnings("serial")
public class PasteAction extends AbstractAction{
	
	private Console console;

	public PasteAction(Console console) {
		putValue(NAME, "Paste (CTRL-V)");
		putValue(SHORT_DESCRIPTION, "Paste copied text");
		this.console = console;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		console.doPaste();
	}

}
