package model;

import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;

import java.util.LinkedList;
import java.util.List;

public class Model {
	
	private static LinkedList<MyNode> nodes;
	private static LinkedList<MyEdge> edges;
	
	public Model()
	{
		nodes = new LinkedList<MyNode>();
		edges = new LinkedList<MyEdge>();
	}
	
	public Model(LinkedList<MyNode> nodes, LinkedList<MyEdge> edges)
	{
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public void addNode(MyNode node)
	{
		if (!isContained(node))
			nodes.add(node);
	}
	
	public void addEdge (MyEdge edge)
	{
		if (!isContained(edge))
			edges.add(edge);
	}
	
	public static LinkedList<MyNode> getAllNodes()
	{
		return nodes;
	}
	
	public static LinkedList<MyEdge> getAllEdges()
	{
		return edges;
	}
	
	public boolean isContained (MyNode node)
	{
		return nodes.contains(node);
	}
	
	public boolean isContained (MyEdge edge)
	{
		return edges.contains(edge);
	}
	
	public void MockData()
	{
		MyNode.setidcounter(0);
		
		
		LinkedList<String> data = new LinkedList<String>();
		
		data.add("in: credentials");
		data.add("out:boolean");
		MyNode fverify = new MyNode("verify", data, NodeType.FUNCTION);
		
		data = new LinkedList<String>();				
		data.add("in: valid");
		data.add("out: status");
		MyNode funlock = new MyNode("unlock bike", data, NodeType.FUNCTION);
		
		data = new LinkedList<String>();				
		data.add("in: HTTP");
		data.add("out: HTTP");
		MyNode eserver = new MyNode("backoffice server", data, NodeType.HARDWARE);
		
		data = new LinkedList<String>();				
		data.add("in: HTTP");
		data.add("out: HTTP, Signal");
		MyNode ecomputer = new MyNode("onboard computer", data, NodeType.HARDWARE);
		
		data = new LinkedList<String>();				
		data.add("in: Signal");
		data.add("out: Signal, DC 15V");
		MyNode econtroller = new MyNode("micro controller", data, NodeType.HARDWARE);
		
		data = new LinkedList<String>();				
		data.add("in: DC 15V");
		data.add("out: -");
		MyNode elock = new MyNode("electric lock", data, NodeType.HARDWARE);
		
		data = new LinkedList<String>();				
		data.add("in: DC 15V");
		data.add("out: -");
		MyNode testNode = new MyNode("test node", data, NodeType.DECISION);

		nodes.add(fverify);
		nodes.add(funlock);
		nodes.add(eserver);
		nodes.add(ecomputer);
		nodes.add(econtroller);
		nodes.add(elock);
		nodes.add(testNode);
		
		
		
		LinkedList<String> s = new LinkedList<String>();
		s.add("in: valid");
		
		edges.add(new MyEdge(ConnectionType.TRACE,s, fverify, funlock));
		
		edges.add(new MyEdge(ConnectionType.INCLUDES, fverify, eserver));
		edges.add(new MyEdge(ConnectionType.INCLUDES, fverify, ecomputer));
		edges.add(new MyEdge(ConnectionType.INCLUDES, funlock, ecomputer));
		edges.add(new MyEdge(ConnectionType.INCLUDES, funlock, econtroller));
		edges.add(new MyEdge(ConnectionType.INCLUDES, funlock, elock));
		
		edges.add(new MyEdge(ConnectionType.CONFLICTS, eserver, ecomputer));
		edges.add(new MyEdge(ConnectionType.EVOLVESTO, ecomputer,eserver));
		edges.add(new MyEdge(ConnectionType.PERFORMS, ecomputer, econtroller));
		edges.add(new MyEdge(ConnectionType.PRECONDITION,  econtroller,ecomputer));
		edges.add(new MyEdge(ConnectionType.GENERALIZES, econtroller, elock));
		edges.add(new MyEdge(ConnectionType.REALIZES, elock, econtroller));
		edges.add(new MyEdge(ConnectionType.INCLUDES, elock, testNode));
		
	}

}
