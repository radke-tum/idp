package graph.operations;

import java.util.LinkedList;

import model.ModelBuilder;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

/**
 * Allows to filter the graph by certain Edge or Node Types
 * @author Luc
 *
 */
public class NodeAndEdgeTypeFilter {
	
	private static LinkedList<MyNodeType> vizNodeTypes;
	private static LinkedList<MyEdgeType> vizEdgeTypes;
	
	/**
	 * Apply the Node and Edge Type filter to the graph
	 * @param nodeTypes Node Types which should be displayed
	 * @param edgeTypes Edge Types which should be displayed
	 */
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
	
	/**
	 * Which Node Types are currently visible in the graph
	 * @return a list with all the Nodes Types which should be displayed
	 */
	public static LinkedList<MyNodeType> getVisibleNodeTypes()
	{
		if (vizNodeTypes==null)
		{
			vizNodeTypes = ModelBuilder.getNodeTypes().getAllNodeTypes();
		}
		
		return vizNodeTypes;
	}
	
	/**
	 * Which Edge Types are currently visible in the graph
	 * @return a list with all the Edge Types which should be displayed
	 */
	public static LinkedList<MyEdgeType> getVisibleEdgeTypes()
	{
		if (vizEdgeTypes==null)
		{
			vizEdgeTypes = ModelBuilder.getEdgeTypes().getAllEdgeTypes();
		}
		
		return vizEdgeTypes;
	}
}
