package gui.toolbars;

import java.util.ArrayList;
import java.util.LinkedList;

import de.tum.pssif.core.metamodel.external.MetamodelNode;
import graph.model.MyNode;
import model.ModelBuilder;

public class TreeNode {
	private LinkedList<TreeNode> leaves;
	private MetamodelNode content;
	
	public TreeNode(MetamodelNode node)
	{
		this.content = node;
		this.leaves = new LinkedList<TreeNode>();
	}
	
	public int getSize()
	{
		int c = 1;
		for (TreeNode tn : this.leaves)
			c += tn.getSize();
		return c;
	}
	
	public LinkedList<TreeNode> getLeaves() {
		return leaves;
	}
	public void setLeaves(LinkedList<TreeNode> leaves) {
		this.leaves = leaves;
	}
	public MetamodelNode getContent() {
		return content;
	}

	
	public void setContent(MetamodelNode content) {
		this.content = content;
	}
	public static ArrayList<MyNode> findInstances(String contentName){
		ArrayList<MyNode> instances = new ArrayList<MyNode>();
		LinkedList<MyNode> graphNodes = new LinkedList<MyNode>();
		graphNodes = ModelBuilder.getAllNodes() ;
	
		for (MyNode node: graphNodes){
			if(node.getNodeType().getName().equals(contentName))
				instances.add(node);
		}
		return instances;
	}
	
	
}
