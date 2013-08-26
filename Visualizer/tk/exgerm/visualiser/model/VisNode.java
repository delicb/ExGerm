package tk.exgerm.visualiser.model;

import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;

public class VisNode extends AbsVisElement {

	protected Dimension size;
	protected Point2D position;
	protected Point2D positionNew;
	protected Paint nodeDefaultPaint;
	protected double mass = 2100000;
	
	protected boolean isSubGraph;
	protected int level;
	protected String finalRoot;
	
	public VisNode(Point2D position, Dimension size, Stroke stroke, Paint paint){
		super(stroke, paint);
		this.size = size;
		this.position = position;
		this.positionNew = position;
	}
	

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Point2D getPositionNew() {
		return positionNew;
	}

	public void setPositionNew(Point2D positionOld) {
		this.positionNew = positionOld;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}
	
	public Paint getNodeDefaultPaint() {
		return nodeDefaultPaint;
	}

	public void setNodeDefaultPaint(Paint nodeDefaultPaint) {
		this.nodeDefaultPaint = nodeDefaultPaint;
	}


	public boolean isSubGraph() {
		return isSubGraph;
	}


	public int getLevel() {
		return level;
	}


	public String getFinalRoot() {
		return finalRoot;
	}


	public void setSubGraph(boolean isSubGraph) {
		this.isSubGraph = isSubGraph;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public void setFinalRoot(String finalRoot) {
		this.finalRoot = finalRoot;
	}

}
