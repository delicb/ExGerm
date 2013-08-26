package tk.exgerm.graphtree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class NodeSeparator implements TreeNode, exGERMModelIcon {

	/**
	 * Lista svih Nodeova koji su sadržani u ovom NodeSeparatoru.
	 */
	ArrayList<Node> nodes = new ArrayList<Node>();
	/**
	 * Ime NodeSeparatora odnosno ono šta će biti ispisano kao ime ovog elementa 
	 * u GraphTree-ju.
	 */
	String name;
	/**
	 * Roditelj ovog NodeSeparatora odnosno Graph u kom se on nalazi.
	 */
	TreeNode parent;
	/**
	 * Ikonica kojom je NodeSeparator predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon = new ImageIcon(
			exGERMIconRenderer.class
			.getResource("/tk/exgerm/graphtree/icons/nodeFolder.png"));
	
	public NodeSeparator(TreeNode graph) {
		this.name = "Nodes";
		this.parent = graph;
	}
	
	public SubGraph getSubGraph(IGraph graph){
		for(Node n : nodes){
			if(n instanceof SubGraph && n.getNode().getName().equals(graph.getName())){
				return (SubGraph)n;
			}
		}
		return null;
	}

	public String toString(){
		return this.name;
	}
	
	public void addNode(Node node) {
		node.setParent(this);
		nodes.add(node);
	}
	
	public void removeNode(INode node){
		int index = -1;
		for (int i = 0; i != nodes.size(); i++) {
			if(nodes.get(i).node.getName().equals(node.getName()))
				index = i;
		}
		if(index != -1)
			nodes.remove(index);
	}

	public Node getNode(int index) {
		return nodes.get(index);
	}

	public int getNodeIndex(Node node) {
		return nodes.indexOf(node);
	}
	
	public void setParent(Graph parent){
		this.parent = parent;
	}
	
	@Override
	public Enumeration<Node> children() {
		return Collections.enumeration(this.nodes);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public Node getChildAt(int childIndex) {
		return this.nodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return this.nodes.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return this.nodes.indexOf(node);
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