package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.windows.GraphAttributesWindow;

@SuppressWarnings("serial")
public class PropertiesAction extends ExGAction {

	private ViewManager viewManager;
	private ICoreContext context;
	
	public PropertiesAction(ViewManager _vm, ICoreContext _context){
		putValue(NAME, "Properties"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Graph properties"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/properties.png"));
		viewManager = _vm;
		context = _context;
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
			GraphAttributesWindow window = new GraphAttributesWindow(viewManager.getActiveView().getModel().getGraph(), context);
			window.setVisible(true);
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
