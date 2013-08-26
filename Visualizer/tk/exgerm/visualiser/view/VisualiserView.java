package tk.exgerm.visualiser.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.visualiser.listeners.DirtyFlagChangedListener;
import tk.exgerm.visualiser.model.VisEdge;
import tk.exgerm.visualiser.model.VisNode;
import tk.exgerm.visualiser.model.VisualiserModel;
import tk.exgerm.visualiser.navigator.Navigator;
import tk.exgerm.visualiser.state.StateManager;
import tk.exgerm.visualiser.view.painters.LassoPainter;
import tk.exgerm.visualiser.view.painters.LinkerPainter;

@SuppressWarnings("serial")
public class VisualiserView extends JPanel {

	private int level;
	private String finalRoot;
	private VisNode mouseOverNode;
	private boolean drawSearchResults;

	private double translationFactor = 40;
	final static double scalingFactor = 1.2;
	AffineTransform transformation = new AffineTransform();
	private StateManager stateManager;

	private VisualiserModel model;
	private LassoPainter lasso;
	private LinkerPainter linker;
	private Navigator navigator;
	
	private boolean visual = false;

	public VisualiserView(VisualiserModel _model) {
		this.model = _model;
		this.lasso = new LassoPainter(this);
		this.linker = new LinkerPainter(this);
		stateManager = new StateManager(this);
		setBackground(SystemColor.WHITE);
		scale(-3);

		finalRoot = null;
		level = 0;
		drawSearchResults = false;

		getModel().getContext().listenEvent(IGraph.GRAPH_DIRTY_FLAG_CHANGED,
				new DirtyFlagChangedListener(this));

		this.navigator = new Navigator(this);
		addComponentListener(new ResizeListener(this));
		
		KeyboardFocusManager manager = KeyboardFocusManager
		.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_F1) {
					VisualiserView.this.model.showHelp("Visualiser");
				}
				return false;
			}
			
		});
	}

	public Timer visualization = new Timer(40, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for (int count = 0; count < 4; count++)
				getModel().inLoop();
			repaint();
		}
	});

	public boolean isVisual() {
		return visual;
	}

	public void setVisual(boolean visual) {
		this.visual = visual;
	}

	public boolean isDrawSearchResults() {
		return drawSearchResults;
	}

	public void setDrawSearchResults(boolean drawSearchResults) {
		this.drawSearchResults = drawSearchResults;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.transform(transformation);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		drawGrid(g2);

		if (drawSearchResults) {
			for (VisNode n : getModel().getSearchNodes()) {
				drawNodeRectangles(g2, n, 4, new Color(255, 80, 0), 0.5f);
			}
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
			for (VisEdge e : getModel().getSearchEdges()) {
				drawEdgeRectangles(g2, e, new Color(255, 80, 0));
			}
		}

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));

		for (VisEdge e : getModel().getVisEdges()) {
			e.getPainter().paint(g2);
		}

		for (VisNode n : getModel().getVisNodes()) {
			n.getPainter().paint(g2);
		}

		lasso.paint(g2);
		linker.paint(g2);

		for (VisNode n : getModel().getSelectedNodes()) {
			drawNodeRectangles(g2, n, 4, new Color(0, 0, 255), 0.1f);
		}

		drawMouseOverNodeIndicator(g2);
	}

	private void drawMouseOverNodeIndicator(Graphics2D g2) {
		VisNode n = getMouseOverNode();
		if (n != null) {
			g2.setStroke(new BasicStroke());
			g2.setPaint(new Color(0, 0, 250));
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.1f));
			RoundRectangle2D r = getNodeRoundRectangle(n);
			g2.fill(r);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
			g2.draw(r);
		}
	}

	private RoundRectangle2D getNodeRoundRectangle(VisNode n) {
		double gap = 6;// transform.getScaleX();
		RoundRectangle2D r = new RoundRectangle2D.Double(n.getPosition().getX()
				- gap, n.getPosition().getY() - gap, n.getSize().getWidth() + 2
				* gap, n.getSize().getHeight() + 2 * gap, 15, 15);
		return r;
	}

	private void drawNodeRectangles(Graphics2D g2, VisNode n, double gap,
			Color col, float alpha) {
		// transparencija
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		// linija
		g2.setStroke(new BasicStroke());
		// velicina selekcionih kvadrata
		gap = 4 / transformation.getScaleX();
		// boja
		g2.setPaint(col);

		// pravougaonici - jedan veliki za okvir i 4 mala u uglovima
		RoundRectangle2D rr = new RoundRectangle2D.Double(n.getPosition()
				.getX()
				- gap, n.getPosition().getY() - gap, n.getSize().getWidth()
				+ gap * 2, n.getSize().getHeight() + gap * 2, 15, 15);
		g2.fill(rr);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2.draw(rr);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
	}

	private void drawEdgeRectangles(Graphics2D g, VisEdge e, Color col) {
		g.setColor(col);
		g.setStroke(new BasicStroke(35.0f));

		g.drawLine((int) e.getSource().getPosition().getX()
				+ (int) (e.getSource().getSize().width / 2), (int) e
				.getSource().getPosition().getY()
				+ (int) (e.getSource().getSize().height / 2), (int) e
				.getDestination().getPosition().getX()
				+ (int) (e.getDestination().getSize().width / 2), (int) e
				.getDestination().getPosition().getY()
				+ (int) (e.getDestination().getSize().height / 2));

	}

	public VisNode getMouseOverNode() {
		return this.mouseOverNode;
	}

	public void setMouseOverNode(VisNode n) {
		this.mouseOverNode = n;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		navigator.paint(g);
	}

	public int getLevel() {
		return level;
	}

	public String getFinalRoot() {
		return finalRoot;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setFinalRoot(String finalRoot) {
		this.finalRoot = finalRoot;
	}

	public double sizeToUserSpace(double size) {
		return size / transformation.getScaleX();
	}

	public void centerOn(VisNode node) {
		if (node != null)
			centerOn(new Point((int) (node.getPosition().getX() + node
					.getSize().getWidth() / 2), ((int) (node.getPosition()
					.getY() + node.getSize().getHeight() * 2 / 3))));
	}

	public void centerOn(Point newCenter) {
		Point oldCenter = new Point(getWidth() / 2, getHeight() / 2);
		transformToUserSpace(oldCenter);
		translateTo(oldCenter, newCenter);
		repaint();
	}

	public void centerOn() {
		Point oldCenter = new Point(getWidth() / 2, getHeight() / 2);
		Point newCenter = new Point(0, 0);
		transformToUserSpace(oldCenter);
		translateTo(oldCenter, newCenter);
		repaint();
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public LassoPainter getLasso() {
		return lasso;
	}

	public LinkerPainter getLinker() {
		return linker;
	}

	public VisualiserModel getModel() {
		return model;
	}
	
	public void transformToUserSpace(Point2D deviceSpace) {
		try {
			transformation.inverseTransform(deviceSpace, deviceSpace);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}

	public void translateTo(Point2D to, Point2D from) {
		transformation.translate(to.getX() - from.getX(), to.getY()
				- from.getY());
	}

	public void translateHorizontally(double value) {
		transformation.translate(-value * translationFactor
				/ transformation.getScaleX(), 0);
	}

	public void translateVertically(double value) {
		transformation.translate(0, -value * translationFactor
				/ transformation.getScaleY());
	}

	public void scale(int steps) {

		double newScaling = transformation.getScaleX();

		double scalingAdjustment = Math.abs(steps * scalingFactor);

		if (steps > 0)
			newScaling *= scalingAdjustment;
		else
			newScaling /= scalingAdjustment;

		scaleTo(newScaling);
	}

	public void scaleTo(double newScaling) {
		if (newScaling < 0.10)
			newScaling = 0.10;
		if (newScaling > 6)
			newScaling = 6;

		transformation.setToScale(newScaling, newScaling);
	}

	public void zoom(Point zoomCenter, int zoomDirection) {
		if (Math.abs(zoomDirection) == 1) {
			Point2D oldPosition = new Point(zoomCenter);
			transformToUserSpace(oldPosition);

			scale(zoomDirection);

			Point2D newPosition = new Point(zoomCenter);
			transformToUserSpace(newPosition);
			translateTo(newPosition, oldPosition);
		}
	}

	public void zoomCenter(int zoomDirection) {
		if (Math.abs(zoomDirection) == 1) {
			Point viewCenter = new Point(getWidth() / 2, getHeight() / 2);
			zoom(viewCenter, zoomDirection);
		}
	}

	private void drawGrid(Graphics2D g2) {
		GeneralPath lines = new GeneralPath();
		GeneralPath detailLines = new GeneralPath();

		g2.setStroke(new BasicStroke(0));

		Point2D startPoint = new Point2D.Double(0, 0);
		Point2D endPoint = new Point2D.Double(getWidth(), getHeight());

		try {
			transformation.inverseTransform(startPoint, startPoint);
			transformation.inverseTransform(endPoint, endPoint);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}

		if (transformation.getScaleX() > 0.25) {
			g2.setPaint(new Color(130, 130, 130));
			g2.setStroke(new BasicStroke(0));
			for (int start = (int) startPoint.getX(); start < (int) endPoint
					.getX(); start++)
				if (start % 75 == 0) {
					detailLines.moveTo(start, startPoint.getY());
					detailLines
							.lineTo(start, startPoint.getY()
									+ (double) getHeight()
									/ transformation.getScaleY());
				}

			for (int start = (int) startPoint.getY(); start < (int) endPoint
					.getY(); start++)
				if (start % 75 == 0) {
					detailLines.moveTo(startPoint.getX(), start);
					detailLines.lineTo(startPoint.getX() + (double) getWidth()
							/ transformation.getScaleX(), start);
				}

			g2.draw(detailLines);
			g2.setStroke(new BasicStroke(.5f));
		}

		int distance = 300;

		if (transformation.getScaleX() < 0)
			distance = 600;

		if (transformation.getScaleX() <= 0.10000000149011612D)
			distance = 1200;

		g2.setPaint(new Color(0, 0, 0));

		for (int start = (int) startPoint.getX(); start < (int) endPoint.getX(); start++)
			if (start % distance == 0) {
				lines.moveTo(start, startPoint.getY());
				lines.lineTo(start, startPoint.getY() + (double) getHeight()
						/ transformation.getScaleY());
			}

		for (int start = (int) startPoint.getY(); start < (int) endPoint.getY(); start++)
			if (start % distance == 0) {
				lines.moveTo(startPoint.getX(), start);
				lines.lineTo(startPoint.getX() + (double) getWidth()
						/ transformation.getScaleX(), start);
			}

		g2.draw(lines);
	}

	// Anim

	private Point2D relocate = null;
	private boolean fastZoomAndScale = false;
	private double newScale = -1;

	public Timer animation = new Timer(40, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			run();
		}
	});

	public void setRelocate(Point2D.Double relocate) {
		this.relocate = relocate;
	}

	public void centerNode(VisNode node) {
		setRelocate(new Point2D.Double(node.getSize().getWidth() / 2
				+ node.getPosition().getX(), node.getSize().getHeight() / 2
				+ node.getPosition().getY()));
	}

	public void run() {
		Point screenCenter = getCanvasCenter();
		Point2D.Double pos = new Point2D.Double();
		relocateCalculator((Point2D) screenCenter, relocate, pos);
		moveWorkspace(pos.x, pos.y);
		if (pos.x == 0 && pos.y == 0) {
			if (newScale == -1)
				fastZoomAndScale = false;
			relocate = null;
		}
	}

	public void moveWorkspace(double x, double y) {
		transformation.translate(x, y);
		repaint();
	}

	/**
	 * Skalira dijagram tako da se svi elementi budu vidljivi
	 */
	public void setBestFit() {
		
		if(getModel().getVisNodes().isEmpty())
			return;
		
		VisNode firstNode = getModel().getVisNodes().get(0);
			double maxX = firstNode.getPosition().getX() + firstNode.getSize().width;
			double minX = firstNode.getPosition().getX();
			
			double maxY = firstNode.getPosition().getY() + firstNode.getSize().height;
			double minY = firstNode.getPosition().getY();
			
			Iterator<VisNode> it = getModel().getVisNodes().iterator();
			while (it.hasNext()) {
				VisNode node = it.next();
				Point2D p = node.getPosition();
				if(p.getX() > maxX)
					maxX = p.getX() + node.getSize().width;
				
				if(p.getX() < minX)
					minX = p.getX();
				
				if(p.getY() > maxY)
					maxY = p.getY() + + node.getSize().height;
				
				if(p.getY() < minY)
					minY = p.getY();
			}

			Rectangle2D rect = new Rectangle();
			rect.setFrameFromDiagonal(minX-50, minY-50, maxX+50, maxY+50);
			
			Point2D rectCenter = new Point();
			Point2D windowCenter = new Point();
			double scaling;
			double rectWidth = rect.getWidth();
			double rectHeight = rect.getHeight();
			double windowWidth = getWidth();
			double windowHeight = getHeight();
			
			rectCenter.setLocation(rect.getCenterX(), rect.getCenterY());
			windowCenter.setLocation(getWidth() / 2, getHeight() / 2);
			
			if (rectWidth > rectHeight) {
				scaling = windowWidth / rectWidth;
				if (rectHeight * scaling > windowHeight) {
					scaling = windowHeight / rectHeight;
				}
			} else {
				scaling = windowHeight / rectHeight;
				if (rectWidth * scaling > windowWidth) {
					scaling = windowWidth / rectWidth;
				}
			}
			
			Point2D oldPosition = rectCenter;
			
			scaleTo(scaling);
			
			Point2D newPosition = windowCenter;
			transformToUserSpace(newPosition);
			
			translateTo(newPosition, oldPosition);
	}
	
	public Point getCanvasCenter() {
		Point screenCenter = new Point(getWidth() / 2, getHeight() / 2);
		try {
			return (Point) transformation.inverseTransform(screenCenter,
					screenCenter);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void relocateCalculator(Point2D currentPos, Point2D destination,
			Point2D diffPos) {
		double xTranslate = 0;
		double yTranslate = 0;

		double scaleFactor = 1 / Math.sqrt(Math.sqrt(Math.sqrt(transformation
				.getScaleX())));
		double precision = 5 * scaleFactor;

		double stepX;
		double stepY;

		try {
			stepX = precision
					* Math.abs(currentPos.getX() - destination.getX()) / 50;
			stepY = precision
					* Math.abs(currentPos.getY() - destination.getY()) / 50;
		} catch (Exception e) {
			animation.stop();
			return;
		}

		if (fastZoomAndScale) {
			stepX *= 2;
			stepY *= 2;
		}

		if (stepX < precision)
			stepX = precision;
		if (stepX > precision * 30 && !fastZoomAndScale)
			stepX = precision * 30;

		if (stepY < precision)
			stepY = precision;
		if (stepY > precision * 30 && !fastZoomAndScale)
			stepY = precision * 30;

		xTranslate *= scaleFactor;
		yTranslate *= scaleFactor;

		if (Math.abs(currentPos.getX() - destination.getX()) < stepX)
			xTranslate = 0;
		else if (currentPos.getX() < destination.getX())
			xTranslate = -stepX;
		else if (currentPos.getX() > destination.getX())
			xTranslate = stepX;

		if (Math.abs(currentPos.getY() - destination.getY()) < stepY)
			yTranslate = 0;
		else if (currentPos.getY() < destination.getY())
			yTranslate = -stepY;
		else if (currentPos.getY() > destination.getY())
			yTranslate = stepY;

		diffPos.setLocation(xTranslate, yTranslate);
	}

}
