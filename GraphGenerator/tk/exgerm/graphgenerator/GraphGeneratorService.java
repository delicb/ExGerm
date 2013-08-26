package tk.exgerm.graphgenerator;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.graphgenerator.help.GraphGeneratorHelp;

public class GraphGeneratorService implements IComponent {

	ICoreContext coreContext;
	BundleContext bundleContext;
	
	/*Akcije GraphGeneratora*/
	GraphGeneratorAction action;
	
	/*Help GraphGeneratora*/
	GraphGeneratorHelp help;
	
	public GraphGeneratorService(BundleContext context) {
		this.bundleContext = context;
	}
	
	@Override
	public void setContext(ICoreContext context) {
		this.coreContext = context;		
		this.action = new GraphGeneratorAction(coreContext, this);
		this.coreContext.addAction(action);
		this.help = new GraphGeneratorHelp();
		this.coreContext.registerHelp(help);
	}
	
	/**
	 * Metoda koja iz CoreContexta uklanja sve što je ova komponenta prijavila.
	 */
	public void shutDown(){
		coreContext.removeAction(action);
		coreContext.removeHelp(help);
	}
	
}
