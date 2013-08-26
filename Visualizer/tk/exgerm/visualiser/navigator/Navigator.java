package tk.exgerm.visualiser.navigator;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

import javax.swing.Timer;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.visualiser.view.VisualiserView;

/**
 * Predstavlja navigacionu kontrolu Visualiser komponente. Implementira kontrole
 * pomeranja dijagrama, zoomiranja kao i BestFit idealnog prikaza. Podržava tri
 * stanja prikaza:
 * <ul>
 * <li>Automatski - ako kursor misa prelazi preko navigatora bi'e vidljiv, ako
 * ne iscrtace se samo kontura kako bi korisniku ukazao svoje postojanje</li>
 * <li>Always - navigator će uvek biti vidljiv</li>
 * <li>Never - navigator će biti skriven</li>
 * </ul>
 * 
 * @author Tim 2
 * 
 */
public class Navigator {

	/**
	 * Konstante koje reprezentuju način prikazivanja navigatora
	 */
	public static final int SHOW_AUTOMATICALLY = 0;
	public static final int SHOW_ALWAYS = 1;
	public static final int SHOW_NEVER = 2;

	/**
	 * Konstante koje reprezentuju stanja u kojima se navigator može naći
	 */
	private static final int MOVE_BACKGROUND = 0;
	private static final int MOVE_NORM = 1;
	private static final int MOVE_SPOTLIGHT_ACTIVE = 2;
	private static final int MOVE_SPOTLIGHT_HOVER = 3;
	private static final int ZOOM_BACKGROUND = 4;
	private static final int ZOOM_MINUS_ACTIVE = 5;
	private static final int ZOOM_MINUS_HOVER = 6;
	private static final int ZOOM_NORM = 7;
	private static final int ZOOM_PLUS_ACTIVE = 8;
	private static final int ZOOM_PLUS_HOVER = 9;
	private static final int ZOOM_BAR_ACTIVE = 10;
	private static final int ZOOM_BAR_HOVER = 11;
	private static final int ZOOM_BAR_NORM = 12;
	private static final int ZOOM_TARGET = 13;

	/**
	 * Niz koji sadrži nazive fajlova sličica koje se prikazuju u navigatoru
	 */
	private static String imgFiles[] = { "move_background.png",
			"move_norm.png", "move_spotlight_active.png",
			"move_spotlight_hover.png", "zoom_background.png",
			"zoom_minus_active.png", "zoom_minus_hover.png", "zoom_norm.png",
			"zoom_plus_active.png", "zoom_plus_hover.png",
			"zoom_bar_active.png", "zoom_bar_hover.png", "zoom_bar_norm.png",
			"zoom_target.png" };

	/**
	 * Viewer u kome se navigator prikazuje
	 */
	private VisualiserView view;
	private static MediaTracker tracker;

	/**
	 * Mapa koja sadrži sličice koje se prikazuju u navigatoru
	 */
	private static HashMap<Integer, Image> images = new HashMap<Integer, Image>();

	/**
	 * Pozicija na kojoj se iscrtava navigator
	 */
	private Point position;
	/**
	 * Rastojanje oko navigatora
	 */
	private static int gap = 10;
	/**
	 * Rastojanje izmedju gornje ivice i navigatora
	 */
	private static int horizontalGap = 30;
	/**
	 * Rastojanje između movePanel-a i zoomPanel-a
	 */
	private static int zoomPanelGap = 20;

	/**
	 * Trenutni način prikaza
	 */
	private static int showState;
	/**
	 * Stanje u kome se nalazi move kontrole
	 */
	private int moveActionState;
	/**
	 * Stanje u kome se nalazi zoom kontrole
	 */
	private int zoomActionState;
	/**
	 * Stanje u kome se nalazi zoomBar kontrole
	 */
	private int zoomBarActionState;
	/**
	 * Da li je Navigator prikazan
	 */
	private boolean showed;
	/**
	 * Da li je dozvoljeno iscrtavanje navigatora
	 */
	private boolean enableRepaint = true;
	/**
	 * "Brzina" prikazivanja i skrivanja
	 */
	private static float fadeFactor = 0.05f;
	/**
	 * "Kolicina" transparencije
	 */
	private float compositeValue = 1f;
	/**
	 * Tajmer za podršku fade efekta prilikom prikazivanja/skrivanja
	 */
	private Timer fadeTimer;
	/**
	 * Da li je kursor miša iznad navigatora
	 */
	private boolean hover;

