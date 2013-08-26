package tk.exgerm.about;

import java.awt.event.ActionEvent;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.service.ICoreContext;

@SuppressWarnings("serial")
public class AboutAction extends ExGAction {

	private AboutWindow window;
	
	public AboutAction(ICoreContext context, AboutService service) {
		putValue(NAME, "About");
		putValue(SHORT_DESCRIPTION, "About exGERM window");
		putValue(SMALL_ICON, loadIcon("images/about.png"));	
	}
	
	@Override
	public int getActionPosition() {
		return ExGAction.MENU;
	}
	@Override
	public String getMenu() {
		return ExGAction.HELP_MENU;
	}
	@Override
	public String getToolbar() {
		return null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.window = new AboutWindow();
		window.setVisible(true);
	}

	@Override
	public int getMenuPostition() {
		return 1100;
	}

	@Override
	public int getToolbarPosition() {
		return -1;
	}

}
