package tk.exgerm.visualiser.model.nodes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;

import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.view.painters.VisNodeDefaultPainter;

public class VisNodeDefault extends VisNode {
	
	public VisNodeDefault(Point2D position, Dimension size, Stroke stroke, Paint paint) {
		super(position, size, stroke, paint);
		visElementPainter = new VisNodeDefaultPainter(this);
	}
	
	public static VisNode createDefault(Point2D pos, int elemNo) {
		Point2D position = (Point2D) pos.clone();
		position.setLocation(position.getX() - 50, position.getY() - 50);
		Paint fill = new GradientPaint(0, 0, new Color(214, 217, 224), 100, 0, new Color(70, 70, 255));
		
		
		VisNodeDefault vnd = new VisNodeDefault(position, new Dimension(100, 100),
						new BasicStroke(3f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND), fill);
		vnd.setName("Default Node " + new Integer(elemNo));
		vnd.setPaint(fill);
		vnd.setNodeDefaultPaint(fill);
		return vnd;
	}


}
