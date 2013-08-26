package tk.exgerm.graphstatis.statisticTools;

import java.util.ArrayList;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.graphstatis.StatisticTool;

public class GraphDepth implements StatisticTool {

	private IGraph graph;
	
	@Override
	public String getDescription() {
		return "Calculates the depth of the graph";
	}

	@Override
	public String getName() {
		return "depth";
	}

	@Override
	public String getResult() {
		ArrayList<IGraph> subgraphs = new ArrayList<IGraph>();
		int depth = 1;
		boolean deeper = true;
		for(int i = 0; i != this.graph.getNodeCount(); i++){
			if(this.graph.getAllNodes().get(i) instanceof IGraph){
				subgraphs.add((IGraph)this.graph.getAllNodes().get(i));
			}
		}
		if(subgraphs.size() != 0){
			while(deeper){
				depth++;
				ArrayList<IGraph> temp = new ArrayList<IGraph>();
				for(int i = 0; i != subgraphs.size(); i++){
					for(int j = 0; j != subgraphs.get(i).getAllNodes().size(); j++){
						if(subgraphs.get(i).getAllNodes().get(j) instanceof IGraph){
							temp.add((IGraph)subgraphs.get(i).getAllNodes().get(j));
						}
					}
				}
				if(temp.size() != 0){
					subgraphs.clear();
					for(int i = 0; i != temp.size(); i++)
						subgraphs.add(temp.get(i));
					temp.clear();
				}else
				deeper = false;
			}
		}
		return "Graph depth " + depth;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.graph = graph;
	}

}
