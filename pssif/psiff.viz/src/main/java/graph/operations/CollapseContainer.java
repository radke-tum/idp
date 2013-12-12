package graph.operations;

import graph.model.MyNode;

import java.util.LinkedList;

public class CollapseContainer {

	private LinkedList<InfoContainer> removeEdges;
	private LinkedList<MyNode> addNodes;
	private LinkedList<InfoContainer> addEdges;
	
	public CollapseContainer()
	{
		this.addEdges = new LinkedList<InfoContainer>();
		this.addNodes = new LinkedList<MyNode>();
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
	
	public void addOldNode (MyNode node)
	{
		this.addNodes.add(node);
	}

	public LinkedList<InfoContainer> getNewEdges() {
		return removeEdges;
	}

	public LinkedList<MyNode> getOldNodes() {
		return addNodes;
	}

	public LinkedList<InfoContainer> getOldEdges() {
		return addEdges;
	}
	
	
}
