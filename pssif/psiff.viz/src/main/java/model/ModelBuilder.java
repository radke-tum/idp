package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;

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
	private static HashMap<MyPair, LinkedList<MyEdgeType>> possibleMappings;

	/**
	 * Add a new Model and MetaModel. The new Model might be merged with another existing Model
	 * @param meta
	 * @param model
	 */
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
	 * @param directed should the new edge be directed
	 * @return true if the add operation was successful, false otherwise
	 */
	public static boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype, boolean directed)
	{
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				return activeModel.addNewEdgeGUI(source, destination, edgetype, directed);
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
	
	public static Model getModel()
	{
		if (mergerOn)
		{
			if (activeModel !=null)
			{
				return activeModel.getModel();
			}
			else
				return null;
		}
		else
		{
			if (activeModels !=null)
			{
				if (activeModels.size()>0)
					return activeModels.getFirst().getModel();
				else
					return null;
			}
			else
				return null;
		}
	}
	
	
	private static void calcPossibleEdges()
	{
		if (possibleMappings== null)
		{
			possibleMappings = new HashMap<ModelBuilder.MyPair, LinkedList<MyEdgeType>>();
		}
		
		for (NodeType start :getMetamodel().getNodeTypes())
		{
			for (NodeType end :getMetamodel().getNodeTypes())
			{
				for (EdgeType et: getMetamodel().getEdgeTypes())
				{
					PSSIFOption<ConnectionMapping> tmp = et.getMapping(start, end);
					if (tmp!=null && tmp.isOne())
					{
						//ConnectionMapping mapping = tmp.getOne();
						MyPair p = new MyPair(start, end);
						LinkedList<MyEdgeType> data = possibleMappings.get(p);
						
						if (data ==null)
						{
							data = new LinkedList<MyEdgeType>();
						}
						MyEdgeType value = getEdgeTypes().getValue(et.getName());
						data.add(value);
						possibleMappings.put(p, data);
					}
					
				}
			}
		}
	}
	
	public static LinkedList<MyEdgeType> getPossibleEdges(NodeType start, NodeType end) 
	{
		if (possibleMappings== null)
			calcPossibleEdges();
		
		LinkedList<MyEdgeType> res = possibleMappings.get(new MyPair(start, end));
		
		if (res ==null)
		{
			res = new LinkedList<MyEdgeType>();
		}
		
		return res;
	}
	
	private static class MyPair
	{
		private NodeType start;
		private NodeType end;
		
		public MyPair (NodeType start, NodeType end)
		{
			this.start = start;
			this.end = end;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((end == null) ? 0 : end.getName().hashCode());
			result = prime * result + ((start == null) ? 0 : start.getName().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyPair other = (MyPair) obj;
			if (end == null) {
				if (other.end != null)
					return false;
			} else if (!end.equals(other.end))
				return false;
			if (start == null) {
				if (other.start != null)
					return false;
			} else if (!start.equals(other.start))
				return false;
			return true;
		}
	}
}
