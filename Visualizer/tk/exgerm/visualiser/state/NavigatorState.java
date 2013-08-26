package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import tk.exgerm.visualiser.navigator.Navigator;

public class NavigatorState extends State{
	
	private Navigator navigator;
	
	public Timer moving = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	getView().translateHorizontally(0.05*navigator.getMoveX());
    		getView().translateVertically(-0.05*navigator.getMoveY());
    		getView().repaint();
        }
     });
	
	public Timer zooming = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	int factor = 0;
        	
        	if(navigator.isZoomBarActive()){
        		factor = (int)Math.signum(navigator.getZoomFactor());
        	}else if(navigator.isPlusActive()){
        		factor = 1;
        	}else if(navigator.isMinusActive()){
        		factor = -1;
        	}
        	
        	getView().zoomCenter(factor);
        	getView().repaint();
        }
     });
	
	public NavigatorState(StateManager stateManager){
		this.stateManager = stateManager;
	}
	
	@Override
	public void initialise(MouseEvent e) { 
		this.navigator = getView().getNavigator();
		getView().setCursor(Cursor.getDefaultCursor());
	}
	
	public void mouseMoved(MouseEvent e) {
		if(!navigator.getBounds().contains(e.getPoint())){
			stateManager.setNextState(stateManager.getPreviousState(), null);
		}
		navigator.mouseMoved(e);
	}

	public void mousePressed(MouseEvent e) {
		navigator.mousePressed(e);
		if(navigator.isSpotlightActive()){
			navigator.setEnableRepaint(false);
			moving.start();
		}else if(navigator.isPlusActive() || navigator.isMinusActive()){
			navigator.setEnableRepaint(false);
			zooming.start();
		}
	}

	public void mouseReleased(MouseEvent e) {
		navigator.mouseReleased(e);
		if(!navigator.isSpotlightActive()){
			moving.stop();
			navigator.setEnableRepaint(true);
		} 
		if(!navigator.isZoomBarActive() || !navigator.isMinusActive()){
		zooming.stop();
		navigator.setEnableRepaint(true);
		}
	}

	public void mouseDragged(MouseEvent e) {
		navigator.mouseDragged(e);
		if(navigator.isZoomBarActive() || navigator.isPlusActive()){
			navigator.setEnableRepaint(false);
			zooming.start();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
			if(navigator.isZoomBarHover()){
				getView().setBestFit();
			}
		}
	}
}
