package tk.exgerm.core.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tk.exgerm.core.Core;
import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.plugin.ExGGraphRegister;

/**
 * Proxy klasa za {@link ExGGraphRegister}. Podržava više registara i svaki od
 * njih može da čuva grafove gde god želi.
 * 
 * U ovoj klasi je implementiran podrazumevani registar grafova, koji čuva
 * podatke u RAM-u da bi sve radilo čak iako konkretna komponenta koja
 * implementira ovu funkcionalnost ne postoji u sistemu.
 * 
 * @author Tim 2
 * 
 */
public class GraphRegisterProxy implements ExGGraphRegister {

	/**
	 * Jedan proxy može da ima više servisa (na primer, jedan lokalni, jedan što
	 * drži grafove na mreži...). Mi ćemo imati jedan ili ni jedan, ali ovo
	 * ostavlja mogućnost da ih ima više.
	 */
	List<ExGGraphRegister> realRegisters;

	Map<String, IGraph> graphs;

	public GraphRegisterProxy() {
		this.realRegisters = new ArrayList<ExGGraphRegister>();
		this.graphs = new HashMap<String, IGraph>();
	}

	/**
	 * Dodavanje pravog graf registra sa kojim proxy radi. Pošto je default
	 * impelementacija takva da ne pamti stanje registra između pokretanja
	 * programa, ako spoljnji registrar to radi kopiraćemo sve njegove podatke.
	 * 
	 * Takođe, ako se registrar prijavi u toku rada kad već u default
	 * implementaciji postoje grafovi dodaćemo postojeće grafove.
	 * 
	 * @param register
	 *            Pravi graf registar.
	 */
	public void addBackend(ExGGraphRegister register) {
		this.realRegisters.add(register);
		replicateToRegister(register);
		replicateFromRegister(register);
	}

	/**
	 * Uklanja registar iz liste aktivnih. Pošto se svi podaci repliciraju, kad
	 * se registar imaće sve iste podatke kao i svi ostali registri, tako da se
	 * iz njega ništa ne briše niti se dodaje.
	 * 
	 * @param register
	 *            Registar koji se uklanja.
	 */
	public void removeBackend(ExGGraphRegister register) {
		this.realRegisters.remove(register);
	}

	@Override
	public void addGraph(IGraph graph) throws ExGGraphExsistException {
		if (graphs.containsKey(graph.getName()))
			throw new ExGGraphExsistException("Graph with name "
					+ graph.getName() + " exists in register");
		else {
			graphs.put(graph.getName(), graph);
		}
		
		if (this.realRegisters.size() > 0) {
			Iterator<ExGGraphRegister> it = this.realRegisters.iterator();
			while (it.hasNext()) {
				it.next().addGraph(graph);
			}
		}
		
		// obavestavamo sve koji su zainteresovani
		Core.getInstance().getEventDispatcher().raiseEvent(
				ExGGraphRegister.GRAPH_ADDED, graph);
	}

	@Override
	public List<IGraph> getAllGraphs() {
		// posto su svi podaci redudantni, dovoljno je da vratimo 
		// ono sto stoji kod nas
		return new ArrayList<IGraph>(graphs.values());
	}

	@Override
	public IGraph getGraph(String name) {
		return graphs.get(name);
	}

	@Override
	public void removeGraph(String name) throws ExGGraphDoesNotExsistException {
		removeGraph(graphs.get(name));
	}

	@Override
	public void removeGraph(IGraph graph) throws ExGGraphDoesNotExsistException {
		if (! graphs.containsValue(graph))
			throw new ExGGraphDoesNotExsistException("Graph is not registered in registry.");
		
		else {
			graphs.remove(graph.getName());
		}
		
		// uklanjanje istog grafa iz svih backed registara
		if (realRegisters.size() > 0) {
			for (ExGGraphRegister register : realRegisters) {
				register.removeGraph(graph);
			}
		}
		
		// obavestavamo sve zainteresovane
		Core.getInstance().getEventDispatcher().raiseEvent(
				ExGGraphRegister.GRAPH_REMOVED, graph);
	}

	@Override
	public boolean containsGraph(String name) {
		return graphs.containsKey(name);
	}

	private void replicateToRegister(ExGGraphRegister register) {
		for (IGraph g : graphs.values()) {
			// ako postoji graf sa istim imenom u registru, pretpostavka je
			// da je nas aktuelniji
			if (register.containsGraph(g.getName())) {
				try {
					register.removeGraph(g);
					register.addGraph(g);
				} catch (ExGGraphDoesNotExsistException e) {
					// posto smo proverili da li postoji, ovaj exception ne bi
					// trebao nikad da se desi
				} catch (ExGGraphExsistException e) {
					// posto smo prvo uklonili graf sa istim imenom
					// ovaj exception ne bi trebalo nikad da se desi
				}
			}
		}
	}

	private void replicateFromRegister(ExGGraphRegister register) {
		List<IGraph> allGraph = register.getAllGraphs();
		for (IGraph g : allGraph) {
			// prestavka je da je naš registar aktuelniji, pa
			// očuvavamo naše podatke
			if (!graphs.containsKey(g.getName())) {
				try {
					addGraph(g);
				} catch (ExGGraphExsistException e) {
					// pošto smo prvo proverili da li graf postoji,
					// ovaj exception nikad ne bi trebao da bude bačen.
				}
			}
		}
	}
	
	public void renameGraph(IGraph graph, String oldName) {
		if (graphs.containsKey(oldName)) {
			graphs.remove(oldName);
			graphs.put(graph.getName(), graph);
		}
	}
}