	/**
	 * Širina move kontrole
	 */
	private static int movePanelWidth;
	/**
	 * Visina move kontrole
	 */
	private static int movePanelHeight;
	/**
	 * Poyicija na kojoj se indikator move kontrole iscrtava
	 */
	private Point spotlightMousePosition;

	/**
	 * Širina zoom kontrole
	 */
	private static int zoomPanelWidth;
	/**
	 * Visina zoom kontrole
	 */
	private static int zoomPanelHeight;
	/**
	 * Širina zoomBar kontrole
	 */
	private static int zoomBarWidth;
	/**
	 * Visina zoomBar kontrole
	 */
	private static int zoomBarHeight;
	/**
	 * "Količina" pomeranja zoomBar-a
	 */
	private int zoomBarMovement = 0;

	/**
	 * Širina "mete" koja se iscrtava prilikom zoom operacije
	 */
	private static int zoomTargetWidth;
	/**
	 * Visina "mete" koja se iscrtava prilikom zoom operacije
	 */
	private static int zoomTargetHeight;

	/**
	 * Predstavlja indikator dirty-flag-a grafa.
	 */
	private static Image notSavedImg;
	/**
	 * Predstavlja indikator On stanja "fensi" vizuelizacije
	 */
	private static Image visONImg;
	/**
	 * Predstavlja indikator Off stanja "fensi" vizuelizacije
	 */
	private static Image visOFFImg;

	/**
	 * Konstruktor, inicijaliyuje viewer komponentu, kao i sličice potrebne za
	 * prikaz Navigaor-a
	 * 
	 * @param view
	 *            viewer komponenta
	 */
	public Navigator(VisualiserView view) {
		this.view = view;

		if (tracker == null) {
			tracker = new MediaTracker(view);
			Image image;
			for (int i = 0; i < imgFiles.length; i++) {

				image = Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("images/" + imgFiles[i]));
				images.put(i, image);
				tracker.addImage(image, i);

				notSavedImg = Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("images/notsaved.png"));
				visONImg = Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("images/on.png"));
				visOFFImg = Toolkit.getDefaultToolkit().getImage(
						getClass().getResource("images/off.png"));

