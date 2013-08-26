package tk.exgerm.dabsearch;


import java.util.ArrayList;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.dabsearch.help.DABHelp;
import tk.exgerm.dabsearch.util.BFSCommand;
import tk.exgerm.dabsearch.util.DFSCommand;
import tk.exgerm.dabsearch.util.SearchAction;

public class DABSService implements IComponent {

	private ICoreContext context;
	
	/*
	 * OSGI context u okviru koga se pokrece BFS
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;
	
	private ArrayList<ExGCommand> commands;
	private ExGAction action;
	
	private DABHelp help;
	
	public DABSService(BundleContext bundleContext) {
		super();
		this.bundleContext = bundleContext;
		commands = new ArrayList<ExGCommand>();
		help = new DABHelp();
		
	}

	@Override
	public void setContext(ICoreContext context) {
		this.context = context;
		
		context.registerHelp(help);
		commands.add(new BFSCommand(context));
		commands.add(new DFSCommand(context));
		action = new SearchAction(context,this);
		context.addAction(action);
		for(ExGCommand command : commands)
			try {
				context.registerCommand(command);
			} catch (ExGCommandAlreadyExistException e) {
				
				e.printStackTrace();
			}
	}
	
	public void unregisterCommands(){
		for(ExGCommand command : commands){
			context.removeCommand(command);
		}
		commands.clear();
	}

	public void shutDown() {
		unregisterCommands();
		context.removeHelp(help);
		context.removeAction(action);
	}

}
