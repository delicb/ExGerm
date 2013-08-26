package tk.exgerm.visualiser.state;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LassoState extends State {
	
	private int x,y,x1,y1;
	Rectangle2D rec2 = new Rectangle();
	
	public LassoState(StateManager _stateManager){
		this.stateManager = _stateManager;
	}


	@Override
	public void initialise(MouseEvent e) {
		Point2D mousePos = e.getPoint();
		getView().transformToUserSpace(mousePos);
		
		x=e.getX();
		y=e.getY();
		x1=(int)mousePos.getX();
		y1=(int)mousePos.getY();
	}
	
	public void mouseMoved(MouseEvent e) {
		if(getView().getNavigator().getBounds().contains(e.getPoint())){
			stateManager.setNextState(StateManager.States.NAVIGATE, e);
		}
	}
	
	public void mousePressed(MouseEvent e) {

	}
	
	public void mouseReleased(MouseEvent e) {
		stateManager.setNextState(StateManager.States.POINT, e);
		getView().getLasso().setCoordinates(false);
		getView().repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		
		Point2D mousePos = e.getPoint();
		getView().transformToUserSpace(mousePos);

		
		if(x<=e.getX() && y<=e.getY())
		{
			rec2=new Rectangle(x1,y1 ,(int)mousePos.getX()-x1,(int)mousePos.getY()-y1 );
			getView().getLasso().setCoordinates(x1,y1 ,(int)mousePos.getX()-x1,(int)mousePos.getY()-y1, true);
			getView().repaint();

		}
		if(x>e.getX() && y<=e.getY())
		{		
			rec2=new Rectangle((int)mousePos.getX(),y1 ,x1-(int)mousePos.getX(),(int)mousePos.getY()-y1 );
			getView().getLasso().setCoordinates((int)mousePos.getX(),y1 ,x1-(int)mousePos.getX(),(int)mousePos.getY()-y1, true );
			getView().repaint();

		}
		if(x>e.getX() && y>e.getY())
		{
			rec2=new Rectangle((int)mousePos.getX(),(int)mousePos.getY() ,x1-(int)mousePos.getX(),y1-(int)mousePos.getY() );
			getView().getLasso().setCoordinates((int)mousePos.getX(),(int)mousePos.getY() ,x1-(int)mousePos.getX(),y1-(int)mousePos.getY(), true );
			getView().repaint();

		}
		if(x<=e.getX() && y>e.getY())
		{
			rec2=new Rectangle(x1,(int)mousePos.getY() ,(int)mousePos.getX()-x1,y1-(int)mousePos.getY() );
			getView().getLasso().setCoordinates(x1,(int)mousePos.getY() ,(int)mousePos.getX()-x1,y1-(int)mousePos.getY(), true );
			getView().repaint();

		}
		
		
		try
		{
			for(int i=0;i<getModel().getVisNodes().size();i++)
			{
				Point2D poza=new Point();
				poza=getModel().getNodeAt(i).getPosition();
				int xx=getModel().getNodeAt(i).getSize().width/2;
				int yy=getModel().getNodeAt(i).getSize().height/2;
				poza.setLocation(poza.getX()+xx,poza.getY()+yy);

				if(rec2.contains(poza))
				{
					if(!getModel().getSelectedNodes().contains(getModel().getNodeAt(i)))
					{
						getModel().selectNode(getModel().getNodeAt(i));
					}
				}
				else
				{
					if(getModel().getSelectedNodes().contains(getModel().getNodeAt(i)))
					{
						getModel().deselectNode(getModel().getNodeAt(i));
					}
				}
				
				poza.setLocation(poza.getX()-xx,poza.getY()-yy);
			}
			
		}catch(Exception ex)
		{

		}
		

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

}
