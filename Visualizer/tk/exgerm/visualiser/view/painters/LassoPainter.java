package tk.exgerm.visualiser.view.painters;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.SystemColor;

import tk.exgerm.visualiser.view.VisualiserView;

public class LassoPainter {
	
	private VisualiserView view;
	private int a;
	private int b;
	private int c;
	private int d;
	private boolean draw = false;
	
	public LassoPainter(VisualiserView _view){
		this.view = _view;
		this.a = 0;
		this.b = 0;
		this.c = 0;
		this.d = 0;
	}
	
	public void paint(Graphics2D g) {
		if(!draw) return;
		else{
			g.setStroke(new BasicStroke((float) view.sizeToUserSpace(1.2), BasicStroke.CAP_SQUARE,
					BasicStroke.JOIN_BEVEL, 1f, new float[] { 10f, 20f }, 0));
			g.setPaint(SystemColor.DARK_GRAY);
			g.drawRect(a, b, c, d);
		}

	}
	
	public void setCoordinates(int a, int b, int c, int d, boolean draw){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.draw = draw;
	}
	
	public void setCoordinates(boolean draw){
		this.a = 0;
		this.b = 0;
		this.c = 0;
		this.d = 0;
		this.draw = draw;
	}

}
