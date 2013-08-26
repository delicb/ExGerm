package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class HelpAction extends AbstractAction {

	private ICoreContext context;
	
	public HelpAction(ICoreContext context) {
		putValue(NAME, "Help");
		putValue(SHORT_DESCRIPTION, "Open the help for GraphTree");
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		context.raise(ExGHelp.HELP_REQUESTED, "GraphTree", "GraphTree");
	}
}
