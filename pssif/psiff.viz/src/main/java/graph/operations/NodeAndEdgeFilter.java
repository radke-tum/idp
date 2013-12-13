package graph.operations;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;




import model.Model;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;

public class NodeAndEdgeFilter {

	public static Graph<MyNode, MyEdge> filter(Graph<MyNode, MyEdge> graph, LinkedList<NodeType> nodeTypes,  LinkedList<ConnectionType> edgeTypes)
	{		
		LinkedList<MyNode> existingNodes = new LinkedList<MyNode>();
		LinkedList<MyEdge> existingEdges = new LinkedList<MyEdge>();
		
		Collection<MyEdge> edges = graph.getEdges();		
		LinkedList<MyEdge> edges2 = new LinkedList<MyEdge>();
		
		Iterator<MyEdge> it1 = edges.iterator();
		while (it1.hasNext())
		{
			edges2.add(it1.next());
			
		}
		
		for (MyEdge e : edges2)
		{
			if (!edgeTypes.contains(e.getConnectionType()))
			{
				graph.removeEdge(e);
			}
			else
				existingEdges.add(e);
		}
		
		
		Collection<MyNode> nodes = graph.getVertices();
		LinkedList<MyNode> nodes2 = new LinkedList<MyNode>();
		
		Iterator<MyNode> it2 = nodes.iterator();
		while (it2.hasNext())
		{
			nodes2.add(it2.next());
			
		}
		
		for (MyNode n : nodes2)
		{
			if (!nodeTypes.contains(n.getNodeType()))
			{
				graph.removeVertex(n);
			}
			else
				existingNodes.add(n);
		}
		
		LinkedList<MyNode> modelNodes = Model.getAllNodes();
		for (MyNode n : modelNodes)
		{
			if (nodeTypes.contains(n.getNodeType()) && !existingNodes.contains(n))
			{
				graph.addVertex(n);
			}
		}
		
		LinkedList<MyEdge> modelEdges = Model.getAllEdges();
		for (MyEdge e : modelEdges)
		{
			if (edgeTypes.contains(e.getConnectionType()) && !existingEdges.contains(e))
			{
				graph.addEdge(e, e.getSourceNode(), e.getDestinationNode(),EdgeType.DIRECTED);
			}
		}
		
		return graph;
	}
}
