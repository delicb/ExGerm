package tk.exgerm.visualiser.state;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import tk.exgerm.visualiser.model.VisualiserModel;
import tk.exgerm.visualiser.state.StateManager.States;
import tk.exgerm.visualiser.view.VisualiserView;

/**
 * Predstavlja apstraktnu klasu koju nasleđuju klase koje implementiraju
 * ponašanje sistema u određenom stanju. Klase koje je nasleđuju u konstruktoru
 * moraju inicijalizovati atribut {@link StateManager stateManager}.
 * Implementira {@link MouseAdapter rukovaoce dogadjajima misa}. Sadrži
 * apstraktnu metodu {@link #initialise()} koja se redefiniše u klasama
 * naslednicama i služi za postavljanje inicijalnih paramatara u okviru stanja. <br>
 * <br>
 * Ukoliko se u metodama klasa naslednica želi izvršiti funkcionalnost
 * implementirana metodama ove klase, poziva se željena metoda promenljive
 * <code>super</code>.<br>
 * <br>
 * Sistem se postavlja u naredno stanje pozivom metode
 * {@link #setNextState(States)}, koja kao parametar prima konstantu koja
 * reprezentuje stanje u koje želimo uvesti sistem. <br>
 * <br>
 * Metoda {@link #getView()} vraća {@link VisualiserView vizuelnu komponentu}
 * čijim se stanjima upravlja, a {@link #getModel()} {@link VisualiserModel
 * model} koji ona prikazuje.
 * 
 * @author Tim 2
 * 
 */
public abstract class State extends MouseAdapter {

	protected StateManager stateManager;

	private Point2D initialDragPosition;
	private Cursor oldCursor;
	private boolean dragInitiated = false;

	/**
	 * Apstraktna metoda koja služi za postavljanje inicijalnih paramatara u
	 * okviru stanja.
	 * @param e 
	 */
	public abstract void initialise(MouseEvent e);

	public void mousePressed(MouseEvent e) {
		getView().grabFocus();
		// Drag na srednje dugme, pochetna tachka
		if (e.getButton() == MouseEvent.BUTTON2) {
			initialDragPosition = e.getPoint();
			oldCursor = getView().getCursor();
			dragInitiated = true;
			getView().transformToUserSpace(initialDragPosition);
		}
	}

	public void mouseReleased(MouseEvent e) {
		// Drag na srednje dugme, krajnja tachka
		if (e.getButton() == MouseEvent.BUTTON2) {
			getView().setCursor(oldCursor);
			dragInitiated = false;
		}
	}

	public void mouseDragged(MouseEvent e) {
		// Drag na srednje dugme
		if (dragInitiated) {
			Point2D mousePos = e.getPoint();

			getView().transformToUserSpace(mousePos);
			getView().translateTo(mousePos, initialDragPosition);
			getView().repaint();
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {
			// Zoom u tacki
			getView().zoom(e.getPoint(), -e.getWheelRotation());
			getView().repaint();
		} else if (e.isShiftDown()) {
			// skrol po X osi
			getView().translateHorizontally(e.getWheelRotation());
		} else {
			// skrol po Y osi
			getView().translateVertically(e.getWheelRotation());
		}
		getView().repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3){
			getView().visualization.stop();
			getView().setVisual(false);
			getView().repaint();
		}
	}

	/**
	 * Vraća vizuelnu komponentu čijim stanjima upravlja.
	 * 
	 * @return vizuelna komponenta čijim stanjima upravlja
	 */
	public VisualiserView getView() {
		return stateManager.getView();
	}

	/**
	 * Vraća model koji se prikazuje u vizuelna komponenta.
	 * 
	 * @return model koji se prikazuje u vizuelna komponenta
	 */
	public VisualiserModel getModel() {
		return stateManager.getView().getModel();
	}
}
