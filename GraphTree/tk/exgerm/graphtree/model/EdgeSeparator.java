package tk.exgerm.graphtree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class EdgeSeparator implements TreeNode, exGERMModelIcon {

	/**
	 * Lista svih Edgeva koji su sadržani u ovom EdgeSeparatoru.
	 */
	ArrayList<Edge> edges = new ArrayList<Edge>();
	/**
	 * Ime EdgeSeparatora odnosno ono šta će biti ispisano kao ime ovog elementa 
	 * u GraphTree-ju.
	 */
	String name;
	/**
	 * Roditelj ovog EdgeSeparatora odnosno Graph u kom se on nalazi.
	 */
	TreeNode parent;
	/**
	 * Ikonica kojom je EdgeSeparator predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon = new ImageIcon(
			exGERMIconRenderer.class
			.getResource("/tk/exgerm/graphtree/icons/edgeFolder.png"));
	
	public EdgeSeparator(TreeNode graph) {
		this.name = "Edges";
		this.parent = graph;
	}

	public String toString(){
		return this.name;
	}
	
	public void addEdge(Edge edge) {
		edge.setParent(this);
		edges.add(edge);
	}
	
	public void removeEdge(IEdge edge){
		int index = -1;
		for (int i = 0; i != edges.size(); i++) {
			if(edges.get(i).edge.getID() == edge.getID())
				index = i;
		}
		if(index != -1)
			edges.remove(index);
	}

	public Edge getEdge(int index) {
		return edges.get(index);
	}

	public int getEdgeIndex(Edge edge) {
		return edges.indexOf(edge);
	}
	
	public void setParent(Graph parent){
		this.parent = parent;
	}
	
	@Override
	public Enumeration<Edge> children() {
		return Collections.enumeration(this.edges);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.edges.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return this.edges.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return this.edges.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return this.getParent();
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
