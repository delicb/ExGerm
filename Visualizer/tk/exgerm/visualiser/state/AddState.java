package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;


public class AddState extends State {
	
	private Point2D lastPosition;
	private int nodeAtPosition = -1;
	
	public AddState(StateManager stateManager){
		this.stateManager = stateManager;
	}

	@Override
	public void initialise(MouseEvent e) {
		getView().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		getModel().deselectAllNodes();
	}
	
	public void mouseMoved(MouseEvent e) {
		if(getView().getNavigator().getBounds().contains(e.getPoint())){
			stateManager.setNextState(StateManager.States.NAVIGATE, e);
		}
	}

	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);	// Drag na srednje dugme
		lastPosition = e.getPoint();
		getView().transformToUserSpace(lastPosition);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			nodeAtPosition = getModel().getNodeAtPosition(lastPosition);
			if (nodeAtPosition == -1) {
				getModel().newNode(lastPosition);
			}
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			getView().getStateManager().setNextState(StateManager.States.POINT, e);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
	}
	
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

}
