package tk.exgerm.console.actions;

import java.awt.event.ActionEvent;

import tk.exgerm.console.ConsoleService;
import tk.exgerm.console.ConsoleView;
import tk.exgerm.console.gui.Console;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class NewConsoleAction extends ExGAction{
	
	private ICoreContext context;
	private ConsoleService service;
	
	public NewConsoleAction(ICoreContext context, ConsoleService service) {
		putValue(NAME, "New console"); //$NON-NLS-1$
		putValue(SMALL_ICON, loadIcon("images/console.png"));
		putValue(SHORT_DESCRIPTION, "Create new console"); //$NON-NLS-1$
		this.context = context;
		this.service = service;
	}

	@Override
	public int getActionPosition() {
		return ExGAction.MENU | ExGAction.TOOLBAR;
	}

	@Override
	public String getMenu() {
		return ExGAction.VIEW_MENU;
	}

	@Override
	public String getToolbar() {
		return ExGAction.MAIN_TOOLBAR;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Console console = new Console(context, service.getAliases(), "Console "
				+ service.getUniqueID());
		ConsoleView consoleView = new ConsoleView(console, service.getUniqueID());
		service.getConsoleViews().add(consoleView);
		context.addGraphicalComponent(consoleView);
	}

	@Override
	public int getMenuPostition() {
		return 400;
	}

	@Override
	public int getToolbarPosition() {
		return 500;
	}

}
