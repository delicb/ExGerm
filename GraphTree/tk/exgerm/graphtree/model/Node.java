package tk.exgerm.graphtree.model;

import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.INode;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class Node implements TreeNode, exGERMModelIcon {

	/**
	 * INode koji ovaj Node predstavlja u GraphTree modelu.
	 */
	INode node;
	/**
	 * Ime Nodea odnosno ono šta će biti ispisano kao ime ovog elementa u 
	 * GraphTree-ju.
	 */
	String name;
	/**
	 * Roditelj ovog Nodea odnosno NodeSeparator u kom se Node nalazi.
	 */
	NodeSeparator parent;
	/**
	 * Ikonica kojom je Node predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon = new ImageIcon(
			exGERMIconRenderer.class
			.getResource("/tk/exgerm/graphtree/icons/node.png"));
	
	public Node(INode node){
		this.node = node;
		this.name = node.getName();
	}
	
	public String toString(){
		return this.node.getName();
	}
	
	public void setParent(NodeSeparator parent){
		this.parent = parent;
	}
	
	public INode getNode(){
		return node;
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
		return icon;
	}

}
