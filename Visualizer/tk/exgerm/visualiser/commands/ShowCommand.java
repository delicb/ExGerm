package tk.exgerm.visualiser.commands;

import java.io.PrintStream;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;

public class ShowCommand implements ExGCommand {

	private ICoreContext context;
	private ViewManager viewManager;

	public ShowCommand(ICoreContext _context, ViewManager _viewManager) {
		super();
		this.context = _context;
		this.viewManager = _viewManager;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		if (params.length == 1) {
			INode n = (INode) context.getData(CONSOLE_ACTIVE_NODE);
			if (n != null){
				try {
					Activator.visService.addVisualiser(viewManager.getSubView(n.getFinalRoot().getName(), n.getLevel(), n.getName()));
				} catch (Exception e) {
					throw new ExGCommandErrorException("Visualizer error! (1)");
				}
			}else{			
				IGraph g = (IGraph) context.getData(CONSOLE_ACTIVE_GRAPH);
				if (g != null)
					try {
						Activator.visService.addVisualiser(viewManager.getRootView(g.getName()));
					} catch (Exception e) {
						throw new ExGCommandErrorException("Visualizer error! (2)");
					}
			}
		} else
			throw new ExGCommandErrorException("Show command don't require parameters!");
		return null;
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public String getKeyword() {
		return "show";
	}

	@Override
	public String getSyntax() {
		return null;
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}
}
