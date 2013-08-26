package tk.exgerm.graphgenerator;

import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.exception.ExGNodeAlreadyExsistException;
import tk.exgerm.core.exception.ExGNodeDoesNotExsistException;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.service.ICoreContext;

public class Generate {
	
	private ICoreContext context;
	
	public Generate(ICoreContext _context){
		this.context = _context;
	}
	
	/**
	 * Generiše graf u obliku putanje
	 * 
	 * @param n - broj čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void path(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}
		
		for(int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i+1));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}	
		INode source;
		INode dest;
		
		for(int i = 1; i < n; i++){
			source = newGraph.getNode(Integer.toString(i));
			dest = newGraph.getNode(Integer.toString(i+1));
			
			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Genriše graf u obliku zatvorene putanje - kružnica
	 * 
	 * @param n - broj čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void cycle(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}
		
		for(int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i+1));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;
		int i;
		
		for(i = 1; i < n; i++){
			source = newGraph.getNode(Integer.toString(i));
			dest = newGraph.getNode(Integer.toString(i+1));
			
			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}
		}

		source = newGraph.getNode(Integer.toString(1));
		dest = newGraph.getNode(Integer.toString(n));

		try {
			newGraph.addEdge(source, dest, false);
		} catch (ExGNodeDoesNotExsistException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Genriše graf u obliku binarnog drveta
	 * 
	 * @param h - parametar, generiše se (2^h)-1 čvorova i (2^h)-2 veza 
	 * @param graphName - ime generisanog grafa
	 */
	public void completeBinaryTree(int h, String graphName){
		int powered = (int) Math.pow (2, h) - 1;
		
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}
		
