package tk.exgerm.core.gui.tabmanager.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.TransferHandler;

import tk.exgerm.core.gui.tabmanager.TabComponent;
import tk.exgerm.core.gui.tabmanager.TabPanel;

/**
 * Reaguje na događaje miša na tabbed pane-u
 * 
 * @author Tim 2
 */
public class TabComponentMouseListener extends MouseAdapter {

	TabPanel tabPanel;

	/**
	 * Konstruktor, samo inicijalizuje
	 * 
	 * @param tabPanel {@link TabPanel tabPanel} čiji se događaji miša obrađuju 
	 */
	public TabComponentMouseListener(TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

	/**
	 * Reakcija na wheel dogadjaj miša i to:<br><br>
	 * 
	 * <ul>
	 *  <li><b>WheelRotation > 0</b> - selektuje sledeći tab u odnosu na trenutno selektovani</li>
	 *  <li><b>WheelRotation < 0</b> - selektuje prethodni tab u odnosu na trenutno selektovani</li>
	 * </ul>
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		
		int newSelectionIndex = e.getWheelRotation()+tabPanel.getSelectedIndex();		
		if(newSelectionIndex < 0)
			newSelectionIndex = tabPanel.getTabCount() - 1;
		if(newSelectionIndex > tabPanel.getTabCount()-1)
			newSelectionIndex = 0;

		try {
			tabPanel.setSelectedIndex(newSelectionIndex);
		}catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Prosleđuje drag događaj {@link TransferHandler TransferHandler-u} 
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

		if(tabPanel.indexAtLocation(e.getX(), e.getY()) != -1){
			    TransferHandler th = tabPanel.getTransferHandler();
			    th.exportAsDrag(tabPanel, e, TransferHandler.MOVE);
	     }
	}
	
	/**
	 * 	Obezbeđuje selekciju tab-a klikom miša.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		TabComponent tabComponent = (TabComponent)e.getComponent();
		
		if(tabPanel.indexOfTabComponent(tabComponent) != tabPanel.getSelectedIndex())
			try {
				tabPanel.setSelectedIndex(tabPanel.indexOfTabComponent(tabComponent));
			}catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}
		super.mousePressed(e);
	}
	
	/**
	 * Obezbeđuje zatvaranje tab-a klikom miša. Zatvaranje se obavlja:
	 * <ul>

	 *  <li><b>wheel klik</b> -  zatvara tab koji je prouzrokovao dogadjaj</li>
	 *  <li><b>desni dupli klik</b> - zatvara tab koji je prouzrokovao dogadjaj</li>
	 * </ul>
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		TabComponent tabComponent = null;
		try {
			tabComponent = (TabComponent)e.getComponent();
		}catch (ClassCastException ex) {
			ex.printStackTrace();
			return;
		}
		if(e.getClickCount() == 1){ // jedan klik misem?
			if (e.getButton() == MouseEvent.BUTTON2){ // srednji taster?
				tabComponent.getCloseButton().doClick();
			}
		}else // dupli klik
			if(e.getButton() == MouseEvent.BUTTON1){ // levi taster?
				tabComponent.getCloseButton().doClick();
		}
	}
}
