package tk.exgerm.visualiser.view;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ResizeListener implements ComponentListener {

	VisualiserView view;
	
	public ResizeListener(VisualiserView view){
		this.view = view;
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) {
		Rectangle r = view.getBounds();
		view.getNavigator().setPosition(new Point(r.width, 0));
	}

	@Override
	public void componentShown(ComponentEvent e) {
		view.centerOn();
	}

}
