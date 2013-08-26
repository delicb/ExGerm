package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import tk.exgerm.visualiser.model.VisNode;

public class DirectedEdgeState extends State {

	private int nodeInMotion = -1;
	public Point2D lastPosition;
	private VisNode node1 = null;
	private VisNode node2 = null;
	
	
	public DirectedEdgeState(StateManager _stateManager){
		this.stateManager = _stateManager;
	}

	@Override
	public void initialise(MouseEvent e) {
		getView().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);

		lastPosition = e.getPoint();
		getView().transformToUserSpace(lastPosition);

		if (e.getButton() == MouseEvent.BUTTON1) {
			nodeInMotion = getModel().getNodeAtPosition(lastPosition);
			if (nodeInMotion != -1){
				if(node1 == null)
					node1 = getModel().getNodeAt(nodeInMotion);
				else
					node2 = getModel().getNodeAt(nodeInMotion);
			} else return;
			
			if(node1 != null){
				if (node1 == node2){
					getModel().newEdge(node1, node2, true);
					node1 = null;
					node2 = null;
					getView().getLinker().setCoordinates(false);
					getView().repaint();
					return;
				}
			}
			
			if(node2 != null){
				getModel().newEdge(node1, node2, true);
				node1 = null;
				node2 = null;
				getView().getLinker().setCoordinates(false);
				getView().repaint();
			}
		}
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			node1 = null;
			node2 = null;
			getView().getLinker().setCoordinates(false);
			getView().repaint();
			getView().getStateManager().setNextState(StateManager.States.POINT, e);
		}

	}
	
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);	
		getView().getLinker().setCoordinates(false);
		getView().repaint();
	}
	
	public void mouseMoved(MouseEvent e){
		if(getView().getNavigator().getBounds().contains(e.getPoint())){
			stateManager.setNextState(StateManager.States.NAVIGATE, e);
		}
		
		lastPosition = e.getPoint();
		getView().transformToUserSpace(lastPosition);

		if( node1 != null ){
			getView().getLinker().setCoordinates((int) node1.getPosition().getX() + (int) (node1.getSize().width / 2),
					(int) node1.getPosition().getY() + (int) (node1.getSize().height / 2),
					(int)lastPosition.getX(),(int)lastPosition.getY(), true);
			getView().repaint();
		}

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

}
