package tk.exgerm.ucsearch;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.INode;

public class UCNode {
	
	private INode node;
	private IEdge edge;
	private double weight = 0;
	private UCNode parent;
	private String attribute;
	
	public UCNode(INode node, UCNode parent, IEdge edge, String attribute){
		this.attribute = attribute;
		this.node = node;
		this.edge = edge;
		this.parent = parent;
		double temp = 1;
		if(edge != null){
			Object pos = edge.getAttribute(this.attribute);
			if(pos != null){
				try{
					temp = Double.parseDouble((String)pos);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		if(parent != null)
			this.weight = parent.getWeight() + (Double)temp;
		else
			this.weight = (Double)temp;
	}
	
	public String toString(){
		return node.getName() + " " + weight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public INode getNode() {
		return node;
	}
	
	public IEdge getEdge() {
		return edge;
	}

	public UCNode getParent() {
		return parent;
	}
	
	

}
