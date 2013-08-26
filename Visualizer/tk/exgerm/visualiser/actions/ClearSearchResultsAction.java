package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.view.VisualiserView;

@SuppressWarnings("serial")
public class ClearSearchResultsAction extends ExGAction {

	private ViewManager viewManager;
	
	public ClearSearchResultsAction(ViewManager _vm){
		putValue(NAME, "Clear Search"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/clear.png"));
		putValue(SHORT_DESCRIPTION, "Remove search markers"); //$NON-NLS-1$
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
		VisualiserView v = viewManager.getActiveView();
		v.getModel().removeSearchResults();
		v.repaint();		
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
