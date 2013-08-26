package tk.exgerm.core.impl.model;

/**
 * Generator rednih brojeva. Koristi se za identifikaciju grana
 * 
 * @author Tim 2
 */
public class IDGenerator {

	private int counter;

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the step
	 */
	public int getStep() {
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(int step) {
		this.step = step;
	}

	private int step;

	/**
	 * Novi generator, startuje od 1 povecava se sa korakom od po 1
	 */
	public IDGenerator() {
		this(1, 1);
	}

	/**
	 * Kreira novi generator, sa mogućnošću definisanja starta i podrazumevanim
	 * korakom 1
	 * 
	 * @param start
	 *            Početak od koga se generišu ID-jevi
	 */
	public IDGenerator(int start) {
		this(start, 1);
	}

	/**
	 * Kreira novi generator, sa mogućnošću definisanja početka i koraka.
	 * 
	 * @param start
	 *            Početak od koga se generišu ID-jevi
	 * @param step
	 *            Korak za koliko se uvećava svaki sledeći ID
	 */
	public IDGenerator(int start, int step) {
		this.counter = start;
		this.step = step;
	}

	/**
	 * Generiše novi ID.
	 * @return Novogenerisani ID.
	 */
	public int generateID() {
		int toReturn = this.counter;
		this.counter += step;
		return toReturn;
	}
	
	/**
	 * Vraća prethodni izgenerisani ID.
	 * @return
	 */
	public int getID() {
		return this.counter;
	}
}
