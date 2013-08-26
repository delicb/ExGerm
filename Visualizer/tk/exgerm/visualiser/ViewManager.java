package tk.exgerm.visualiser;

import java.util.ArrayList;
import java.util.HashMap;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.visualiser.view.VisualiserView;

public class ViewManager {
	
	private ICoreContext context;
	
	private VisualiserView activeView;
	
	private HashMap<String, ArrayList < HashMap <String, VisualiserView> > > views;
	

	public ViewManager(ICoreContext _context){
		this.context = _context;
		this.views = new HashMap<String, ArrayList<HashMap<String, VisualiserView>>>();
	}
	
	public void setActiveView(String root, int level, String name){
		activeView = (views.get(root)).get(level).get(name);
		context.addData(IGraph.ACTIVE_GRAPH, activeView.getModel().getGraph());
	}
	
	public void setNullActiveView(){
		activeView = null;
		context.addData(IGraph.ACTIVE_GRAPH, null);
	}
	
	public VisualiserView getActiveView(){
		return this.activeView;
	}
	
	public void addRootView(String name, VisualiserView view){
		HashMap <String, VisualiserView> map = new HashMap<String, VisualiserView>();
		map.put(name, view);
		ArrayList<HashMap <String, VisualiserView>> list = new ArrayList<HashMap<String,VisualiserView>>();
		list.add(0, map);
		views.put(name, list);
	}
	
	public void renameRootView(String oldRoot, String newRoot){
		ArrayList < HashMap <String, VisualiserView> > tempList = new ArrayList<HashMap<String,VisualiserView>>();
		HashMap <String, VisualiserView> tempMap = new HashMap<String, VisualiserView>();

		tempList = views.get(oldRoot);
		VisualiserView v = tempList.get(0).get(oldRoot);
		tempMap.put(newRoot, v);
		tempList.remove(0);
		tempList.add(0, tempMap);
		
		views.put(newRoot, tempList);
		
		views.remove(oldRoot);
		
		getRootView(newRoot).setName(newRoot);
	}
	
	public void renameSubView(String oldName, String newName, String root, int level){
		HashMap <String, VisualiserView> tempMap = new HashMap<String, VisualiserView>();
		tempMap = (views.get(root)).get(level);
		VisualiserView v = tempMap.get(oldName);
		v.setName(newName);
		tempMap.put(newName, v);
		tempMap.remove(oldName);
		
		(views.get(root)).remove(level);
		(views.get(root)).add(level, tempMap);
	}
	
	public VisualiserView getRootView(String name){
		return (views.get(name)).get(0).get(name);
	}
	
	public void addSubView(String root, int level, String name, VisualiserView view){
		HashMap <String, VisualiserView> map;
		try{
			map = (views.get(root)).get(level);
		}catch(Exception ex){
			map = new HashMap<String, VisualiserView>();
		}
		map.put(name, view);
		(views.get(root)).add(level, map);
	}
	
	public VisualiserView getSubView(String root, int level, String name){
		return (views.get(root)).get(level).get(name);
	}
}
