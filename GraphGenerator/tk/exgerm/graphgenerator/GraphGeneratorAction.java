package tk.exgerm.graphgenerator;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class GraphGeneratorAction extends ExGAction{
	
	private ICoreContext context;
	private GraphGenerator generator;
	
	public GraphGeneratorAction(ICoreContext context, GraphGeneratorService service) {
		putValue(NAME, "Graph generator");
		putValue(SMALL_ICON, loadIcon("images/generator.png"));
		putValue(SHORT_DESCRIPTION, "Generate a predefined graph");
		this.context = context;
		
	}
	
	@Override
	public int getActionPosition() {
		return ExGAction.MENU;
	}
	@Override
	public String getMenu() {
		return ExGAction.PLUGINS_MENU;
	}
	@Override
	public String getToolbar() {
		return null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.generator = new GraphGenerator(context);
		generator.setVisible(true);
	}

	@Override
	public int getMenuPostition() {
		return 300;
	}

	@Override
	public int getToolbarPosition() {
		return -1;
	}

}
