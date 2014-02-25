package graph.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import model.ModelBuilder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class GraphBuilder {
	
	private Graph<MyNode, MyEdge> g;
	//private boolean detailedNodes;
	private static boolean commentsVisible = false;
	
	public Graph<MyNode, MyEdge> createGraph(boolean detailedNodes)
	{
		//this.detailedNodes=detailedNodes;
		
		if (g==null)
			g = new SparseMultigraph<MyNode,MyEdge>();

		removeAllNodesAndEdges();
		
		buildGraphFromModel(detailedNodes);
		
		return g;
	}
	
	public Graph<MyNode, MyEdge> changeNodeDetails(boolean detailedNodes, Graph<MyNode, MyEdge> graph)
	{
		Collection<MyNode> nodes = graph.getVertices();
				
		for (MyNode n : nodes)
		{
			n.setDetailedOutput(detailedNodes);
		}

		return graph;
	}
	
	
	public Graph<MyNode, MyEdge> removeAllNodesAndEdges ()
	{
		return removeAllNodesAndEdges(g);
	}
	
	private Graph<MyNode, MyEdge> removeAllNodesAndEdges (Graph<MyNode, MyEdge> graph)
	{
		LinkedList<MyEdge> edges = new LinkedList<MyEdge>(graph.getEdges());
		
		for (MyEdge e : edges)
		{
			graph.removeEdge(e);
		}
		
		LinkedList<MyNode> nodes = new LinkedList<MyNode>(graph.getVertices());
		
		for (MyNode n : nodes)
		{
			graph.removeVertex(n);
		}
		
		return graph;
	}
	
	public Graph<MyNode, MyEdge> updateGraph (boolean detailedNodes)
	{
		this.removeAllNodesAndEdges();
		
		return buildGraphFromModel(detailedNodes);
	}
	
	private Graph<MyNode, MyEdge> buildGraphFromModel (boolean detailedNodes)
	{
		if (commentsVisible)
			System.out.println("buildGraphFromModel ");
		LinkedList<MyEdge> edges = ModelBuilder.getAllEdges();
		LinkedList<MyNode> nodes = ModelBuilder.getAllNodes();
		
		
		for (MyNode n : nodes)
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
		
		for (MyEdge e : edges)
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
