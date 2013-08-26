package tk.exgerm.visualiser.view;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import tk.exgerm.visualiser.model.AbsVisElement;

public abstract class VisElementPainter {
	protected AbsVisElement element;

	public VisElementPainter(AbsVisElement element) {
		this.element = element; 
	}

	public abstract void paint(Graphics2D g);
	
	public abstract boolean isElementAt(Point2D pos);

}
