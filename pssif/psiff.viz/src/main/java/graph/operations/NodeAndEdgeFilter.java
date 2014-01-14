package graph.operations;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import model.ModelBuilder;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;

public class NodeAndEdgeFilter {

	public static Graph<MyNode2, MyEdge2> filter(Graph<MyNode2, MyEdge2> graph, LinkedList<MyNodeType> nodeTypes,  LinkedList<MyEdgeType> edgeTypes)
	{		
		LinkedList<MyNode2> existingNodes = new LinkedList<MyNode2>();
		LinkedList<MyEdge2> existingEdges = new LinkedList<MyEdge2>();
			
		LinkedList<MyEdge2> edges = new LinkedList<MyEdge2>(graph.getEdges());
		
		for (MyEdge2 e : edges)
		{
			if (!edgeTypes.contains(e.getEdgeType()))
			{
				graph.removeEdge(e);
			}
			else
				existingEdges.add(e);
		}
		
		

		LinkedList<MyNode2> nodes = new LinkedList<MyNode2>(graph.getVertices());
		
		
		for (MyNode2 n : nodes)
		{
			if (!nodeTypes.contains(n.getNodeType()))
			{
				graph.removeVertex(n);
			}
			else
				existingNodes.add(n);
		}
		
		LinkedList<MyNode2> modelNodes = ModelBuilder.getAllNodes();
		for (MyNode2 n : modelNodes)
		{
			if (nodeTypes.contains(n.getNodeType()) && !existingNodes.contains(n))
			{
				graph.addVertex(n);
			}
		}
		
		LinkedList<MyEdge2> modelEdges = ModelBuilder.getAllEdges();
		for (MyEdge2 e : modelEdges)
		{
			if (edgeTypes.contains(e.getEdgeType()) && !existingEdges.contains(e))
			{
				graph.addEdge(e, e.getSourceNode(), e.getDestinationNode(),EdgeType.DIRECTED);
			}
		}
		
		return graph;
	}
}
