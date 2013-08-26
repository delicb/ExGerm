package tk.exgerm.console.commands;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tk.exgerm.core.exception.ExGCommandErrorException;
import tk.exgerm.core.exception.ExGCommandErrorException.CommandErrorType;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.service.ICoreContext;

public class AttributesCommand implements ExGCommand {

	ICoreContext context;

	public AttributesCommand(ICoreContext context) {
		super();
		this.context = context;
	}

	@Override
	public Object execute(PrintStream out, String... params)
			throws ExGCommandErrorException {
		IGraph graph = (IGraph) context.getData(ExGCommand.CONSOLE_ACTIVE_GRAPH);
		INode node = (INode) context.getData(ExGCommand.CONSOLE_ACTIVE_NODE);
		
		//1 parametar - att
		if(params.length == 1){
			if (graph == null)
				throw new ExGCommandErrorException(CommandErrorType.ERROR,"No graph or node selected to display attributes.");
			else {
				if (node == null) {
					// listanje atributa selektovanog grafa
					printAtt(out, graph.getAllAttributes());
				} else {
					// listanje atributa selektovanog noda
					printAtt(out, node.getAllAttributes());
				}
			}
			//2 parametra att IME_NODA_ILI_GRAFA
		} else if(params.length == 2){
			if(graph!=null){
				//ispis attribuda noda ako postoji u grafu
				if(graph.getNode(params[1])==null)
					throw new ExGCommandErrorException(CommandErrorType.ERROR,"Node >"+params[1]+"< not found!");
				printAtt(out,graph.getNode(params[1]).getAllAttributes());
			}else{
				//ispis atributa grafa ako postoji u registru
				if(context.getGraph(params[1])==null)
					throw new ExGCommandErrorException(CommandErrorType.ERROR,"Graph >"+params[1]+"< not found!");
				printAtt(out,context.getGraph(params[1]).getAllAttributes());
			}
			//3 parametra att NODE/EDGE/GRAPH IME_ENTITETA
		} else if(params.length == 3){
			if(graph!=null){
				//moze edge, node iz grafa ili graph iz registra
				if(params[1].equalsIgnoreCase("graph")){
					//ispis atributa grafa ako postoji u registru
					if(context.getGraph(params[2])==null)
						throw new ExGCommandErrorException(CommandErrorType.ERROR,"Graph >"+params[2]+"< not found!");
					printAtt(out,context.getGraph(params[2]).getAllAttributes());
				}
				if(params[1].equalsIgnoreCase("node")){
					//ispis attribuda noda ako postoji u grafu
					if(graph.getNode(params[1])==null)
						throw new ExGCommandErrorException(CommandErrorType.ERROR,"Node >"+params[1]+"< not found!");
					printAtt(out,graph.getNode(params[1]).getAllAttributes());
				}
				if(params[1].equalsIgnoreCase("edge")){
					//ispis atributa edgea ako postoji u grafu
					int id;
					try{
						id = Integer.parseInt(params[2]);
					}catch(NumberFormatException e){
						throw new ExGCommandErrorException(CommandErrorType.ERROR,params[2] + "is not a legal edge ID! ID must be a number.");
					}
					if(graph.getEdge(id)==null)
						throw new ExGCommandErrorException(CommandErrorType.ERROR,"Edge with id >"+params[2]+"< not found!");
					printAtt(out,graph.getEdge(id).getAllAttributes());
				}
			}else{
				//moze samo graph da bude
				if(params[1].equalsIgnoreCase("graph")){
				//ispis atributa grafa ako postoji u registru
					if (context.getGraph(params[2]) == null)
						throw new ExGCommandErrorException(
								CommandErrorType.WARNING, "Graph >" + params[2]
										+ "< not found!");
					printAtt(out,context.getGraph(params[2]).getAllAttributes());
				} else
					throw new ExGCommandErrorException(CommandErrorType.ERROR,"Ilegal arguments! Use help to see command syntax.");
			}
			
		} 
		return null;
	}

	/**
	 * Metoda za ispis atributa na konzoli. U sustini ispisuje prosledjenu mapu
	 * u obliku [ime_atributa] = vrednost
	 * 
	 * @param out
	 *            PrintStream na koji se ispisuju atributi
	 * @param map
	 *            Mapa atributa koji se ispisuju
	 */
	private void printAtt(PrintStream out, Map<String, Object> map) {
		Collection<Object> list = map.values();
		Set<String> keys = map.keySet();
		Iterator<Object> it = list.iterator();
		Iterator<String> keyIt = keys.iterator();
		while (it.hasNext() && keyIt.hasNext())
			out.println(" [" + keyIt.next() + "] =  " + it.next().toString());
	}

	@Override
	public String getHelp() {
		return "Prints a list of all attributes for the selected graph/node/edge\n"
				+ getSyntax();
	}

	@Override
	public String getKeyword() {
		return "att";
	}

	@Override
	public String getSyntax() {
		return 	"Use one of the folowing:\n" +
				"\t att - Graph or node must be selected to list its attributes \n" +
		 	    "\t att <graph_name> - When no graph is selected will list attributes of graph_name graph from the registry \n" +
		 		"\t att <node_name> - When graph is selected will list attributes of node_name node from the selected graph \n" +
		 		"\t att graph <graph_name> - Will list attributes of graph_name graph from the registry \n" +
		 		"\t att edge <edge_id> - Will list attributes of edge with edge_id id from the selected graph";
	}

	@Override
	public boolean returnsGraph() {
		return false;
	}

}
