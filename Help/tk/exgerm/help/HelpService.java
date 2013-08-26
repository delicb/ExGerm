package tk.exgerm.help;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.help.listeners.HelpClosedListener;
import tk.exgerm.help.listeners.HelpRequestListener;

public class HelpService implements IComponent {

	private ICoreContext coreContext;

	/*Listeneri komponente*/
	private IListener helpRequest;
	private IListener helpClosed;
	
	/*Grafička komponenta helpa
	 * SCrollPane koji sadrži u sebi JEditorPane koji parsira HTML stranice*/
	private HelpViewSrcoll scroll;

	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/*Akcije ove komponente*/
	private HelpAction action;

	public HelpService(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void setContext(ICoreContext context) {
		this.coreContext = context;
		action = new HelpAction(this, coreContext);
		context.addAction(action);
		initializeListeners();
		coreContext.listenEvent(ExGHelp.HELP_REQUESTED, helpRequest);
		coreContext.listenEvent(ExGGraphicalComponent.TAB_CLOSED, helpClosed);
	}
	
	public HelpViewSrcoll getScroll(){
		return scroll;
	} 
	
	public void setScroll(HelpViewSrcoll scroll){
		this.scroll = scroll;
	}
	
	/**
	 * Metoda inicijalizuje sve listenere potrebne za ovu komponentu
	 */
	public void initializeListeners(){
		helpRequest = new HelpRequestListener(coreContext, this);
		helpClosed = new HelpClosedListener(coreContext, this);
	}

	/**
	 * Metoda čisti sve listenere, akciju i grafičku komponentu koje je ova
	 * komponenta prijavila CoreContextu.
	 */
	public void shutDown() {
		coreContext.removeGraphicalComponent(scroll);
		coreContext.removeAction(action);
		coreContext.removeListener(helpRequest);
		coreContext.removeListener(helpClosed);
	}

}
