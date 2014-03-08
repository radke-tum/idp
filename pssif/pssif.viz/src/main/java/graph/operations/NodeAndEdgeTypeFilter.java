package graph.operations;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import model.ModelBuilder;

/**
 * Allows to filter the graph by certain Edge or Node Types
 * @author Luc
 *
 */
public class NodeAndEdgeTypeFilter {
	/**
	 * maps viewname to visible NodeTypes
	 */
	private static HashMap<String, LinkedList<MyNodeType>> mapViewToNodeType;
	/**
	 * maps viewname to visible EdgeTypes
	 */
	private static HashMap<String, LinkedList<MyEdgeType>> mapViewToEdgeType;
	
	/**
	 * Apply the Node and Edge Type filter to the graph
	 * @param nodeTypes Node Types which should be displayed
	 * @param edgeTypes Edge Types which should be displayed
	 */
	public static void filter( LinkedList<MyNodeType> nodeTypes,  LinkedList<MyEdgeType> edgeTypes, String viewName)
	{		
		init();
		
		mapViewToEdgeType.put(viewName, edgeTypes);
		mapViewToNodeType.put(viewName, nodeTypes);
		
		calcVisibleNodesAndEdges();
	}
	
	/**
	 * Check in the model which Nodes and Edges should be visible after applying/removing the Type filter
	 */
	private static void calcVisibleNodesAndEdges()
	{
		LinkedList<MyEdge> edges = new LinkedList<MyEdge>(ModelBuilder.getAllEdges());
		
		LinkedList<MyEdgeType> edgeTypes = getVisibleEdgeTypes();
		
		for (MyEdge e : edges)
		{
			if (!edgeTypes.contains(e.getEdgeType()))
				e.setVisible(false);
			else
				e.setVisible(true);
		}
		
		LinkedList<MyNode> nodes = new LinkedList<MyNode>(ModelBuilder.getAllNodes());
		
		LinkedList<MyNodeType> nodeTypes = getVisibleNodeTypes();
		
		for (MyNode n : nodes)
		{
			if (!nodeTypes.contains(n.getNodeType()))
				n.setVisible(false);
			else
				n.setVisible(true);
		}
	}
	
	/**
	 * Undo a Filter
	 * @param viewName the name of the filter
	 */
	public static void undoFilter(String viewName)
	{
		if (mapViewToEdgeType!=null)
			mapViewToEdgeType.remove(viewName);
		if (mapViewToNodeType!=null)
			mapViewToNodeType.remove(viewName);
		
		calcVisibleNodesAndEdges();
	}
	
	/**
	 * Which Node Types are currently visible in the graph
	 * @return a list with all the Nodes Types which should be displayed
	 */
	public static LinkedList<MyNodeType> getVisibleNodeTypes()
	{
		LinkedList<MyNodeType> res = new LinkedList<MyNodeType>();
		
		if (mapViewToNodeType!=null && mapViewToNodeType.size()!=0)
		{
			HashSet<MyNodeType> tmp = new HashSet<MyNodeType>();
			
			Collection<LinkedList<MyNodeType>> a= mapViewToNodeType.values();
			
			for (LinkedList<MyNodeType> list : a)
			{
				tmp.addAll(list);
			}
		
			res.addAll(Arrays.asList(tmp.toArray(new MyNodeType[0])));
		}
		else
		{
			res.addAll(ModelBuilder.getNodeTypes().getAllNodeTypes());
		}
			
		
		return res;
	}
	
	/**
	 * Which Edge Types are currently visible in the graph
	 * @return a list with all the Edge Types which should be displayed
	 */
	public static LinkedList<MyEdgeType> getVisibleEdgeTypes()
	{
		LinkedList<MyEdgeType> res = new LinkedList<MyEdgeType>();
		
		if (mapViewToEdgeType!=null && mapViewToEdgeType.size()!=0)
		{
			HashSet<MyEdgeType> tmp = new HashSet<MyEdgeType>();
			
			Collection<LinkedList<MyEdgeType>> a= mapViewToEdgeType.values();
			
			for (LinkedList<MyEdgeType> list : a)
			{
				tmp.addAll(list);
			}
		
			res.addAll(Arrays.asList(tmp.toArray(new MyEdgeType[0])));
		}
		else
		{
			res.addAll(ModelBuilder.getEdgeTypes().getAllEdgeTypes());
		}
			
		
		return res;
	}
	
	/**
	 * check if the HashMaps are initialized, if not initialize them
	 */
	private static void init()
	{
		if (mapViewToEdgeType==null)
			mapViewToEdgeType = new HashMap<String, LinkedList<MyEdgeType>>();
		if (mapViewToNodeType==null)
			mapViewToNodeType = new HashMap<String, LinkedList<MyNodeType>>();
	}
}
