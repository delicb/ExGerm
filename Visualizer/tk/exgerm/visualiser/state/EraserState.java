package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class EraserState extends State {
	
	private Point2D lastPosition;
	private int nodeInMotion = -1;
	private boolean button1 = false;

	
	public EraserState(StateManager stateManager){
		this.stateManager = stateManager;
	}

	@Override
	public void initialise(MouseEvent e) {
		getView().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getModel().deselectAllNodes();
	}
	
	public void mouseMoved(MouseEvent e) {
		if(getView().getNavigator().getBounds().contains(e.getPoint())){
			stateManager.setNextState(StateManager.States.NAVIGATE, e);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			button1 = true;
			lastPosition = e.getPoint();
			getView().transformToUserSpace(lastPosition);
			nodeInMotion = getModel().getNodeAtPosition(lastPosition);
			if (nodeInMotion != -1) {
				getModel().deleteNode(getModel().getNodeAt(nodeInMotion));
			}
			getModel().eraseEdge(lastPosition);
		} else 	if (e.getButton() == MouseEvent.BUTTON3)
					getView().getStateManager().setNextState(StateManager.States.POINT, e);
				else
					button1 = false;
	}
	
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		nodeInMotion = -1;
	}
	
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		Point2D mousePos = e.getPoint();
		getView().transformToUserSpace(mousePos);
		
		if (button1) {
			nodeInMotion = getModel().getNodeAtPosition(lastPosition);
			if (nodeInMotion != -1) {
				getModel().deleteNode(getModel().getNodeAt(nodeInMotion));
			}
		}
		getModel().eraseEdge(lastPosition);
		lastPosition = mousePos;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

}
