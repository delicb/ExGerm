package tk.exgerm.visualiser.state;

import java.awt.event.MouseEvent;
import java.util.HashMap;

import tk.exgerm.visualiser.Visualiser;
import tk.exgerm.visualiser.view.VisualiserView;

/**
 * Predstavlja rukovaoc stanjima u kojima se {@link Visualiser Visualiser
 * komponenta} može naći. Realizuje postavljanje aplikacije u naredno stanje,
 * dodaje osluškivače {@link VisualiserView Viewer} komponenti.<br>
 * <br>
 * Da bi se dodalo novo stanje potrebno je definisati novu konstantu u
 * {@link States} atributu klase {@link StateManager} i u konstruktoru pozvati
 * metodu {@link #addState(States, State)}. Ona kao parametre prima konstantu
 * koja reprezentuje stanje i konkretnu instancu klase koja nasleđuje apstraktnu
 * klasu {@link State}.
 * 
 * @see State
 * @see VisualiserView
 * 
 * @author Tim 2
 * 
 */
public class StateManager {
	/**
	 * Konstante koje reprezentuju stanja
	 */
	public enum States {
		POINT, LASSO, ERASER, ADD, EDGE, NAVIGATE, DIR_EDGE
		// NOTE: dodati konstante po potrebi
	}

	/**
	 * viewer komponenta
	 */
	private VisualiserView view;

	/**
	 * Stanje u kome se sistem trenutno nalazi
	 */
	private State currentState;
	
	/**
	 * Prethodno stanje u kome se sistem nalazio
	 */
	private State previousState;

	HashMap<States, State> states = new HashMap<States, State>();

	/**
	 * Konstruktor, kao parametar prima podatak o {@link VisualiserView
	 * vizuelnoj komponenti} cija ce stanja kontrolisati}. Inicijalizuje stanja
	 * sistema i postavlja sistem u početno stanje.
	 * 
	 * @param view
	 *            {@link VisualiserView vizuelna komponenta}
	 */
	public StateManager(VisualiserView view) {
		this.view = view;

		addState(States.POINT, new PointState(this));
		addState(States.LASSO, new LassoState(this));
		addState(States.ERASER, new EraserState(this));
		addState(States.ADD, new AddState(this));
		addState(States.EDGE, new EdgeState(this));
		addState(States.DIR_EDGE, new DirectedEdgeState(this));
		addState(States.NAVIGATE, new NavigatorState(this));
		// NOTE: dodati instance stanja po potrebi

		// postavljamo sistem u pocetno stanje
		this.currentState = getState(States.POINT);
		updateListeners();
	}

	/**
	 * Dodavanje stanja u kome se {@link VisualiserView vizuelna komponenta}
	 * može naći
	 * 
	 * @param constant
	 *            konstanta koja reprezentuje stanje
	 * @param state
	 *            instanca klase u kojoj je implementirano ponašanje sistema
	 */
	private void addState(States constant, State state) {
		states.put(constant, state);
	}

	/**
	 * Vraća instancu klase u kojoj je implementirano ponašanje sistema za
	 * prosledjenu konstantu.
	 * 
	 * @param constant
	 *            konstanta koja reprezentuje stanje
	 * @return instanca klase u kojoj je implementirano ponašanje sistema
	 */
	private State getState(States constant) {
		return states.get(constant);
	}
	
	private States getStateConstant(State state){
		for (Object key : states.keySet()) {
			if(states.get(key).equals(state)){
				return (States)key;
			}
		}
		
		return null;
	}
	
	/**
	 * Postavlja sistem u željeno stanje.
	 * 
	 * @param constant
	 *            konstanta koja reprezentuje stanje u koje se sistem uvodi
	 */
	public void setNextState(States constant, MouseEvent e) {
		this.previousState = this.currentState;
		this.currentState = getState(constant);
		updateListeners();
		getCurrentState().initialise(e);
	}

	/**
	 * Vraća instancu klase trenutnog stanja sistema
	 * 
	 * @return instancu klase trenutnog stanja sistema
	 */
	private State getCurrentState() {
		return currentState;
	}

	/**
	 * Vraća konstantu prethodnog stanja sistema
	 * 
	 * @return konstantu prethodnog stanja sistema
	 */
	public States getPreviousState() {
		return getStateConstant(previousState);
	}
	
	/**
	 * Vraća @link {@link VisualiserView vizualnu komponentu} čijim stanjima
	 * upravlja
	 * 
	 * @return {@link VisualiserView vizualnu komponentu} čijim stanjima
	 *         upravlja
	 */
	public VisualiserView getView() {
		return view;
	}

	/**
	 * Dodaje osluskivace Viewer komponenti
	 * 
	 * NOTE: MouseWheelListener definisan u apstraktnoj klasi State, pa ga nije
	 * potrenbno dodavati prilikom promene stanja. Dodaje se, mozda se kasnije
	 * javi potreba
	 */
	private void updateListeners() {
		if (getView().getMouseListeners().length > 0)
			getView().removeMouseListener(getView().getMouseListeners()[0]);
		if (getView().getMouseMotionListeners().length > 0)
			getView().removeMouseMotionListener(
					getView().getMouseMotionListeners()[0]);
		if (getView().getMouseWheelListeners().length > 0)
			getView().removeMouseWheelListener(
					getView().getMouseWheelListeners()[0]);

		getView().addMouseListener(getCurrentState());
		getView().addMouseMotionListener(getCurrentState());
		getView().addMouseWheelListener(getCurrentState());
	}
}
