package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.state.StateManager;

@SuppressWarnings("serial")
public class PointStateAction extends ExGAction {
	
	private ViewManager viewManager;
	
	public PointStateAction(ViewManager _vm){
		putValue(NAME, "Defrault state"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Defrault state"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/point.png"));
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
			viewManager.getActiveView().getStateManager().setNextState(StateManager.States.POINT, null);
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