				tracker.addImage(notSavedImg, 1);
			}

			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			movePanelWidth = getImage(MOVE_NORM).getWidth(view);
			movePanelHeight = getImage(MOVE_NORM).getHeight(view);
			zoomPanelWidth = getImage(ZOOM_NORM).getWidth(view);
			zoomPanelHeight = getImage(ZOOM_NORM).getHeight(view);
			zoomBarWidth = getImage(ZOOM_BAR_NORM).getWidth(view);
			zoomBarHeight = getImage(ZOOM_BAR_NORM).getHeight(view);
			zoomTargetWidth = getImage(ZOOM_TARGET).getHeight(view);
			zoomTargetHeight = getImage(ZOOM_TARGET).getHeight(view);
		}

		moveActionState = MOVE_NORM;
		zoomActionState = ZOOM_NORM;
		zoomBarActionState = ZOOM_BAR_NORM;

		fadeTimer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!showed)
					fadeIn();
				else
					fadeOut();
				paintImmediately();
			}
		});
	}

	/**
	 * Iscrtava elemente navigatora
	 * 
	 * @param g
	 *            grafički kontekst u kome se elementi iscrtavaju
	 */
	public void paint(Graphics g) {
		if (position == null) {
			return;
		}

		if (view.isVisual()) {
			g.drawImage(visONImg, view.getBounds().width - 64, 0, view);
		} else {
			g.drawImage(visOFFImg, view.getBounds().width - 64, 0, view);
		}

		Boolean dirty = (Boolean) getView().getModel().getGraph()
				.getFinalRoot().getData(IGraph.DIRTY_FLAG);

		if (dirty == true) {
			g.drawImage(notSavedImg, view.getBounds().width - 48, 0, view);
		}

		int showState = getShowState();
		if (showState == SHOW_NEVER && !showed) {
			return;
		}

		Point movePanelPosition = getMovePanelPosition();
		Point zoomPanelPosition = getZoomPanelPosition();
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(getImage(MOVE_BACKGROUND), movePanelPosition.x,
				movePanelPosition.y, view);
		g.drawImage(getImage(ZOOM_BACKGROUND), zoomPanelPosition.x,
				zoomPanelPosition.y, view);

		if ((showState == SHOW_NEVER && showed)
				|| (showState == SHOW_ALWAYS && !showed)
				|| (showState == SHOW_AUTOMATICALLY && (hover && !showed || !hover
						&& showed))) {
			fadeTimer.start();
		}

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				compositeValue));
		drawMovePanel(g);
		drawZoomPanel(g);
		if (moveActionState != MOVE_NORM)
			drawSpotlghit(g);

		if (isZoomBarActive() || isPlusActive() || isMinusActive()) {
			g.drawImage(getImage(ZOOM_TARGET), view.getWidth() / 2
					- zoomTargetWidth / 2, view.getHeight() / 2
					- zoomTargetHeight / 2, view);
		}
	}

	/**
	 * Iscrtava move kontrolu na prosledjeni grafički kontekst
	 * 
	 * @param g
	 *            grafički kontekst
	 */
	private void drawMovePanel(Graphics g) {
		Point movePanelPosition = getMovePanelPosition();
		g.drawImage(getImage(MOVE_NORM), movePanelPosition.x,
				movePanelPosition.y, view);
	}

	/**
	 * Iscrtava zoom kontrolu na prosledjeni grafički kontekst
	 * 
	 * @param g
	 *            grafički kontekst
	 */
	private void drawZoomPanel(Graphics g) {
		Point zoomPanelPosition = getZoomPanelPosition();
		Point zoomBarPosition = getZoomBarPosition();
		g.drawImage(getImage(zoomActionState), zoomPanelPosition.x,
				zoomPanelPosition.y, view);
		g.drawImage(getImage(zoomBarActionState), zoomBarPosition.x,
				zoomBarPosition.y, view);
	}

	/**
	 * Iscrtava indikator move kontrole na prosledjeni grafički kontekst
	 * 
	 * @param g
	 *            grafički kontekst
	 */
	private void drawSpotlghit(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Point p = getOrtogonal(spotlightMousePosition);
		if (p == null) {
			return;
		}

		Point movePanelPosition = getMovePanelPosition();
		Double angle = ((p.getX() < 0) ? Math.toRadians(180) : 0)
				+ Math.atan(p.getY() / p.getX());

		g2.rotate(-angle, movePanelPosition.x + movePanelWidth / 2,
				movePanelPosition.y + movePanelHeight / 2);

		if (moveActionState == MOVE_SPOTLIGHT_ACTIVE) {
			int x = Math.abs(p.x);
			int y = Math.abs(p.y);
			float composite;

			composite = (x >= y) ? x : y;
			composite = (composite + 24f) / 48f;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					composite));
		} else {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					1.0f));
		}

		g2.drawImage(getImage(moveActionState), movePanelPosition.x,
				movePanelPosition.y, view);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
	}

	/**
	 * Setuje stanje prikaza Navigatora
	 * 
	 * @param showState
	 *            stanje prikaza Navigatora
	 */
	public static void setShowState(int showState) {
		Navigator.showState = showState;
	}

	/**
	 * Vraca trenutno stanje prikaza
	 * 
	 * @return stanje prikaza
	 */
	private int getShowState() {
		return Navigator.showState;
	}

	/**
	 * Setuje poziciju na kojoj će Navigator biti iscrtan
	 * 
	 * @param position
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * Vraća pociciju na kojoj se navigator iscrtava
	 * 
	 * @return pociciju na kojoj se navigator iscrtava
	 */
	public Point getPosition() {
		if (position == null)
			return null;

		return new Point(this.position.x - movePanelWidth - 2 * getGap() - 1,
				this.position.y + getHorizontalGap());
	}

	/**
	 * Vraća pociciju na kojoj se move kontrola iscrtava
	 * 
	 * @return pociciju na kojoj se move kontrola iscrtava
	 */
	public Point getMovePanelPosition() {
		if (position == null)
			return null;

		return new Point(getPosition().x + getGap(), getPosition().y + getGap());
	}

	/**
	 * Vraća pociciju na kojoj se zoom kontrola iscrtava
	 * 
	 * @return pociciju na kojoj se zoom kontrola iscrtava
	 */
	public Point getZoomPanelPosition() {
		return new Point(getMovePanelPosition().x + movePanelWidth / 2
				- zoomPanelWidth / 2, getMovePanelPosition().y
				+ movePanelHeight + getZoomPanelGap());
	}

	/**
	 * Vraća pociciju na kojoj se zoomBar kontrola iscrtava
	 * 
	 * @return pociciju na kojoj se zoomBar kontrola iscrtava
	 */
	public Point getZoomBarPosition() {
		return new Point(getZoomPanelPosition().x, getZoomPanelPosition().y
				+ zoomBarMovement + zoomPanelHeight / 2 - zoomBarHeight / 2);
	}

	/**
	 * Vraća veličinu imaginarnog okvira oko navigatora
	 * 
	 * @return veličinu imaginarnog okvira oko navigatora
	 */
	public int getGap() {
		return Navigator.gap;
	}

	/**
	 * Vraća rastojanje od gornje ivice i navigatora
	 * 
	 * @return rastojanje od gornje ivice i navigatora
	 */
	public static int getHorizontalGap() {
		return horizontalGap;
	}

	/**
	 * Setuje rastojanje od gornje ivice i navigatora
	 * 
	 * @param horizontalGap
	 *            rastojanje od gornje ivice i navigatora
	 */
	public static void setHorizontalGap(int horizontalGap) {
		Navigator.horizontalGap = horizontalGap;
	}

	/**
	 * Setuje veličinu imaginarnog okvira oko navigatora
	 * 
	 * @param gap
	 *            veličina imaginarnog okvira oko navigatora
	 */
	public static void setGap(int gap) {
		Navigator.gap = gap;
	}

	/**
	 * Vraća rastojanje između move i zoom kontrole
	 * 
	 * @return rastojanje između move i zoom kontrole
	 */
	public int getZoomPanelGap() {
		return zoomPanelGap;
	}

	/**
	 * Setuje rastojanje između move i zoom kontrole
	 * 
	 * @param gap
	 *            rastojanje između move i zoom kontrole
	 */
	public static void setZoomPanelGap(int gap) {
		Navigator.zoomPanelGap = gap;
	}

	/**
	 * Vraća faktor brzine skrivanja i prikazivanja navigatora
	 * 
	 * @return faktor brzine skrivanja i prikazivanja navigatora
	 */
	public float getFadeFactor() {
		return Navigator.fadeFactor;
	}

	/**
	 * Setuje faktor brzine skrivanja i prikazivanja navigatora
	 * 
	 * @param fadeFactor
	 *            faktor brzine skrivanja i prikazivanja navigatora
	 */
	public static void setFadeFactor(float fadeFactor) {
		Navigator.fadeFactor = fadeFactor;
	}

	/**
	 * Vraća okvir Navigatora
	 * 
	 * @return okvir Navigatora
	 */
	public Rectangle getBounds() {
		if (getPosition() == null)
			return null;

		return new Rectangle(getPosition().x, getPosition().y, getWidth(),
				getHeight());
	}

	/**
	 * Vraća okvir move kontrole
	 * 
	 * @return okvir move kontrole
	 */
	private Ellipse2D getMovePanelBounds() {
		Point movePanelPosition = getMovePanelPosition();
		return new Ellipse2D.Double(movePanelPosition.x, movePanelPosition.y,
				movePanelWidth, movePanelHeight);
	}

	/**
	 * Vraća okvir zoom kontrole
	 * 
	 * @return okvir zoom kontrole
	 */
	private Rectangle getZoomPanelBounds() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return new Rectangle(zoomPanelPosition.x, zoomPanelPosition.y,
				zoomPanelWidth, zoomPanelHeight);
	}

	/**
	 * Vraća okvir zoomPlus dugmeta
	 * 
	 * @return okvir zoomPlus dugmeta
	 */
	private Rectangle getPlusBounds() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return new Rectangle(zoomPanelPosition.x, zoomPanelPosition.y,
				zoomPanelWidth, 21);
	}

	/**
	 * Vraća okvir zoomMinus dugmeta
	 * 
	 * @return okvir zoomMInus dugmeta
	 */
	private Rectangle getMinusBounds() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return new Rectangle(zoomPanelPosition.x, zoomPanelPosition.y
				+ zoomPanelHeight - 21, zoomPanelWidth, 21);
	}

	/**
	 * Vraća okvir zoomBar kontrole
	 * 
	 * @return okvir zoomBar kontrole
	 */
	private Rectangle getZoomBarBounds() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return new Rectangle(zoomPanelPosition.x, zoomPanelPosition.y
				+ zoomPanelHeight / 2 - zoomBarHeight / 2, zoomBarWidth,
				zoomBarHeight);
	}

	/**
	 * Vraća visinu navigatora
	 * 
	 * @return visinu navigatora
	 */
	private int getHeight() {
		return movePanelHeight + getZoomPanelGap() + zoomPanelHeight + getGap()
				* 2;
	}

	/**
	 * Vraća širinu navigatora
	 * 
	 * @return širinu navigatora
	 */
	private int getWidth() {
		return movePanelWidth + getGap() * 2;
	}

	/**
	 * Vraća instancu slicice definisanu prosleđenom konstantom
	 * 
	 * @param image
	 *            konstanta koja repreyentuje slicicu
	 * @return instancu slicice
	 */
	private Image getImage(int image) {
		return images.get(image);
	}

	/**
	 * Ako je indikator prelaska preko move kontrole aktivan vraća
	 * <code>true</code>, ako nije <code>false</code>
	 * 
	 * @return indikator prelaska preko move kontrole aktivan vraća
	 *         <code>true</code>, ako nije <code>false</code>
	 */
	public boolean isSpotlightActive() {
		return (moveActionState == MOVE_SPOTLIGHT_ACTIVE);
	}

	/**
	 * Ako je indikator klika na zoomBar kontrolu aktivan vraća
	 * <code>true</code>, ako nije <code>false</code>
	 * 
	 * @return indikator klika na zoomBar kontrolu aktivan vraća
	 *         <code>true</code>, ako nije <code>false</code>
	 */
	public boolean isZoomBarActive() {
		return zoomBarActionState == ZOOM_BAR_ACTIVE;
	}

	/**
	 * Ako je indikator prelaska preko zoomBar kontrole aktivan vraća
	 * <code>true</code>, ako nije <code>false</code>
	 * 
	 * @return indikator prelaska preko zoomBar kontrole aktivan vraća
	 *         <code>true</code>, ako nije <code>false</code>
	 */
	public boolean isZoomBarHover() {
		return zoomBarActionState == ZOOM_BAR_HOVER;
	}

	/**
	 * Ako je indikator klika na zoomPlus dugme aktivan vraća <code>true</code>,
	 * ako nije <code>false</code>
	 * 
	 * @return indikator klika na zoomPlus dugme aktivan vraća <code>true</code>
	 *         , ako nije <code>false</code>
	 */
	public boolean isPlusActive() {
		return zoomActionState == ZOOM_PLUS_ACTIVE;
	}

	/**
	 * Ako je indikator klika na zoomMinus dugme aktivan vraća <code>true</code>
	 * , ako nije <code>false</code>
	 * 
	 * @return indikator klika na zoomMinus dugme aktivan vraća
	 *         <code>true</code>, ako nije <code>false</code>
	 */
	public boolean isMinusActive() {
		return zoomActionState == ZOOM_MINUS_ACTIVE;
	}

	/**
	 * Vraća viewer kontrolu na kojoj se Navigator iscrtava
	 * 
	 * @return
	 */
	public VisualiserView getView() {
		return view;
	}

	/**
	 * Iscrtava okvir u kome se nalazi Navigator
	 * 
	 */
	public void paintImmediately() {
		if (enableRepaint && view.isValid())
			view.paintImmediately(getBounds());
	}

	/**
	 * Setuje dozvolu iscravanja Navigatora prosleđenim parametrom. Ako je
	 * prosleđeni parametar <code>true</code> iscrtavanje omogućeno,
	 * <code>false</code> bez iscrtavanja
	 * 
	 * @param enableRepaint
	 *            ako je <code>true</code> iscrtavanje omogućeno,
	 *            <code>false</code> bez iscrtavanja
	 */
	public void setEnableRepaint(boolean enableRepaint) {
		this.enableRepaint = enableRepaint;
	}

	/**
	 * Vraća vrednost tačke u ortogonalnom koordinatnom sistemu
	 * 
	 * @param position
	 *            tačka čika se ortogonalna projekcija traži
	 * 
	 * @return projekciju tačke u ortogonalnom koordinatnom sistemu
	 */
	private Point getOrtogonal(Point position) {
		if (position == null)
			return null;

		Point movePanelPosition = getMovePanelPosition();
		Point p1 = (Point) position.clone();
		p1.translate(-movePanelPosition.x - movePanelWidth / 2,
				-movePanelPosition.y);
		p1.move(p1.x, movePanelHeight / 2 - p1.y);

		return p1;
	}

	/**
	 * Vraća vrednost pomeraja vertikalne komponente
	 * 
	 * @return rednost pomeraja vertikalne komponente
	 */
	public int getMoveX() {
		return (moveActionState == MOVE_SPOTLIGHT_ACTIVE) ? getOrtogonal(spotlightMousePosition).x
				: 0;
	}

	/**
	 * Vraća vrednost pomeraja horizontalne komponente
	 * 
	 * @return rednost pomeraja horizontalne komponente
	 */
	public int getMoveY() {
		return (moveActionState == MOVE_SPOTLIGHT_ACTIVE) ? getOrtogonal(spotlightMousePosition).y
				: 0;
	}

	/**
	 * Vraća faktor zoomiranja
	 * 
	 * @return faktor zoomiranja
	 */
	public double getZoomFactor() {
		Point startPosition = getZoomBarStartPosition();
		Point zoomBarPosition = getZoomBarPosition();
		Point currentPosition = (Point) zoomBarPosition.clone();
		return startPosition.y - currentPosition.y;
	}

	/**
	 * Realizuje fadeIn efekat
	 */
	private void fadeIn() {
		compositeValue += getFadeFactor();
		if (compositeValue > .99f) {
			compositeValue = 1f;
			fadeTimer.stop();
			showed = true;
		}
		paintImmediately();
	}

	/**
	 * Realizuje fadeOut efekat
	 */
	private void fadeOut() {
		compositeValue -= getFadeFactor();
		if (compositeValue < 0.01f) {
			compositeValue = 0f;
			fadeTimer.stop();
			showed = false;
		}
		paintImmediately();
	}

	/**
	 * Vraća pomeraj zoomBar kontrole u prosleđenoj tački
	 * 
	 * @param p tačka na osnovu koje se računa pomeraj
	 * 
	 * @return pomeraj zoomBar-a kontrole
	 */
	private int getZoomBarMovement(Point p) {
		int newY = p.y - zoomBarHeight / 2;

		Point zoomPanelPosition = getZoomPanelPosition();
		if (newY + zoomBarHeight / 2 < getZoomMaxValue()) {
			newY = zoomPanelPosition.y + 20;
		} else if (newY + zoomBarHeight > getZoomMinValue()) {
			newY = zoomPanelPosition.y + zoomPanelHeight - 20 - zoomBarHeight;
		}

		return newY - getZoomPanelPosition().y - zoomPanelHeight / 2
				+ zoomBarHeight / 2;
	}

	/**
	 * Vraća max granicu pomeranja zoomBar kontrole
	 * 
	 * @return max granicu pomeranja zoomBar kontrole
	 */
	private int getZoomMaxValue() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return zoomPanelPosition.y + 21 + zoomBarHeight / 2 - 1;
	}

	/**
	 * Vraća min granicu pomeranja zoomBar kontrole
	 * 
	 * @return min granicu pomeranja zoomBar kontrole
	 */
	private int getZoomMinValue() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return zoomPanelPosition.y + zoomPanelHeight - 20;
	}

	/**
	 * Postavlja zoomBar kontrolu u početno stanje
	 */
	private void resetZoomBarPosition() {
		zoomBarMovement = 0;
	}

	/**
	 * Vraća početnu poziciju zoomBar kontrole
	 * 
	 * @return početnu poziciju zoomBar kontrole
	 */
	private Point getZoomBarStartPosition() {
		Point zoomPanelPosition = getZoomPanelPosition();
		return new Point(zoomPanelPosition.x, zoomPanelPosition.y
				+ zoomPanelHeight / 2 - zoomBarHeight / 2);
	}

	/**
	 * Reaguje na pomeraj miša iznad Navigaotra
	 * 
	 * @param e događaj miša
	 */
	public void mouseMoved(MouseEvent e) {
		int showState = getShowState();
		if (showState == SHOW_NEVER) {
			return;
		}
		view.setToolTipText(null);
		if (getBounds() != null && getBounds().contains(e.getPoint())) {
			if (showState == SHOW_AUTOMATICALLY) {
				hover = true;
			}

			if (getMovePanelBounds().contains(e.getPoint())) {
				moveActionState = MOVE_SPOTLIGHT_HOVER;
				spotlightMousePosition = e.getPoint();
			} else {
				moveActionState = MOVE_NORM;
				spotlightMousePosition = null;
			}

			if (getZoomPanelBounds().contains(e.getPoint())) {
				if (getPlusBounds().contains(e.getPoint())) {
					zoomActionState = ZOOM_PLUS_HOVER;
				} else if (getMinusBounds().contains(e.getPoint())) {
					zoomActionState = ZOOM_MINUS_HOVER;
				} else if (getZoomBarBounds().contains(e.getPoint())) {
					zoomBarActionState = ZOOM_BAR_HOVER;
					view.setToolTipText("Double-click to see best fit view.");
				} else {
					zoomActionState = ZOOM_NORM;
					zoomBarActionState = ZOOM_BAR_NORM;
					view.setToolTipText(null);
				}
			} else {
				zoomActionState = ZOOM_NORM;
				zoomBarActionState = ZOOM_BAR_NORM;
			}
			paintImmediately();

		} else {
			moveActionState = MOVE_NORM;
			spotlightMousePosition = null;
			zoomActionState = ZOOM_NORM;
			zoomBarActionState = ZOOM_BAR_NORM;
			view.setToolTipText(null);

			if (showState == SHOW_AUTOMATICALLY) {
				hover = false;
				if (showed) {
					paintImmediately();
				}
			}
		}
	}

	/**
	 * Reaguje na klik miša iznad Navigaotra
	 * 
	 * @param e događaj miša
	 */
	public void mousePressed(MouseEvent e) {
		int showState = getShowState();
		if (showState == SHOW_NEVER) {
			return;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			if (getMovePanelBounds().contains(e.getPoint())) {
				moveActionState = MOVE_SPOTLIGHT_ACTIVE;
				spotlightMousePosition = e.getPoint();
			}

			if (getZoomPanelBounds().contains(e.getPoint())) {
				if (getPlusBounds().contains(e.getPoint())) {
					zoomActionState = ZOOM_PLUS_ACTIVE;
				} else if (getMinusBounds().contains(e.getPoint())) {
					zoomActionState = ZOOM_MINUS_ACTIVE;
				} else if (getZoomBarBounds().contains(e.getPoint())) {
					zoomBarActionState = ZOOM_BAR_ACTIVE;
				} else {
					zoomActionState = ZOOM_NORM;
					zoomBarActionState = ZOOM_BAR_NORM;
				}
			}
			paintImmediately();
		}
	}

	/**
	 * Reaguje na otpuštanje miša iznad Navigaotra
	 * 
	 * @param e događaj miša
	 */
	public void mouseReleased(MouseEvent e) {
		int showState = getShowState();
		if (showState == SHOW_NEVER) {
			return;
		}

		if (getMovePanelBounds().contains(e.getPoint())) {
			moveActionState = MOVE_SPOTLIGHT_HOVER;
			spotlightMousePosition = e.getPoint();
		} else {
			moveActionState = MOVE_NORM;
			spotlightMousePosition = null;
		}

		if (getZoomPanelBounds().contains(e.getPoint())) {

			if (getPlusBounds().contains(e.getPoint())) {
				zoomActionState = ZOOM_PLUS_HOVER;
			} else if (getMinusBounds().contains(e.getPoint())) {
				zoomActionState = ZOOM_MINUS_HOVER;
			} else if (getZoomBarBounds().contains(e.getPoint())) {
				zoomBarActionState = ZOOM_BAR_HOVER;
			} else {
				zoomActionState = ZOOM_NORM;
				zoomBarActionState = ZOOM_BAR_NORM;
			}
		} else {
			zoomActionState = ZOOM_NORM;
			zoomBarActionState = ZOOM_BAR_NORM;
		}
		resetZoomBarPosition();

		getView().repaint();
	}

	/**
	 * Reaguje na prevlačenje miša iznad Navigaotra
	 * 
	 * @param e događaj miša
	 */
	public void mouseDragged(MouseEvent e) {
		int showState = getShowState();
		if (showState == SHOW_NEVER) {
			return;
		}

		if (getMovePanelBounds().contains(e.getPoint())) {
			moveActionState = MOVE_SPOTLIGHT_ACTIVE;
			spotlightMousePosition = e.getPoint();
		} else {
			moveActionState = MOVE_NORM;
			spotlightMousePosition = null;
		}

		if (zoomBarActionState == ZOOM_BAR_ACTIVE) {
			zoomBarMovement = getZoomBarMovement(e.getPoint());
			;
		}

		paintImmediately();
	}
}
