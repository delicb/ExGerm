package tk.exgerm.graphstatis.statisticTools;

import java.util.ArrayList;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.graphstatis.StatisticTool;

public class SubgraphCount implements StatisticTool {

	private IGraph g;
	
	@Override
	public String getDescription() {
		return "Calculates the total number of subgraphs in a graph";
	}

	@Override
	public String getName() {
		return "subgraphs";
	}

	@Override
	public String getResult() {
		ArrayList<IGraph> subgraphs = new ArrayList<IGraph>();
		int sum = 0;
		boolean deeper = true;
		for(int i = 0; i != this.g.getNodeCount(); i++){
			if(this.g.getAllNodes().get(i) instanceof IGraph){
				sum++;
				subgraphs.add((IGraph)this.g.getAllNodes().get(i));
			}
		}
		if(subgraphs.size() != 0){
			while(deeper){
				ArrayList<IGraph> temp = new ArrayList<IGraph>();
				for(int i = 0; i != subgraphs.size(); i++){
					for(int j = 0; j != subgraphs.get(i).getAllNodes().size(); j++){
						if(subgraphs.get(i).getAllNodes().get(j) instanceof IGraph){
							sum++;
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
		return "Subrgraphs: " + sum;
	}

	@Override
	public void setGraph(IGraph graph) {
		this.g = graph;
	}

}
