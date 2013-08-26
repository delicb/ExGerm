package tk.exgerm.ucsearch;

import java.util.ArrayList;
import java.util.PriorityQueue;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.INode;

public class UCSearch {
	
	private INode start;
	private INode finish;
	public String attribute;
	private PriorityQueue<UCNode> queue = new PriorityQueue<UCNode>(1000000,new UCComparator());
	
	public UCSearch(String att, INode start, INode finish){
		if(att != null)
			this.attribute = att;
		else
			this.attribute = "Weight";
		this.start = start;
		this.finish = finish;
	}
	
	public ArrayList<Object> search(){
		
		if(this.start.equals(this.finish)){
			return null;
		}
		
		expandStartNode(this.start);
		
		UCNode nextNode = queue.poll();
				
		if(nextNode != null){
			if(nextNode.getNode().equals(this.finish)) return returnArrayRes(nextNode);
			else{
				expandNode(nextNode);
				while(queue.peek() != null){
					nextNode = queue.poll();
					if(nextNode.getNode().equals(this.finish)) break;
					expandNode(nextNode);
				}
			}
		}
		
		return returnArrayRes(nextNode);
		
	}
	
	public void expandNode(UCNode node){
		
		ArrayList<IEdge> edges = (ArrayList<IEdge>)node.getNode().getConnectedEdges();
		for(IEdge e : edges){
			if(!(e.getFrom().equals(e.getTo()))){
				if((e.isDirected() && node.getNode().equals(e.getFrom()))){				
					queue.add(new UCNode(e.getTo(),node,e,attribute));
				}else if(!e.isDirected()){
					if(e.getFrom().equals(node.getNode()))
						queue.add(new UCNode(e.getTo(),node,e,attribute));
					else
						queue.add(new UCNode(e.getFrom(),node,e,attribute));
				}
			}
		}
	}
	
	public void expandStartNode(INode start){
		
		UCNode startNode = new UCNode(start,null,null,attribute);
		
		ArrayList<IEdge> edges = (ArrayList<IEdge>)start.getConnectedEdges();
		
		for(IEdge e : edges){
			if(!(e.getFrom().equals(e.getTo()))){
				if(e.isDirected() && start.equals(e.getFrom())){			
					queue.add(new UCNode(e.getTo(),startNode,e,attribute));
				}else if(!e.isDirected()){
					if(e.getFrom().equals(start))
						queue.add(new UCNode(e.getTo(),startNode,e,attribute));
					else
						queue.add(new UCNode(e.getFrom(),startNode,e,attribute));
				}
			}
		}		
	}
	
	public ArrayList<Object> returnArrayRes(UCNode node){
		ArrayList<Object> invertResult = new ArrayList<Object>();
		while(node.getParent() != null){
			invertResult.add(node.getNode());
			invertResult.add(node.getEdge());
			node = node.getParent();
		}
		invertResult.add(node.getNode());
		ArrayList<Object> result = new ArrayList<Object>();
		for(int i = invertResult.size() -1; i != -1; i--){
			result.add(invertResult.get(i));
		}
		return result;
	}

}
