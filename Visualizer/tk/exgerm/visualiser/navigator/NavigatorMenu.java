package tk.exgerm.visualiser.navigator;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import tk.exgerm.core.plugin.ExGSubmenu;
import tk.exgerm.visualiser.ViewManager;

/**
 * Predstavlja pod meni kojim se kontroliše stanje prikaza NAvigator kontrole 
 * 
 * @author Tim 2
 *
 */
public class NavigatorMenu extends JMenu implements ExGSubmenu {

	private static final long serialVersionUID = -3915442249872074345L;
	
	private JRadioButtonMenuItem showAutomatic;
	private JRadioButtonMenuItem showAlways;
	private JRadioButtonMenuItem showNever;
	private ButtonGroup bg;
	
	public NavigatorMenu(ViewManager viewManager){		
		setText("Show Navigation");
		this.bg = new ButtonGroup();

		this.showAutomatic = new JRadioButtonMenuItem(new ShowAutomaticAction(viewManager));
		add(showAutomatic);
		bg.add(showAutomatic);
		
		this.showAlways = new JRadioButtonMenuItem(new ShowAlwaysAction(viewManager));
		add(showAlways);
		bg.add(showAlways);
		
		this.showNever = new JRadioButtonMenuItem(new ShowNeverAction(viewManager));
		add(showNever);
		bg.add(showNever);
	}
	
	@Override
	public JMenu getMenuContent() {
		return this;
	}

	@Override
	public String getMenu() {
		return VIEW_MENU;
	}

	@Override
	public int getMenuPostition() {
		return 300;
	}

	public void setSelectedMenu(int menuIndex){
		switch(menuIndex){
		case 0:
			showAutomatic.setSelected(true);
			break;
		case 1:
			showAlways.setSelected(true);
			break;
		case 2:
			showNever.setSelected(true);
			break;
		default:
		}
	}
	
}