		for (int i = 0; i < powered; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;
		
		for (int i = 1; i < powered; i++){
			source = newGraph.getNode(Integer.toString((i-1)/2));
			dest = newGraph.getNode(Integer.toString(i));
			
			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Generiše graf u obliku mreže trouglova
	 * 
	 * @param h - parametar, generiše se n(n-1)/2 čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void triangularMesh(int h, String graphName){
		int n = (h * (h + 1)) / 2;
		
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for(int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		int tmp = 0;
		INode source;
		INode dest;
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j <= i; j++) {
				if (j < i){
					source = newGraph.getNode(Integer.toString(tmp));
					dest = newGraph.getNode(Integer.toString(tmp+1));
					
					try {
						newGraph.addEdge(source, dest, false);
					} catch (ExGNodeDoesNotExsistException e) {
						e.printStackTrace();
					}
				}
				if (i < h - 1){
					source = newGraph.getNode(Integer.toString(tmp));
					dest = newGraph.getNode(Integer.toString(tmp+i+1));

					try {
						newGraph.addEdge(source, dest, false);
					} catch (ExGNodeDoesNotExsistException e) {
						e.printStackTrace();
					}
					
					source = newGraph.getNode(Integer.toString(tmp));
					dest = newGraph.getNode(Integer.toString(tmp+i+2));

					try {
						newGraph.addEdge(source, dest, false);
					} catch (ExGNodeDoesNotExsistException e) {
						e.printStackTrace();
					}
				}
				++tmp;
			}
		}
	}
	
	/**
	 * Generiše graf u obliku mreže kvadrata
	 * 
	 * @param h - parametar, generiše se n^2 čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void squareMesh(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				INode node = context.newNode(Integer.toString(i) + Integer.toString(j));
				try {
					newGraph.addNode(node);
				} catch (ExGNodeAlreadyExsistException e) {
					e.printStackTrace();
				}
			}
		}
		
		INode source;
		INode dest;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - 1; j++) {
				source = newGraph.getNode(Integer.toString(i) + Integer.toString(j));
				dest = newGraph.getNode(Integer.toString(i) + Integer.toString(j+1));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
				
				source = newGraph.getNode(Integer.toString(j) + Integer.toString(i));
				dest = newGraph.getNode(Integer.toString(j+1) + Integer.toString(i));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Generiše kompletan graf - svaki čcor je povezan sa svakim
	 * 
	 * @param n - parametar
	 * @param graphName - ime generisanog grafa
	 */
	public void completeGraph(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;
		
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				source = newGraph.getNode(Integer.toString(i));
				dest = newGraph.getNode(Integer.toString(j));
				
				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Generiše graf u obliku hiperkocke
	 * 
	 * @param n - parametar, generiše se 2^n čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void hypercube(int n, String graphName){
		int powered = (int) Math.pow (2, n);
		
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < powered; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;
		
		for (int i = 0; i < powered; i++) {
			for (int j = 1; j < powered; j*=2) {
				if ((i & j) == 0){
					source = newGraph.getNode(Integer.toString(i));
					dest = newGraph.getNode(Integer.toString(i+j));
	
					try {
						newGraph.addEdge(source, dest, false);
					} catch (ExGNodeDoesNotExsistException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void completeBipartiteGraph(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < 2 * n; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;

		for (int i = 0; i < n; i++){
			for (int j = n; j < 2 * n; j++){
				source = newGraph.getNode(Integer.toString(i));
				dest = newGraph.getNode(Integer.toString(j));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Generiše graf u obliku torusa
	 * 
	 * @param n - parametar, generiše se n^2 čvorova
	 * @param graphName - ime generisanog grafa
	 */
	public void thorus(int n, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				INode node = context.newNode(Integer.toString(i) + Integer.toString(j));
				try {
					newGraph.addNode(node);
				} catch (ExGNodeAlreadyExsistException e) {
					e.printStackTrace();
				}
			}
		}
		
		INode source;
		INode dest;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - 1; j++) {
				source = newGraph.getNode(Integer.toString(i) + Integer.toString(j));
				dest = newGraph.getNode(Integer.toString(i) + Integer.toString(j+1));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
				
				source = newGraph.getNode(Integer.toString(j) + Integer.toString(i));
				dest = newGraph.getNode(Integer.toString(j+1) + Integer.toString(i));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}
			source = newGraph.getNode(Integer.toString(0) + Integer.toString(i));
			dest = newGraph.getNode(Integer.toString(n-1) + Integer.toString(i));

			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}

			source = newGraph.getNode(Integer.toString(i) + Integer.toString(0));
			dest = newGraph.getNode(Integer.toString(i) + Integer.toString(n-1));

			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Generiše graf u obliku kružnice za čije su čvorove vezani kompletni grafovi
	 * 
	 * @param numberOfCliques - broj čvorova u kružnici
	 * @param sizeOfClique - veličina kompletnog grafa
	 * @param graphName - ime generisanog grafa
	 */
	public void cycleOfCliques(int numberOfCliques, int sizeOfClique, String graphName){
		int n = numberOfCliques * sizeOfClique;
		
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}

		for (int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}
		
		INode source;
		INode dest;
		
		for (int i = 0; i < numberOfCliques ; i++){
			int cur = i * sizeOfClique;
			if (i < numberOfCliques - 1){
				source = newGraph.getNode(Integer.toString(cur));
				dest = newGraph.getNode(Integer.toString(cur+sizeOfClique));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}else{
				source = newGraph.getNode(Integer.toString(cur));
				dest = newGraph.getNode(Integer.toString(0));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
			}
			
			for (int j = cur + 1; j < cur + sizeOfClique; j++) {
				for (int k = cur; k < j; k++) {
					source = newGraph.getNode(Integer.toString(k));
					dest = newGraph.getNode(Integer.toString(j));

					try {
						newGraph.addEdge(source, dest, false);
					} catch (ExGNodeDoesNotExsistException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Generiše random povezani graf. Koristi se za randomTree i za sam randomConnGraph.
	 * U zavisnosti od parametara n i m daje rezultate.
	 * 
	 * @param n - broj čvorova
	 * @param m - broj veza
	 * @param graphNamee - ime generisanog grafa grafa
	 */
	public void randomConnectedGraph(int n, int m, String graphName){
		IGraph newGraph = context.newGraph(graphName);
		
		try {
			context.addGraph(newGraph);
		} catch (ExGGraphExsistException e) {
			return;
		}
		
		for(int i = 0; i < n; i++){
			INode node = context.newNode(Integer.toString(i));
			try {
				newGraph.addNode(node);
			} catch (ExGNodeAlreadyExsistException e) {
				e.printStackTrace();
			}
		}

		INode source;
		INode dest;
		
		for(int i = 1; i < n; i++){
			source = newGraph.getNode(Integer.toString(randomInt(i-1)));
			dest = newGraph.getNode(Integer.toString(i));
			
			try {
				newGraph.addEdge(source, dest, false);
			} catch (ExGNodeDoesNotExsistException e) {
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < m - (n - 1); i++) {
			outerLoop: while (true) {
				int from = randomInt (n-1);
				int to = randomInt (n-1);
				if (from >= to) continue;
				for (int j = 0; j < n - 1 + i; j++) {
					IEdge e = context.getGraph(graphName).getAllEdges().get(j);
					if( (context.getGraph(graphName).getAllNodes().get(from) == e.getFrom()) &&
							(context.getGraph(graphName).getAllNodes().get(to) == e.getTo()) ){
						continue outerLoop;
					}
				}
				source = newGraph.getNode(Integer.toString(from));
				dest = newGraph.getNode(Integer.toString(to));

				try {
					newGraph.addEdge(source, dest, false);
				} catch (ExGNodeDoesNotExsistException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	/**
	 * Pomoćna random funkcija
	 * 
	 * @param n
	 * @return 
	 */
	private int randomInt (int n) {return (int) (Math.random () * n);}

}
