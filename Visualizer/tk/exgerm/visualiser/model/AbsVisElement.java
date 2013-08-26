package tk.exgerm.visualiser.model;

import java.awt.Paint;
import java.awt.Stroke;

import tk.exgerm.visualiser.view.VisElementPainter;

public abstract class AbsVisElement {

	protected String name;
	protected Paint paint;
	protected Stroke stroke;
	protected VisElementPainter visElementPainter;
	
	public AbsVisElement(Stroke stroke, Paint paint){
		this.stroke = stroke;
		this.paint = paint;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public VisElementPainter getPainter() {
		return visElementPainter;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	@Override
	public String toString() {
		return name;
	}


}
