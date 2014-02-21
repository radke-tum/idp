package graph.operations;

import java.util.LinkedList;

import model.ModelBuilder;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

public class NodeAndEdgeTypeFilter {
	
	private static LinkedList<MyNodeType> vizNodeTypes;
	private static LinkedList<MyEdgeType> vizEdgeTypes;
	
	public static void filter( LinkedList<MyNodeType> nodeTypes,  LinkedList<MyEdgeType> edgeTypes)
	{		
		vizNodeTypes = nodeTypes;
		vizEdgeTypes = edgeTypes;
		
		LinkedList<MyEdge> edges = new LinkedList<MyEdge>(ModelBuilder.getAllEdges());
		
		for (MyEdge e : edges)
		{
			if (!edgeTypes.contains(e.getEdgeType()))
				e.setVisible(false);
			else
				e.setVisible(true);
		}
		
		LinkedList<MyNode> nodes = new LinkedList<MyNode>(ModelBuilder.getAllNodes());
			
		for (MyNode n : nodes)
		{
			if (!nodeTypes.contains(n.getNodeType()))
				n.setVisible(false);
			else
				n.setVisible(true);
		}
	}
	
	public static LinkedList<MyNodeType> getVisibleNodeTypes()
	{
		if (vizNodeTypes==null)
		{
			vizNodeTypes = ModelBuilder.getNodeTypes().getAllNodeTypes();
		}
		
		return vizNodeTypes;
	}
	
	public static LinkedList<MyEdgeType> getVisibleEdgeTypes()
	{
		if (vizEdgeTypes==null)
		{
			vizEdgeTypes = ModelBuilder.getEdgeTypes().getAllEdgeTypes();
		}
		
		return vizEdgeTypes;
	}
}
