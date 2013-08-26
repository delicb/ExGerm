package tk.exgerm.dabsearch;

import java.util.ArrayList;

import tk.exgerm.core.exception.ExGIteratorDoesNotExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGIterator;

public class Search {
	
	private IGraph graph;
	private INode start, finish;

	/**
	 * Kreira pretragu u okviru zadanog grafa od start do finish noda.
	 * 
	 * @param graph
	 *            graf u kome se vrsi pretraga
	 * @param start
	 *            početak pretrage
	 * @param finish
	 *            cilj pretrage
	 */
	public Search(IGraph graph, INode start, INode finish) {
		this.graph = graph;
		this.start = start;
		this.finish = finish;
	}
	
	public ArrayList<INode> search(String type) throws ExGNodeDoesNotExsistException{
		
		ExGIterator it = null;

			try {
				it = graph.getIterator(type);
			} catch (ExGIteratorDoesNotExsistException e) {
				// uvek postoji
				e.printStackTrace();
			}
			//iterator postuje usmerenje grana
			it.setRespectEdgeDirections(true);
			//iterator pocinje od start noda
			it.setStart(start);
			INode temp = null;
			while(it.hasNext()){
				temp = it.next();
				if(temp == finish)
					break;
			}
			if(temp!=finish)
				return null;
			
			ArrayList<INode> result = returnArray(temp);
			it.clear();
			return result;
	}
	
	private ArrayList<INode> returnArray(INode temp) {
		ArrayList<INode> result = new ArrayList<INode>();
		INode end = temp;
		
		result.add(end);
		while(end.getData(ExGIterator.PARENT)!=null){
			result.add(0,(INode) end.getData(ExGIterator.PARENT));
			end = (INode) end.getData(ExGIterator.PARENT);
			if(end.getData(ExGIterator.PARENT)!=null)
				result.add(0,null);
		}
		
		return result;
	}
	

}
