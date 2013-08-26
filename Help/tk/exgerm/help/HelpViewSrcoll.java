package tk.exgerm.help;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import tk.exgerm.core.plugin.ExGGraphicalComponent;

@SuppressWarnings("serial")
public class HelpViewSrcoll extends JScrollPane implements ExGGraphicalComponent {

	private HelpView view;
	
	public HelpViewSrcoll(HelpView view){
		super(view);
		setName("Help");
		this.view = view;
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	public HelpView getHelpView(){
		return view;
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public int getPosition() {
		return ExGGraphicalComponent.EAST;
	}

	@Override
	public boolean isTabNameChangeable() {
		return false;
	}

}
