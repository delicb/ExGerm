package tk.exgerm.graphstatis;

import tk.exgerm.core.model.IGraph;

/**
 * Sve klase iz ove komponente koje analiziraju graf, treba ovaj interace da
 * implementiraju
 * 
 * @author Tim 2
 */
public interface StatisticTool {
	/**
	 * Postavlja graf nad kojim treba da se nađe određena statistika.
	 * 
	 * @param graph
	 *            Graf koji se analizira
	 */
	public void setGraph(IGraph graph);

	/**
	 * Vraća rezultat izvršavanja. Pošto rezulzaz izvršavanja može da bude dosta
	 * stvari, a u svakom slučaju će biti ispisano, odgovornost konkretne
	 * implementacije je da kreira string sa rezultatom.
	 * 
	 * @return Rezultat izvršavanja statistike.
	 */
	public String getResult();

	/**
	 * Vraća ime konkretnog alata za statistiku.
	 * 
	 * @return Ime
	 */
	public String getName();

	/**
	 * Opis. Treba ukratko da sadrži šta statistika radi, kakav će joj biti
	 * rezultat, i slične stvari.
	 * 
	 * @return Objašnjenje šta se analizira u grafu.
	 */
	public String getDescription();
}
