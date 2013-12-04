package graph.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import model.Model;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class GraphBuilder {
	
	private Graph<MyNode, MyEdge> g;
	private boolean detailedNodes;
	
	public Graph<MyNode, MyEdge> createGraph(boolean detailedNodes)
	{
		//this.detailedNodes=detailedNodes;
		
		if (g==null)
			g = new SparseMultigraph<MyNode,MyEdge>();

		removeAllNodesAndEdges();
		
		MyNode.setidcounter(0);
		
		LinkedList<MyEdge> edges = Model.getAllEdges();
		LinkedList<MyNode> nodes = Model.getAllNodes();
		
		
		for (MyNode n : nodes)
		{
			n.setDetailedOutput(detailedNodes);
			g.addVertex(n);
		}
		
		for (MyEdge e : edges)
		{
			g.addEdge(e, e.getSourceNode(), e.getDestinationNode(), EdgeType.DIRECTED);
		}
		
		return g;
	}
	/*
	public Graph<MyNode, MyEdge> createGraph()
	{

				if (g==null)
					g = new SparseMultigraph<MyNode,MyEdge>();

				removeAllNodesAndEdges();
				
				MyNode.setidcounter(0);
				
				
				LinkedList<String> data = new LinkedList<>();
				
				data.add("in: credentials");
				data.add("out:boolean");
				MyNode fverify = new MyNode("verify", data, NodeType.FUNCTION);
				
				data = new LinkedList<>();				
				data.add("in: valid");
				data.add("out: status");
				MyNode funlock = new MyNode("unlock bike", data, NodeType.FUNCTION);
				
				data = new LinkedList<>();				
				data.add("in: HTTP");
				data.add("out: HTTP");
				MyNode eserver = new MyNode("backoffice server", data, NodeType.HARDWARE);
				
				data = new LinkedList<>();				
				data.add("in: HTTP");
				data.add("out: HTTP, Signal");
				MyNode ecomputer = new MyNode("onboard computer", data, NodeType.HARDWARE);
				
				data = new LinkedList<>();				
				data.add("in: Signal");
				data.add("out: Signal, DC 15V");
				MyNode econtroller = new MyNode("micro controller", data, NodeType.HARDWARE);
				
				data = new LinkedList<>();				
				data.add("in: DC 15V");
				data.add("out: -");
				MyNode elock = new MyNode("electric lock", data, NodeType.HARDWARE);
				
				data = new LinkedList<>();				
				data.add("in: DC 15V");
				data.add("out: -");
				MyNode testNode = new MyNode("test node", data, NodeType.DECISION);
				
				g.addVertex(fverify);
				g.addVertex(funlock);
				g.addVertex(eserver);
				g.addVertex(ecomputer);
				g.addVertex(econtroller);
				g.addVertex(elock);
				g.addVertex(testNode);
				
				LinkedList<String> s = new LinkedList<>();
				s.add("in: valid");

				g.addEdge(new MyEdge(ConnectionType.TRACE,s, fverify, funlock), fverify, funlock, EdgeType.DIRECTED);
				
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, fverify, eserver), fverify, eserver, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, fverify, ecomputer), fverify, ecomputer, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, funlock, ecomputer), funlock, ecomputer, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, funlock, econtroller), funlock, econtroller, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, funlock, elock), funlock, elock, EdgeType.DIRECTED);
				
				g.addEdge(new MyEdge(ConnectionType.CONFLICTS, eserver, ecomputer), eserver, ecomputer, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.EVOLVESTO, ecomputer,eserver), ecomputer,eserver, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.PERFORMS, ecomputer, econtroller), ecomputer, econtroller, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.PRECONDITION,  econtroller,ecomputer), econtroller,ecomputer, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.GENERALIZES, econtroller, elock), econtroller, elock, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.REALIZES, elock, econtroller), elock, econtroller, EdgeType.DIRECTED);
				g.addEdge(new MyEdge(ConnectionType.INCLUDES, elock, testNode), elock, testNode, EdgeType.DIRECTED);
				
				return g;
	}*/
	
	public Graph<MyNode, MyEdge> removeAllNodesAndEdges ()
	{
		Collection<MyEdge> edges =g.getEdges();
		LinkedList<MyEdge> edges2 = new LinkedList<>();
		
		Iterator<MyEdge> it1 = edges.iterator();
		while (it1.hasNext())
		{
			edges2.add(it1.next());
			
		}
		
		for (MyEdge e : edges2)
		{
			g.removeEdge(e);
		}
		
		
		Collection<MyNode> nodes =g.getVertices();
		LinkedList<MyNode> nodes2 = new LinkedList<>();
		
		Iterator<MyNode> it2 = nodes.iterator();
		while (it2.hasNext())
		{
			nodes2.add(it2.next());
			
		}
		
		for (MyNode n : nodes2)
		{
			g.removeVertex(n);
		}
		
		return g;
	}
}
