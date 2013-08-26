package tk.exgerm.about;

import org.osgi.framework.BundleContext;

import tk.exgerm.core.plugin.IComponent;
import tk.exgerm.core.service.ICoreContext;

public class AboutService implements IComponent {

	ICoreContext coreContext;
	BundleContext bundleContext;
	AboutWindow window;
	AboutAction action;
	
	public AboutService(BundleContext context) {
		this.bundleContext = context;
	}
	
	@Override
	public void setContext(ICoreContext coreContext) {		
		this.coreContext = coreContext;		
		this.action = new AboutAction(coreContext, this);
		this.coreContext.addAction(action);
		this.window = new AboutWindow();
	}
	
	public void shutDown(){
		this.coreContext.removeAction(action);
	}

}
