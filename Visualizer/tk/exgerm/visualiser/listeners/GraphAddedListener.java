package tk.exgerm.visualiser.listeners;

import java.util.ArrayList;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.Activator;
import tk.exgerm.visualiser.ViewManager;
import tk.exgerm.visualiser.model.VisualiserModel;
import tk.exgerm.visualiser.view.VisualiserView;

public class GraphAddedListener implements IListener {

	private VisualiserView view;
	private VisualiserModel model;
	private ViewManager viewManager;
	private ICoreContext context;

	
	public GraphAddedListener(ViewManager _viewManager, ICoreContext _context){
		this.viewManager = _viewManager;
		this.context = _context;
	}
	
	@Override
	public void raise(String event, Object... parameters) {
		IGraph graph = (IGraph) parameters[0];
		
		String graphName = graph.getName();
		
		model = new VisualiserModel(context);
		view = new VisualiserView(model);
		model.setView(view);
		view.setName(graphName);
		view.getModel().setGraph(graph);
		
		viewManager.addRootView(graphName, view);
		
		if(Activator.visService.isNewActionGraph() == true){
			Activator.visService.addVisualiser(view);
			Activator.visService.setNewActionGraph(false);
			return;
		}
		
		graphCrawler((ArrayList<INode>) graph.getAllNodes(), (ArrayList<IEdge>) graph.getAllEdges());
	}
	
	public void graphCrawler(ArrayList<INode> nodes, ArrayList<IEdge> edges){
		VisualiserView view;
		try{
			view = viewManager.getSubView(nodes.get(0).getFinalRoot().getName(), nodes.get(0).getLevel()-1, nodes.get(0).getGraph().getName());
		}catch(Exception e){
			return;
		}
		
		for(INode node : nodes){
			if(node instanceof IGraph){
				view.getModel().addNode(node.getName(), true, node.getFinalRoot().getName(), node.getLevel());
				
				VisualiserModel m = new VisualiserModel(context);
				VisualiserView v = new VisualiserView(m);
				m.setView(v);
				v.setName(node.getName());
				v.setFinalRoot(node.getFinalRoot().getName());
				v.setLevel(node.getLevel());
				v.getModel().setGraph((IGraph) node);
				
				viewManager.addSubView(node.getFinalRoot().getName(), node.getLevel(), node.getName(), v);
				
				graphCrawler((ArrayList<INode>) ((IGraph)node).getAllNodes(), (ArrayList<IEdge>) ((IGraph)node).getAllEdges());
			}else{
				view.getModel().addNode(node.getName(), false, null, node.getLevel());
			}
		}

		for(IEdge e : edges){
			view.getModel().addEdge(e.getFrom().getName(), e.getTo().getName(), e.getID(), e.isDirected());
		}

	}
}
