package tk.exgerm.visualiser.view.painters;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.view.VisElementPainter;

public class VisNodePainter extends VisElementPainter {

	protected Shape shape;
	protected Shape shape2;
	protected Shape shape3;
    Rectangle2D rec = new Rectangle();

	
	public VisNodePainter(VisNode element) {
		super(element);
	}
	
	public void paint(Graphics2D g) {
		VisNode node = (VisNode) element;
		g.translate(node.getPosition().getX(), node.getPosition().getY());

		g.setPaint(Color.BLACK);
		g.setStroke(node.getStroke());
		g.draw(getShape2());
		g.setPaint(new Color(255,255,255,0));
		g.fill(getShape2());
		
		g.setPaint(Color.BLACK);
		g.setStroke(node.getStroke());
		g.draw(getShape());
		g.setPaint(node.getPaint());
		g.fill(getShape());
		
		g.setPaint(Color.BLACK);
		g.setStroke(node.getStroke());
		g.draw(getShape3());
		g.setPaint(new Color(255,200,100,255));
		g.fill(getShape3());
		
		g.setPaint(Color.DARK_GRAY);
	    Font font = new Font("Calibri", Font.PLAIN, 25);
	    TextLayout textLayout = new TextLayout(node.getName(), font, g.getFontRenderContext());
	    
	    rec = textLayout.getBounds();
	    
	    if(rec.getWidth()/2 > node.getSize().width/2){
		    textLayout.draw(g, (float) -(rec.getWidth()/2 - node.getSize().width/2), (float)(node.getSize().height + rec.getHeight() + rec.getHeight()/5));
	    }else{
		    textLayout.draw(g, (float) (node.getSize().width/2 - rec.getWidth()/2), (float)(node.getSize().height + rec.getHeight() + rec.getHeight()/5));
	    }

		
		g.translate(-node.getPosition().getX(), -node.getPosition().getY());
	}
	
	public boolean isElementAt(Point2D pos){
		VisNode node = (VisNode) element;
		return ( (getShape().contains(pos.getX() - node.getPosition().getX(), pos.getY() - node.getPosition().getY())) ||
		         (getShape2().contains(pos.getX() - node.getPosition().getX(), pos.getY() - node.getPosition().getY())) || 
		         (getShape3().contains(pos.getX() - node.getPosition().getX(), pos.getY() - node.getPosition().getY())) );
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public Shape getShape2() {
		return shape2;
	}

	public void setShape2(Shape shape2) {
		this.shape2 = shape2;
	}
	public Shape getShape3() {
		return shape3;
	}

	public void setShape3(Shape shape2) {
		this.shape3 = shape2;
	}

}
