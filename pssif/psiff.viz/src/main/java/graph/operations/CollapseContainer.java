package graph.operations;



import graph.model.MyEdge;
import graph.model.MyNode;

import java.util.LinkedList;

public class CollapseContainer {

	private LinkedList<MyEdge> removeEdges;
	private LinkedList<MyNode> addNodes;
	private LinkedList<MyEdge> addEdges;
	
	public CollapseContainer()
	{
		this.addEdges = new LinkedList<MyEdge>();
		this.addNodes = new LinkedList<MyNode>();
		this.removeEdges = new LinkedList<MyEdge>();
	}
	
	public void addNewEdges(MyEdge edge)
	{
		this.removeEdges.add(edge);
	}
	
	public void addOldEdge (MyEdge edge)
	{
		this.addEdges.add(edge);
	}
	
	public void addOldNode (MyNode node)
	{
		this.addNodes.add(node);
	}

	public LinkedList<MyEdge> getNewEdges() {
		return removeEdges;
	}

	public LinkedList<MyNode> getOldNodes() {
		return addNodes;
	}

	public LinkedList<MyEdge> getOldEdges() {
		return addEdges;
	}
	
	
}
