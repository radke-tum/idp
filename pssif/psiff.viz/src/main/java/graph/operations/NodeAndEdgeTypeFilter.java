package graph.operations;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
	
	//private static LinkedList<MyNodeType> vizNodeTypes;
	//private static LinkedList<MyEdgeType> vizEdgeTypes;
	
	private static HashMap<String, LinkedList<MyNodeType>> mapViewToNodeType;
	private static HashMap<String, LinkedList<MyEdgeType>> mapViewToEdgeType;
	
	//private static LinkedList<String> activeViews;
	/**
	 * Apply the Node and Edge Type filter to the graph
	 * @param nodeTypes Node Types which should be displayed
	 * @param edgeTypes Edge Types which should be displayed
	 */
	public static void filter( LinkedList<MyNodeType> nodeTypes,  LinkedList<MyEdgeType> edgeTypes, String viewName)
	{		
		init();
		
		// Add active Filter
		//activeViews.add(viewName);
		//vizNodeTypes = nodeTypes;
		//vizEdgeTypes = edgeTypes;
		mapViewToEdgeType.put(viewName, edgeTypes);
		mapViewToNodeType.put(viewName, nodeTypes);
		
		calcVisibleNodesAndEdges();
		
		
	}
	
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
	
	public static void undoFilter(String viewName)
	{
		mapViewToEdgeType.remove(viewName);
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
		/*
		
		if (vizNodeTypes==null)
		{
			vizNodeTypes = ModelBuilder.getNodeTypes().getAllNodeTypes();
		}
		
		return vizNodeTypes;*/
	}
	
	/**
	 * Which Edge Types are currently visible in the graph
	 * @return a list with all the Edge Types which should be displayed
	 */
	public static LinkedList<MyEdgeType> getVisibleEdgeTypes()
	{
		/*if (vizEdgeTypes==null)
		{
			vizEdgeTypes = ModelBuilder.getEdgeTypes().getAllEdgeTypes();
		}
		
		return vizEdgeTypes;*/
		
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
	
	private static void init()
	{
		if (mapViewToEdgeType==null)
			mapViewToEdgeType = new HashMap<String, LinkedList<MyEdgeType>>();
		if (mapViewToNodeType==null)
			mapViewToNodeType = new HashMap<String, LinkedList<MyNodeType>>();
		/*if (activeViews==null)
			activeViews = new LinkedList<String>();*/
	}
}
