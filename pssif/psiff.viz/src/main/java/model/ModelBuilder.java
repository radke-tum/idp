package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.HashSet;
import java.util.LinkedList;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;

/**
 * Builds out of a Model and an MetaModel a Model which can be displayed as Graph and Matrix
 * @author Luc
 *
 */
public class ModelBuilder {
	
	private static MyModelContainer activeModel;

	/**
	 * Initializes all the content
	 * @param meta
	 * @param model
	 */
	public ModelBuilder(Metamodel Pmeta, Model Pmodel)
	{
		// check if we have to merge the model with an existing
		if (activeModel==null)
		{
			MyModelContainer newModel = new MyModelContainer(Pmodel, Pmeta);
			activeModel =newModel;
		}
		else
		{
			ModelMerger merger = new ModelMerger();
			Model mergedModel = merger.mergeModels(activeModel.getModel(), Pmodel, Pmeta);
			
			MyModelContainer newModel = new MyModelContainer(mergedModel, Pmeta);
			activeModel =newModel;
		}
	}
	
	/**
	 * Should not be used!! Only for test purposes
	 */
	public ModelBuilder()
	{
		activeModel = null;
	}

	/**
	 * get all Nodes from the Model
	 * @return List with the Nodes
	 */
	public static LinkedList<MyNode> getAllNodes()
	{
		if (activeModel !=null)
			return activeModel.getAllNodes();
		else
			return new LinkedList<MyNode>();
	}
	
	/**
	 * get all Edges from the Model
	 * @return List with the Edges
	 */
	public static LinkedList<MyEdge> getAllEdges()
	{
		if (activeModel !=null)
			return activeModel.getAllEdges();
		else
			return new LinkedList<MyEdge>();
	}
	
/*
	public static MyNode findNode (MyNode n)
	{
		for (MyNode current : getAllNodes())
		{
			if (current.equals(n))
				return current;
		}
		
		return null;
	}
	
	public static MyEdge findEdge (MyEdge e)
	{
		for (MyEdge current : getAllEdges())
		{
			if (current.equals(e))
				return current;
		}
		
		return null;
	}*/
	
	/**
	 * get all Node Types from the Model
	 * @return the NodeTypes object
	 */
	public static MyNodeTypes getNodeTypes() {
		if (activeModel !=null)
			return activeModel.getNodeTypes();
		else
		{
			return new MyNodeTypes(new HashSet<MyNodeType>());
		}
	}
	
	/**
	 * get all Edge Types from the Model
	 * @return the EdgeTypes object
	 */
	public static MyEdgeTypes getEdgeTypes() {
		if (activeModel !=null)
			return activeModel.getEdgeTypes();
		else
		{
			return new MyEdgeTypes(new HashSet<MyEdgeType>());
		}
	}
	
	/**
	 * add an Edge which is added during a collapse operation
	 * @param newEdge the new Edge 
	 */
	public static void addCollapserEdge(MyEdge newEdge)
	{
		if (activeModel !=null)
		{
			newEdge.setCollapseEdge(true);
			activeModel.addEdge(newEdge);
		}
	}
	
	/**
	 * remove an Edge which was added during a collapse operation
	 * @param edge the edge in question
	 */
	public static void removeCollapserEdge(MyEdge edge)
	{
		if (activeModel !=null)
		{
			activeModel.removeCollapserEdge(edge);
		}
	}
	
	public static void printVisibleStuff ()
	{
		System.out.println("------visible Nodes----------");
		for (MyNode n : getAllNodes())
		{
			if (n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
		System.out.println("------invisible Nodes----------");
		for (MyNode n : getAllNodes())
		{
			if (!n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
	}
	
	/**
	 * Add a new Node which was created through the Gui
	 * @param nodeName The name of the new Node
	 * @param type The type of the new Node
	 */
	public static void addNewNodeFromGUI (String nodeName, MyNodeType type)
	{
		if (activeModel !=null)
		{
			activeModel.addNewNodeFromGUI(nodeName, type);
		}
	}
	
	/**
	 * Add a new Edge which was created through the Gui
	 * @param source The start Node of the Edge
	 * @param destination The destination Node of the Edge
	 * @param edgetype The type of the Edge
	 * @return true if the add operation was successful, false otherwise
	 */
	public static boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype)
	{
		if (activeModel !=null)
		{
			return activeModel.addNewEdgeGUI(source, destination, edgetype);
		}
		else
			return false;
	}
}
