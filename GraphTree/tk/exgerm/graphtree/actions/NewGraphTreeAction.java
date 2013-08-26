package tk.exgerm.graphtree.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphtree.GraphTree;
import tk.exgerm.graphtree.GraphTreeScroll;
import tk.exgerm.graphtree.GraphTreeService;

@SuppressWarnings("serial")
public class NewGraphTreeAction extends ExGAction {

	private ICoreContext context;
	private GraphTreeService service;
	
	public NewGraphTreeAction(ICoreContext context, GraphTreeService service) {
		putValue(NAME, "Open GraphTree"); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, "Open a new GraphTree view"); //$NON-NLS-1$
		this.context = context;
		this.service = service;
	}
	
	@Override
	public int getActionPosition() {
		return ExGAction.MENU;
	}

	@Override
	public String getToolbar() {
		return null;
	}

	@Override
	public int getToolbarPosition() {
		return 0;
	}

	@Override
	public String getMenu() {
		return  ExGAction.VIEW_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 500;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		GraphTree newtree = new GraphTree(context);
		GraphTreeScroll gts = new GraphTreeScroll(newtree);
		service.reinitializeListeners(newtree);
		context.addGraphicalComponent(gts);	
	}

}
