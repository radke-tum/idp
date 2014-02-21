package graph.operations;



import graph.model2.MyEdge2;
import graph.model2.MyNode2;

import java.util.LinkedList;

public class CollapseContainer {

	private LinkedList<MyEdge2> removeEdges;
	private LinkedList<MyNode2> addNodes;
	private LinkedList<MyEdge2> addEdges;
	
	public CollapseContainer()
	{
		this.addEdges = new LinkedList<MyEdge2>();
		this.addNodes = new LinkedList<MyNode2>();
		this.removeEdges = new LinkedList<MyEdge2>();
	}
	
	public void addNewEdges(MyEdge2 edge)
	{
		this.removeEdges.add(edge);
	}
	
	public void addOldEdge (MyEdge2 edge)
	{
		this.addEdges.add(edge);
	}
	
	public void addOldNode (MyNode2 node)
	{
		this.addNodes.add(node);
	}

	public LinkedList<MyEdge2> getNewEdges() {
		return removeEdges;
	}

	public LinkedList<MyNode2> getOldNodes() {
		return addNodes;
	}

	public LinkedList<MyEdge2> getOldEdges() {
		return addEdges;
	}
	
	
}
