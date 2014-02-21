package graph.operations;

import java.util.LinkedList;

import model.ModelBuilder;
import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;

public class NodeAndEdgeTypeFilter {
	
	private static LinkedList<MyNodeType> vizNodeTypes;
	private static LinkedList<MyEdgeType> vizEdgeTypes;
	
	public static void filter( LinkedList<MyNodeType> nodeTypes,  LinkedList<MyEdgeType> edgeTypes)
	{		
		vizNodeTypes = nodeTypes;
		vizEdgeTypes = edgeTypes;
		
		LinkedList<MyEdge2> edges = new LinkedList<MyEdge2>(ModelBuilder.getAllEdges());
		
		for (MyEdge2 e : edges)
		{
			if (!edgeTypes.contains(e.getEdgeType()))
				e.setVisible(false);
			else
				e.setVisible(true);
		}
		
		LinkedList<MyNode2> nodes = new LinkedList<MyNode2>(ModelBuilder.getAllNodes());
			
		for (MyNode2 n : nodes)
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
