package tk.exgerm.visualiser.view.painters;

import java.awt.geom.Arc2D;
import java.awt.geom.Area;

import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.model.nodes.VisNodeDefault;

public class VisNodeDefaultPainter extends VisNodePainter {

	public VisNodeDefaultPainter(VisNode node) {
		super(node);
		VisNodeDefault vnd = (VisNodeDefault) node;
		
		double w = vnd.getSize().width;
		double h = vnd.getSize().height;
		
		Arc2D arc = new Arc2D.Double(0, 0, w, h, 0.0, 360, Arc2D.CHORD);
		
		Area arcArea = new Area(arc);
		
		shape = arcArea;
		
		Area arcAreaX = new Area();

		shape2 = arcAreaX;
		Area arcAreaXX = new Area();
		
		shape3 = arcAreaXX;

	}

}
