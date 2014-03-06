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
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;

/**
 * Builds out of a Model and an MetaModel a Model which can be displayed as Graph and Matrix
 * @author Luc
 *
 */
public class ModelBuilder {
	
	private static MyModelContainer activeModel;
	private static LinkedList<MyModelContainer> activeModels;
	private static boolean mergerOn=true;
	private static Metamodel metaModel = PSSIFCanonicMetamodelCreator.create();

	/**
	 * Initializes all the content
	 * @param meta
	 * @param model
	 */
	/*public ModelBuilder()
	{
		
	}*/
	
	public static void addModel(Metamodel Pmeta, Model Pmodel)
	{
		if (mergerOn)
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
		else
		{
			if (activeModels ==null)
			{
				activeModels = new LinkedList<MyModelContainer>();
			}
			
			if (activeModels.size()>0)
			{
				ModelMerger merger = new ModelMerger();
				Model mergedModel = merger.mergeModels(activeModels.getFirst().getModel(), Pmodel, Pmeta);
				
				MyModelContainer newModel = new MyModelContainer(mergedModel, Pmeta);
				activeModels.remove();
				activeModels.add(newModel);
			}
			else
			{
				MyModelContainer newModel = new MyModelContainer(Pmodel, Pmeta);
				activeModels.add(newModel);
			}
			
		}
	}
	
	public static void resetModel()
	{
		activeModel = null;
		activeModels =null;
	}

	/**
	 * get all Nodes from the Model
	 * @return List with the Nodes
	 */
	public static LinkedList<MyNode> getAllNodes()
	{
		if (mergerOn)
		{
			if (activeModel !=null)
				return activeModel.getAllNodes();
			else
				return new LinkedList<MyNode>();
		}
		else
		{
			LinkedList<MyNode> tmp = new LinkedList<MyNode>();
			if (activeModels!=null)
			{
				
				for (MyModelContainer mc : activeModels)
				{
					tmp.addAll(mc.getAllNodes());
				}	
			}
			
			return tmp;
		}
	}
	
	/**
	 * get all Edges from the Model
	 * @return List with the Edges
	 */
	public static LinkedList<MyEdge> getAllEdges()
	{
		if (mergerOn)
		{
			if (activeModel !=null)
				return activeModel.getAllEdges();
			else
				return new LinkedList<MyEdge>();
		}
		else
		{
			LinkedList<MyEdge> tmp = new LinkedList<MyEdge>();
			if (activeModels!=null)
			{
				
				for (MyModelContainer mc : activeModels)
				{
					tmp.addAll(mc.getAllEdges());
				}	
			}
			
			return tmp;
		}
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
		if (mergerOn)
		{
			if (activeModel !=null)
				return activeModel.getNodeTypes();
			else
			{
				return new MyNodeTypes(new HashSet<MyNodeType>());
			}
		}
		else
		{
			if (activeModels !=null)
				return activeModels.getFirst().getNodeTypes();
			else
			{
				return new MyNodeTypes(new HashSet<MyNodeType>());
			}
		}
	}
	
	/**
	 * get all Edge Types from the Model
	 * @return the EdgeTypes object
	 */
	public static MyEdgeTypes getEdgeTypes() {
		if (mergerOn)
		{
			if (activeModel !=null)
				return activeModel.getEdgeTypes();
			else
			{
				return new MyEdgeTypes(new HashSet<MyEdgeType>());
			}
		}
		else
		{
			if (activeModels !=null)
				return activeModels.getFirst().getEdgeTypes();
			else
			{
				return new MyEdgeTypes(new HashSet<MyEdgeType>());
			}
		}
	}
	
	/**
	 * add an Edge which is added during a collapse operation
	 * @param newEdge the new Edge 
	 */
	public static void addCollapserEdge(MyEdge newEdge)
	{
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				newEdge.setCollapseEdge(true);
				activeModel.addEdge(newEdge);
			}
		}
		else
		{
			throw new NullPointerException("Not implemented");
		}
	}
	
	/**
	 * remove an Edge which was added during a collapse operation
	 * @param edge the edge in question
	 */
	public static void removeCollapserEdge(MyEdge edge)
	{
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				activeModel.removeCollapserEdge(edge);
			}
		}
		else
		{
			throw new NullPointerException("Not implemented");
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
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				activeModel.addNewNodeFromGUI(nodeName, type);
			}
		}
		else
		{
			throw new NullPointerException("Not implemented");
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
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				return activeModel.addNewEdgeGUI(source, destination, edgetype);
			}
			else
				return false;
		}
		else
		{
			throw new NullPointerException("Not implemented");
		}
	}
	
	public static Metamodel getMetamodel()
	{
		return metaModel;
	}
}
