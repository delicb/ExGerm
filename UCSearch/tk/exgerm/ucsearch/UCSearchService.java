package tk.exgerm.ucsearch;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.ucsearch.help.UCSearchHelp;

public class UCSearchService implements IComponent {
	
	private ICoreContext context;
	private ExGCommand command;
	private ExGAction action;	
	@SuppressWarnings("unused")
	private BundleContext bundleContext;
	private UCSearchHelp help;
	

	public UCSearchService(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;		
	}

	@Override
	public void setContext(ICoreContext context) {
		this.context = context;
		this.command = new UCSCommand(context);
		this.action = new UCSearchAction(context,this);
		this.context.addAction(action);
		this.help = new UCSearchHelp();
		this.context.registerHelp(help);
		try {
			context.registerCommand(command);
		} catch (ExGCommandAlreadyExistException e) {
			e.printStackTrace();
		}
	}

	public void shutDown() {
		context.removeCommand(command);
		context.removeAction(action);
		context.removeHelp(help);
	}

}


