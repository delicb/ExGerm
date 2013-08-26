package tk.exgerm.visualiser.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.windows.NewGraphWindow;

@SuppressWarnings("serial")
public class NewGraphAction extends ExGAction {

	private ICoreContext context;
	
	public NewGraphAction(ICoreContext _context){
		putValue(NAME, "New graph..."); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/newgraph.png"));
		putValue(SHORT_DESCRIPTION, "New graph"); //$NON-NLS-1$
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		context = _context;
	}

	@Override
	public int getActionPosition() {
		return ExGAction.TOOLBAR | ExGAction.MENU;
	}

	@Override
	public String getMenu() {
		return ExGAction.FILE_MENU;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		NewGraphWindow window = new NewGraphWindow(context);
		window.setVisible(true);
		
		if(window.isDialogResult()){
			String graphName = window.getTfName().toString();
			IGraph graph = context.newGraph(graphName);
			Activator.visService.setNewActionGraph(true);
			
			try {
				context.addGraph(graph);
			} catch (ExGGraphExsistException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	@Override
	public int getMenuPostition() {
		return 100;
	}

	@Override
	public int getToolbarPosition() {
		return 400;
	}

}
