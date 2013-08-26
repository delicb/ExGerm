package tk.exgerm.help.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class HelpNode implements TreeNode {

	private String url;
	private ArrayList<HelpNode> children;
	private HelpNode parent;
	private String name = "";
	
	public HelpNode(String name, HelpNode parent, String url){
		this.children = new ArrayList<HelpNode>();
		this.parent = parent;
		this.url = url;
		this.name = name;
	}
	
	public void addChild(HelpNode node){
		this.children.add(node);
	}
	
	public String toString(){
		return name;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public ArrayList<HelpNode> getChildren(){
		return this.children;
	}
	
	@Override
	public Enumeration<HelpNode> children() {
		return Collections.enumeration(this.children);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int arg0) {
		return this.children.get(arg0);
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public int getIndex(TreeNode arg0) {
		return 0;
	}
	
	public int getIndex(String text) {
		for (int i = 0; i != children.size(); i++) {
			if(children.get(i).equals(text))
				return i;
		}
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		if(children.size() != 0)
			return false;
		else return true;
	}

}
