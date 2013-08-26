package tk.exgerm.core.gui.tabmanager.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tk.exgerm.core.gui.tabmanager.TabComponent;
import tk.exgerm.core.gui.tabmanager.TabPanel;

/** 
 * Implementira akciju promene naziva tab-a, konteksnog menija {@link TabPanel tabPanel-a}.
 *   
 * @author Tim 2
 *
 */
public class RenameTabAction implements ActionListener {

	private TabComponent tabComponent;
	
	public RenameTabAction(TabComponent tabComponent){
		this.tabComponent = tabComponent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		tabComponent.setEditMode(true);
	}
}
