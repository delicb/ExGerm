package tk.exgerm.visualiser.model;

import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;

import tk.exgerm.visualiser.view.painters.VisEdgePainter;

public class VisEdge extends AbsVisElement {
	
	protected VisNode source;
	protected VisNode destination;
	protected boolean directed = false;
	protected int id;
	
	protected int div;
	
	public VisEdge(Stroke stroke, Paint paint, VisNode _source, VisNode _destination){
		super(stroke, paint);
		this.source = _source;
		this.destination = _destination;
		visElementPainter = new VisEdgePainter(this);
		div = 1;
	}
	
	public double Length(){
		Point p1 = (Point) source.getPosition();
		Point p2 = (Point) destination.getPosition();
		
		double x1 = p1.x; 
		double y1 = p1.y; 

		double x2 = p2.x; 
		double y2 = p2.y;

		
		double x = x1-x2;
		double y = y1-y2;
		
		return Math.sqrt(x*x + y*y); 
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDirected() {
		return directed;
	}

	public int getDiv() {
		return div;
	}

	public void setDiv(int div) {
		this.div = div;
	}
	
	public void raiseDiv(){
		div++;
	}

	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	public VisNode getDestination() {
		return destination;
	}
	
	public void setDestination(VisNode destination) {
		this.destination = destination;
	}
	
	public VisNode getSource() {
		return source;
	}
	
	public void setSource(VisNode source) {
		this.source = source;
	}
}
