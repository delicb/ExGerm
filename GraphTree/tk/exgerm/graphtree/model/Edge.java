package tk.exgerm.graphtree.model;

import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class Edge implements TreeNode, exGERMModelIcon {

	/**
	 * IEdge koji ovaj Edge predstavlja u GraphTree modelu.
	 */
	IEdge edge;
	/**
	 * Ime Edgea odnosno ono šta će biti ispisano kao ime ovog elementa u 
	 * GraphTree-ju.
	 */
	String name;
	/**
	 * Roditelj ovog Edgea odnosno EdgeSeparator u kom se Edge nalazi.
	 */
	EdgeSeparator parent;
	/**
	 * Ikonica kojom je Edge predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon;

	public Edge(IEdge edge){
		this.edge = edge;
		if(edge.isDirected()){
			this.name = 
				edge.getFrom().getName() + " -> " + edge.getTo().getName();
		}
		else 
			this.name = 
				edge.getFrom().getName() + " -- " + edge.getTo().getName();
	}
	
	/**
	 * Metoda za ispisivanje imena elementa u GraphTree-ju.
	 */
	public String toString(){
		if(edge.isDirected()){
			return edge.getFrom().getName() + " -> " + edge.getTo().getName();
		}
		else 
			return	edge.getFrom().getName() + " -- " + edge.getTo().getName();
	}
	
	public void setParent(EdgeSeparator parent){
		this.parent = parent;
	}
	
	public IEdge getEdge(){
		return edge;
	}
	
	@Override
	public Enumeration<Object> children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public ImageIcon getIcon() {
		if(this.edge.isDirected())
			icon = new ImageIcon(
				exGERMIconRenderer.class
				.getResource("/tk/exgerm/graphtree/icons/edge.png"));
		else 
			icon = new ImageIcon(
				exGERMIconRenderer.class
				.getResource("/tk/exgerm/graphtree/icons/edge.png"));
		return icon;
	}
}
