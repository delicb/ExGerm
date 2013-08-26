package tk.exgerm.graphtree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class Workspace implements TreeNode, exGERMModelIcon {

	/**
	 * Ime korenskog čvora odnosno ono šta će biti ispisano kao ime ovog 
	 * elementa u GraphTree-ju.
	 */
	String name;
	/**
	 * Lista svih Graphova koji su sadržani u ovom Workspaceu.
	 */
	ArrayList<Graph> graphs = new ArrayList<Graph>();
	/**
	 * Ikonica kojom je Workspace predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon = new ImageIcon(
			exGERMIconRenderer.class
			.getResource("/tk/exgerm/graphtree/icons/graphTree.png"));;
	
	public Workspace(String name){
		this.name= name;
	}

	public String toString(){
		return this.name;
	}
	public void addGraph(Graph graph) {
		graph.setParent(this);
		graphs.add(graph);
	}
	
	public void removeGraph(IGraph graph) {
		int index = -1;
		for (int i = 0; i != graphs.size(); i++) {
			if(graphs.get(i).graph.getName().equals(graph.getName()))
				index = i;
		}
		if(index != -1)
			graphs.remove(index);
	}

	public Graph getGraph(int index) {
		return graphs.get(index);
	}
	
	public Graph getGraph(String name){
		for (Graph g : graphs) {
			if(g.getGraph().getName().equals(name))
				return g;
		}
		return null;
	}

	public int getGraphIndex(Graph graph) {
		return graphs.indexOf(graph);
	}
	
	@Override
	public Enumeration<Graph> children() {
		return Collections.enumeration(this.graphs);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.graphs.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return graphs.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return this.graphs.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public ImageIcon getIcon() {
		return icon;
	}

}
