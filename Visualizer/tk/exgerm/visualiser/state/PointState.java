package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.INode;
import tk.exgerm.visualiser.model.VisEdge;
import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.windows.EdgeAttributesWindow;
import tk.exgerm.visualiser.windows.NodeAttributeWindow;

public class PointState extends State{
	
	private Point2D lastPosition;
	private int nodeInMotion = -1;
	private boolean button;
	private boolean drag;
	
	private VisNode mouseOverNode = null;

	@Override
	public void initialise(MouseEvent e) {
		getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public PointState(StateManager stateManager){
		this.stateManager = stateManager;
	}
	
	public void mouseMoved(MouseEvent e) {
		Rectangle r = getView().getNavigator().getBounds();
		
		if(r != null && r.contains(e.getPoint())){
			stateManager.setNextState(StateManager.States.NAVIGATE, e);
		}
		
		lastPosition = e.getPoint();
		getView().transformToUserSpace(lastPosition);
		nodeInMotion = getModel().getNodeAtPosition(lastPosition);
		if (nodeInMotion != -1) {
			VisNode n = getModel().getNodeAt(nodeInMotion);
			// radicemo repaint samo ako je mis na novom nodu
			if (mouseOverNode == null) {
				mouseOverNode = n;
				getView().setMouseOverNode(n);
				getView().repaint();
			}
			// ili ako je nod promenjen
			if (n != mouseOverNode) {
				getView().setMouseOverNode(n);
				getView().repaint();
			}
		} else {
			if((mouseOverNode != null)){
				mouseOverNode = null;
				getView().setMouseOverNode(null);
				getView().repaint();
			}
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		lastPosition = e.getPoint();
		getView().transformToUserSpace(lastPosition);
		
		if (e.getButton() == MouseEvent.BUTTON1){
			button = true;
			nodeInMotion = getModel().getNodeAtPosition(lastPosition);
			
			if (nodeInMotion != -1) {
				if(e.isControlDown() || getModel().getSelectedNodes().size() == 0){
					getModel().selectNodeAtPosition(lastPosition);
				}
				
				if(!e.isControlDown() ){
					if(!getModel().getSelectedNodes().contains(getModel().getNodeAt(nodeInMotion))){
						getModel().deselectAllNodes();
						getModel().selectNodeAtPosition(lastPosition);
					}
				}
			}else{
				stateManager.setNextState(StateManager.States.LASSO, e);
			}
			
			if ((nodeInMotion == -1 && !e.isControlDown())){
				getModel().deselectAllNodes();
			}
			
			if(nodeInMotion!=-1){
				if(!getModel().getSelectedNodes().contains(getModel().getNodeAt(nodeInMotion)) && !e.isControlDown()){
					getModel().deselectAllNodes();
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if(drag){
			getModel().deselectAllNodes();
			drag = false;
		}
		super.mouseReleased(e);	// Drag na srednje dugme
		nodeInMotion = -1;
		button = false;
		getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);	// Drag na srednje dugme

		drag = true;
		if (button){
			getView().visualization.start();
			getView().setVisual(true);
			getView().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		Point2D mousePos = e.getPoint();
		getView().transformToUserSpace(mousePos);
		for (VisNode n : getModel().getSelectedNodes()) {
			Point2D newPosition = (Point2D) n.getPosition().clone();
			newPosition.setLocation(newPosition.getX() + (mousePos.getX() - lastPosition.getX()),
					newPosition.getY()	+ (mousePos.getY() - lastPosition.getY())
			);
			getModel().updateNodePosition(n, newPosition);
		}
		getView().repaint();
		lastPosition = mousePos;
	}
	
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		if(e.getClickCount() == 2){
			lastPosition = e.getPoint();
			getView().transformToUserSpace(lastPosition);

			nodeInMotion = getModel().getNodeAtPosition(lastPosition);
			if (nodeInMotion != -1) {
				getModel().deselectAllNodes();
				INode node = getModel().getGraph().getNode(getModel().getNodeAt(nodeInMotion).getName());
				NodeAttributeWindow window = new NodeAttributeWindow(node, getModel().getContext());
				window.setVisible(true);
				return;
			}
			
			VisEdge visEdge = getModel().cathcEdge(lastPosition);
			if(visEdge != null){
				IEdge edge = getModel().getGraph().getEdge(visEdge.getId());
				EdgeAttributesWindow window = new EdgeAttributesWindow(edge, getModel().getContext());
				window.setVisible(true);
			}
		}
	}



}
