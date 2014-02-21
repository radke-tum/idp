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
	//private boolean detailedNodes;
	private static boolean commentsVisible = false;
	
	public Graph<MyNode2, MyEdge2> createGraph(boolean detailedNodes)
	{
		//this.detailedNodes=detailedNodes;
		
		if (g==null)
			g = new SparseMultigraph<MyNode2,MyEdge2>();

		removeAllNodesAndEdges();
		
		buildGraphFromModel(detailedNodes);
		
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
		LinkedList<MyEdge2> edges = new LinkedList<MyEdge2>(graph.getEdges());
		
		for (MyEdge2 e : edges)
		{
			graph.removeEdge(e);
		}
		
		LinkedList<MyNode2> nodes = new LinkedList<MyNode2>(graph.getVertices());
		
		for (MyNode2 n : nodes)
		{
			graph.removeVertex(n);
		}
		
		return graph;
	}
	
	public Graph<MyNode2, MyEdge2> updateGraph (boolean detailedNodes)
	{
		this.removeAllNodesAndEdges();
		
		return buildGraphFromModel(detailedNodes);
	}
	
	private Graph<MyNode2, MyEdge2> buildGraphFromModel (boolean detailedNodes)
	{
		if (commentsVisible)
			System.out.println("buildGraphFromModel ");
		LinkedList<MyEdge2> edges = ModelBuilder.getAllEdges();
		LinkedList<MyNode2> nodes = ModelBuilder.getAllNodes();
		
		
		for (MyNode2 n : nodes)
		{
			if (n.isVisible())
			{
				n.setDetailedOutput(detailedNodes);
				g.addVertex(n);
				if (commentsVisible)
					System.out.println("Node is visible "+n.getRealName());
			}
			else
			{
				if (commentsVisible)
					System.out.println("Node not visible "+n.getRealName());
			}
		}
		
		for (MyEdge2 e : edges)
		{
			if (e.isVisible() && e.getDestinationNode().isVisible() && e.getSourceNode().isVisible())
			{
				if (commentsVisible)
				{
					System.out.println("Edge :"+ e.getEdgeInformations());
					System.out.println("Source "+e.getSourceNode().getRealName());
					System.out.println("Dest "+e.getDestinationNode().getRealName());
				}
				g.addEdge(e, e.getSourceNode(), e.getDestinationNode(), EdgeType.DIRECTED);
				if (commentsVisible)
					System.out.println("Edge-----------------");
			}
		}
		if (commentsVisible)
			System.out.println("----------------");
		
		return g;
	}
}
