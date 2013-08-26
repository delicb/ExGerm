package tk.exgerm.visualiser.view.painters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import tk.exgerm.visualiser.model.VisEdge;
import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.view.VisElementPainter;

public class VisEdgePainter extends VisElementPainter {
	
	protected Shape line;
	protected Shape curve;

	public VisEdgePainter(VisEdge element) {
		super(element);
	}

	@Override
	public boolean isElementAt(Point2D pos) {
		if(curve != null)
			return ( curve.contains(pos.getX(), pos.getY()) );
		return false;
	}
	
	private void makeCurve(VisNode node){
		GeneralPath gp = new GeneralPath();
		gp.moveTo((int) node.getPosition().getX()
				+ (int) (node.getSize().width / 2), 
				(int)node.getPosition().getY()
				+ (int) (node).getSize().height / 2);
		gp.curveTo((int) node.getPosition().getX()
				+ (int) (node.getSize().width)/2 + 200,
				(int)node.getPosition().getY()
				+ (int) (node).getSize().height/2,
				(int) node.getPosition().getX()
				+ (int) (node.getSize().width)/2,
				(int)node.getPosition().getY()
				+ (int) (node).getSize().height/2 - 250,
				(int) node.getPosition().getX() + (int) (node.getSize().width)/2, 
				(int)node.getPosition().getY()+ (int) (node).getSize().height/2) ;
		
		gp.closePath();
		curve = gp;
	}
	
	private void makeLine(VisEdge e){
		Line2D linee = new Line2D.Double((int) e.getSource().getPosition().getX()
				+ (int) (e.getSource().getSize().width / 2), (int) e
				.getSource().getPosition().getY()
				+ (int) (e.getSource().getSize().height / 2), (int) e
				.getDestination().getPosition().getX()
				+ (int) (e.getDestination().getSize().width / 2), (int) e
				.getDestination().getPosition().getY()
				+ (int) (e.getDestination().getSize().height / 2));
		
		line = linee;
	}

	@Override
	public void paint(Graphics2D g) {
		VisEdge e = (VisEdge) element;

		g.setColor((Color) e.getPaint());
		g.setStroke(e.getStroke());
		
		if(e.getSource() == e.getDestination()){
			
			VisNode node = e.getSource();
			makeCurve(node);
			g.draw(curve);
			
			if (((VisEdge) this.element).isDirected()) {
			
			GeneralPath tri = new GeneralPath();
			tri.moveTo(0, 0);
			tri.lineTo(50, 0);
			tri.lineTo(70,-10);
			tri.lineTo(70, 10);
			tri.lineTo(50,  0);
			tri.closePath();
			
			AffineTransform transform = new AffineTransform();
			transform.rotate(-Math.PI/2 + Math.PI/30);
			tri.transform(transform);
			
			g.translate((int) node.getPosition().getX()
					+ (int) (node.getSize().width / 2), 
					(int)node.getPosition().getY()
					+ (int) (node).getSize().height / 2);
			
			g.draw(tri);
			
			g.translate(-((int) node.getPosition().getX()
					+ (int) (node.getSize().width / 2)), 
					-((int)node.getPosition().getY()
					+ (int) (node).getSize().height / 2));
			
			}
		}
		else{
			makeLine(e);
			g.draw(line);
			
			if (((VisEdge) this.element).isDirected()) {
				
				Point p2 = (Point) e.getDestination().getPosition();
				Point p1 = (Point) e.getSource().getPosition();
				
				double x1 = p1.x; 
				double y1 = p1.y; 
	
				double x2 = p2.x; 
				double y2 = p2.y;
	
				double x = Math.abs(x1-x2);
				double y = Math.abs(y1-y2);
				
				double alpha = Math.atan(x/y);
	
				GeneralPath tri = new GeneralPath();
				tri.moveTo(0, 0);
				tri.lineTo(50, 0);
				tri.lineTo(70,-10);
				tri.lineTo(70, 10);
				tri.lineTo(50,  0);
				tri.closePath();		
				
				AffineTransform transform = new AffineTransform();
				double angle = alpha;
				if( (e.getDestination().getPosition().getX() < e.getSource().getPosition().getX()) && (e.getDestination().getPosition().getY() < e.getSource().getPosition().getY())){
					angle = -alpha + Math.PI/2;
				}
				if( (e.getDestination().getPosition().getX() > e.getSource().getPosition().getX()) && (e.getDestination().getPosition().getY() > e.getSource().getPosition().getY())){
					angle = -alpha - Math.PI/2;
				}
				if( (e.getDestination().getPosition().getX() < e.getSource().getPosition().getX()) && (e.getDestination().getPosition().getY() > e.getSource().getPosition().getY())){
					angle = alpha - Math.PI/2;
				}
				if( (e.getDestination().getPosition().getX() > e.getSource().getPosition().getX()) && (e.getDestination().getPosition().getY() < e.getSource().getPosition().getY())){
					angle = alpha + Math.PI/2;
				}
				if( e.getDestination().getPosition().getX() == e.getSource().getPosition().getX() ){
					if(e.getDestination().getPosition().getY() < e.getSource().getPosition().getY()){			
						angle = Math.PI/2;
					}
					if(e.getDestination().getPosition().getY() > e.getSource().getPosition().getY()){			
						
						angle = - Math.PI/2;
					}
				}
				
				if( e.getDestination().getPosition().getY() == e.getSource().getPosition().getY() ){
					if(e.getDestination().getPosition().getX() < e.getSource().getPosition().getX()){			
						angle = 0;
					}
					if(e.getDestination().getPosition().getX() > e.getSource().getPosition().getX()){			
						
						angle =- Math.PI;
					}
				}
				
				transform.setToRotation(angle);
				
				tri.transform(transform);
				
				g.translate((int) e.getDestination().getPosition().getX()
						+ (int) (e.getDestination().getSize().width / 2), (int) e
						.getDestination().getPosition().getY()
						+ (int) (e.getDestination().getSize().height / 2));
					
	
				g.draw(tri);
				
				g.translate(-((int) e.getDestination().getPosition().getX()
						+ (int) (e.getDestination().getSize().width / 2)), -((int) e
						.getDestination().getPosition().getY()
						+ (int) (e.getDestination().getSize().height / 2)));
			}
		}
	}
}
