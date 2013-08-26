package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.state.StateManager;

@SuppressWarnings("serial")
public class DirectedEdgeStateAction extends ExGAction {

	private ViewManager viewManager;
	
	public DirectedEdgeStateAction(ViewManager _vm){
		putValue(NAME, "Dir.Edge State"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/edgedirected.png"));
		putValue(SHORT_DESCRIPTION, "Add new directed edge"); //$NON-NLS-1$
		viewManager = _vm;
	}

	@Override
	public int getActionPosition() {
		return ExGAction.TOOLBAR | ExGAction.MENU;
	}

	@Override
	public String getMenu() {
		return ExGAction.EDIT_MENU;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			viewManager.getActiveView().getStateManager().setNextState(StateManager.States.DIR_EDGE, null);
		}catch(Exception e){
			
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
