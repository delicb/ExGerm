package tk.exgerm.ucsearch;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class UCSearchAction extends ExGAction {

	ICoreContext context;
	UCSearchService service;
	UCSearch search;

	public UCSearchAction(ICoreContext context, UCSearchService service) {
		putValue(NAME, "UC Search");
		putValue(SMALL_ICON, loadIcon("images/Search.png"));
		putValue(SHORT_DESCRIPTION, "Uniform cost search.");
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
		return 610;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			IGraph graph = (IGraph) context.getData(IGraph.ACTIVE_GRAPH);
			UCSearchChooser ucs = new UCSearchChooser(graph);
			ucs.setVisible(true);
			ArrayList<Object> result = null;
			if (ucs.isDialogResult()) {
				String att = ucs.getAttribute();
				search = new UCSearch(att, graph.getNode(ucs.getFrom()), graph.getNode(ucs.getTo()));
				result = search.search();
				if (result == null) {
					JOptionPane.showMessageDialog(null,
						"No possible route. Nodes might not be connected or all connections are directed from finish to start.",
						"No Result",JOptionPane.INFORMATION_MESSAGE);
				} else {
					context.raise(ExGIterator.SEARCH_RESULT_FOUND, graph, result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
