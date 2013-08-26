package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.visualiser.ViewManager;

@SuppressWarnings("serial")
public class BestFitAction extends ExGAction {

	private ViewManager viewManager;
	
	public BestFitAction(ViewManager _vm){
		putValue(NAME, "Set BestFit"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/bestfit.png"));
		putValue(SHORT_DESCRIPTION, "BestFit"); //$NON-NLS-1$
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
			viewManager.getActiveView().setBestFit();
			viewManager.getActiveView().repaint();
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
