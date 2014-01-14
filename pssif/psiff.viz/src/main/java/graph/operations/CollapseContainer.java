package graph.operations;



import graph.model2.MyNode2;

import java.util.LinkedList;

public class CollapseContainer {

	private LinkedList<InfoContainer> removeEdges;
	private LinkedList<MyNode2> addNodes;
	private LinkedList<InfoContainer> addEdges;
	
	public CollapseContainer()
	{
		this.addEdges = new LinkedList<InfoContainer>();
		this.addNodes = new LinkedList<MyNode2>();
		this.removeEdges = new LinkedList<InfoContainer>();
	}
	
	public void addNewEdges(InfoContainer edge)
	{
		this.removeEdges.add(edge);
	}
	
	public void addOldEdge (InfoContainer edge)
	{
		this.addEdges.add(edge);
	}
	
	public void addOldNode (MyNode2 node)
	{
		this.addNodes.add(node);
	}

	public LinkedList<InfoContainer> getNewEdges() {
		return removeEdges;
	}

	public LinkedList<MyNode2> getOldNodes() {
		return addNodes;
	}

	public LinkedList<InfoContainer> getOldEdges() {
		return addEdges;
	}
	
	
}
