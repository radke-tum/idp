package graph.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import model.ModelBuilder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model2.MyEdge2;
import graph.model2.MyNode2;

public class GraphBuilder {
	
	private Graph<MyNode2, MyEdge2> g;
	private boolean detailedNodes;
	
	public Graph<MyNode2, MyEdge2> createGraph(boolean detailedNodes)
	{
		//this.detailedNodes=detailedNodes;
		
		if (g==null)
			g = new SparseMultigraph<MyNode2,MyEdge2>();

		removeAllNodesAndEdges();
		
		MyNode.setidcounter(0);
		
		LinkedList<MyEdge2> edges = ModelBuilder.getAllEdges();
		LinkedList<MyNode2> nodes = ModelBuilder.getAllNodes();
		
		
		for (MyNode2 n : nodes)
		{
			n.setDetailedOutput(detailedNodes);
			g.addVertex(n);
		}
		
		for (MyEdge2 e : edges)
		{
			g.addEdge(e, e.getSourceNode(), e.getDestinationNode(), EdgeType.DIRECTED);
		}
		
		return g;
	}
	
	public Graph<MyNode2, MyEdge2> changeNodeDetails(boolean detailedNodes, Graph<MyNode2, MyEdge2> graph)
	{
		Collection<MyNode2> nodes = graph.getVertices();
				
		for (MyNode2 n : nodes)
		{
			n.setDetailedOutput(detailedNodes);
		}

		return graph;
	}
	
	
	public Graph<MyNode2, MyEdge2> removeAllNodesAndEdges ()
	{
		return removeAllNodesAndEdges(g);
	}
	
	private Graph<MyNode2, MyEdge2> removeAllNodesAndEdges (Graph<MyNode2, MyEdge2> graph)
	{
		Collection<MyEdge2> edges =graph.getEdges();
		LinkedList<MyEdge2> edges2 = new LinkedList<MyEdge2>();
		
		Iterator<MyEdge2> it1 = edges.iterator();
		while (it1.hasNext())
		{
			edges2.add(it1.next());
			
		}
		
		for (MyEdge2 e : edges2)
		{
			graph.removeEdge(e);
		}
		
		
		Collection<MyNode2> nodes =graph.getVertices();
		LinkedList<MyNode2> nodes2 = new LinkedList<MyNode2>();
		
		Iterator<MyNode2> it2 = nodes.iterator();
		while (it2.hasNext())
		{
			nodes2.add(it2.next());
			
		}
		
		for (MyNode2 n : nodes2)
		{
			graph.removeVertex(n);
		}
		
		return graph;
	}
	
}
