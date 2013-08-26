package tk.exgerm.dabsearch.util;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.dabsearch.DABSService;
import tk.exgerm.dabsearch.Search;
import tk.exgerm.dabsearch.gui.DaBSearchChooser;

@SuppressWarnings("serial")
public class SearchAction extends ExGAction {

	ICoreContext context;
	DABSService service;
	
	public SearchAction(ICoreContext context, DABSService service) {
		putValue(NAME, "DF and BF Search"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/Find.png"));
		putValue(SHORT_DESCRIPTION, "Breadth and Depth first searches."); //$NON-NLS-1$
		this.context = context;
		this.service = service;
	}
	
	
	@Override
	public int getActionPosition() {
		return ExGAction.MENU | ExGAction.TOOLBAR;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public int getToolbarPosition() {
		return 600;
	}

	@Override
	public String getMenu() {
		return ExGAction.PLUGINS_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 600;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		IGraph g = (IGraph) context.getData(IGraph.ACTIVE_GRAPH);
		DaBSearchChooser dbs = new DaBSearchChooser(g);
		dbs.setVisible(true);
		if(dbs.isDialogResult()){
			//pokrenu pretragu
			INode start = g.getNode(dbs.getFrom());
			INode finish = g.getNode(dbs.getTo());
			Search search = new Search(g,start,finish);
			ArrayList<INode> result = null;
			try {
				result = search.search(dbs.getSearch());
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Node notfound.","No Result",JOptionPane.INFORMATION_MESSAGE);
			}
			//ispis ako nema
			if(result == null){
				JOptionPane.showMessageDialog(null, "No possible route. Nodes might not be connected or all connections are directed from finish to start.","No Result",JOptionPane.INFORMATION_MESSAGE);
			}else{
				context.raise(ExGIterator.SEARCH_RESULT_FOUND, g, result);
			}
		}
	}

}
