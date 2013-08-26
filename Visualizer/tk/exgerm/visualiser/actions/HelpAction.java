package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class HelpAction extends ExGAction {

	private ICoreContext context;
	
	public HelpAction(ICoreContext _context){
		putValue(NAME, "Visualizer Help"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/help.png"));
		putValue(SHORT_DESCRIPTION, "Visualizer Help"); //$NON-NLS-1$
		context = _context;
	}

	@Override
	public int getActionPosition() {
		return ExGAction.TOOLBAR;
	}

	@Override
	public String getMenu() {
		return null;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			context.raise(ExGHelp.HELP_REQUESTED, "Visualiser", "Visualiser");
		}catch(Exception ex){

		}
	}

	@Override
	public int getMenuPostition() {
		return 400;
	}

	@Override
	public int getToolbarPosition() {
		return 400;
	}

}
