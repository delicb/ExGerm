package tk.exgerm.graphtree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.graphtree.exGERMIconRenderer;

public class Graph implements TreeNode, exGERMModelIcon {

	/**
	 * INode koji ovaj Node predstavlja u GraphTree modelu.
	 */
	IGraph graph;
	/**
	 * NodeSeparator ovog Grapha, odnosno kontejner koji sadrži sve njegove 
	 * nodeove.
	 */
	NodeSeparator nodes;
	/**
	 * EdgeSeparator ovog Grapha, odnosno kontejner koji sadrži sve njegove 
	 * edgeove.
	 */
	EdgeSeparator edges;
	/**
	 * Ime Grapha odnosno ono šta će biti ispisano kao ime ovog elementa u 
	 * GraphTree-ju.
	 */
	String name;
	/**
	 * Korenski čvor GraphTree komponente, on sadrži u sebi listu svih grafova.
	 */
	Workspace parent;
	/**
	 * Ikonica kojom je Graph predstavljen u GraphTree komponenti.
	 */
	ImageIcon icon = new ImageIcon(
			exGERMIconRenderer.class
			.getResource("/tk/exgerm/graphtree/icons/graphTree.png"));
	
	public Graph(IGraph graph) {
		this.graph = graph;
		this.name = graph.getName();
		this.nodes = new NodeSeparator(this);
		this.edges = new EdgeSeparator(this);
		initializeNodes(graph);
		initializeEdges(graph);
	}

	public String toString(){
		return this.graph.getName();
	}
	
	public IGraph getGraph(){
		return graph;
	}
	
	public NodeSeparator getNodeSeparator(){
		return nodes;
	}
	
	public EdgeSeparator getEdgeSeparator(){
		return edges;
	}
	
	public void setParent(Workspace parent){
		this.parent = parent;
	}
	
	public SubGraph getSubGraph(IGraph graph){
		return this.nodes.getSubGraph(graph);
	}
	
	/**
	 * Metoda koja popunjava NodeSeparator kontejner sa svim nodovima 
	 * prosleđenog grafa.
	 * 
	 * @param graph - IGraph čiji nodovi trebaju učitati u ovaj Graph.
	 */
	public void initializeNodes(IGraph graph){
		ArrayList<INode> nodes = new ArrayList<INode>(graph.getAllNodes());
		for(INode n : nodes){
			if(n instanceof IGraph){
				getNodeSeparator().addNode(new SubGraph(n));
			}
			else if(n instanceof INode){
				getNodeSeparator().addNode(new Node(n));
			}
		}
	}
	
	/**
	 * Metoda koja popunjava EdgeSeparator kontejner sa svim edgeovima 
	 * prosleđenog grafa.
	 * 
	 * @param graph - IGraph čiji edgeovi trebaju učitati u ovaj Graph.
	 */
	public void initializeEdges(IGraph graph){
		ArrayList<IEdge> edges = new ArrayList<IEdge>(graph.getAllEdges());
		for(IEdge e : edges){
			getEdgeSeparator().addEdge(new Edge(e));
		}
	}
	
	@Override
	public Enumeration<Object> children() {
		ArrayList<Object> graphElements = new ArrayList<Object>();
		graphElements.add(this.nodes);
		graphElements.add(this.edges);
		return Collections.enumeration(graphElements);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {		
		switch(childIndex){
			case 0: return nodes;
			case 1: return edges;
			default: return null;
		}
	}

	@Override
	public int getChildCount() {
		return 2;
	}

	@Override
	public int getIndex(TreeNode node) {
		if(node instanceof NodeSeparator) return 0;
		else if(node instanceof EdgeSeparator) return 1;
		else return 0;
	}

	@Override
	public TreeNode getParent() {
		return parent;
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
