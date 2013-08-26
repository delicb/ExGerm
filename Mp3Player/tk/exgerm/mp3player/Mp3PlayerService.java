package tk.exgerm.mp3player;

import java.util.ArrayList;
import java.util.List;

import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.mp3player.help.Mp3Help;

public class Mp3PlayerService implements IComponent {
	
	private List<ExGAction> actions = new ArrayList<ExGAction>();
	private ICoreContext context;
	private ExGHelp help;

	public Mp3PlayerService() {

	}

	@Override
	public void setContext(ICoreContext context) {
		this.context = context;
		registerActions();
		help = new Mp3Help();
		this.context.registerHelp(help);
	}

	private void registerActions() {
		registerAction(new PlayAction());
	}

	private void registerAction(ExGAction action) {
		context.addAction(action);
		actions.add(action);
	}
	
	public void stop() {
		for (ExGAction action : actions) {
			context.removeAction(action);
		}
		this.context.removeHelp(help);
	}

}
