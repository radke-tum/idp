package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;


import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Units;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;

/**
 * Builds out of a Model and an MetaModel a Model which can be displayed as Graph and Matrix
 * @author Luc
 *
 */
public class ModelBuilder {
	
	private static LinkedList<MyModelContainer> activeModels;

	
	/**
	 * Initializes all the content
	 * @param meta
	 * @param model
	 */
	public ModelBuilder(Metamodel Pmeta, Model Pmodel)
	{
		if (activeModels==null)
		{
			activeModels = new LinkedList<MyModelContainer>();
		}
		
		MyModelContainer newModel = new MyModelContainer(Pmodel, Pmeta);
		activeModels.add(newModel);	
		
	}
	
	/**
	 * Should not be used!! Only for test purposes
	 */
	public ModelBuilder()
	{
		activeModels = new LinkedList<MyModelContainer>();
	}

	
	public static  LinkedList<MyNode> getAllNodes()
	{
		LinkedList<MyNode> res = new LinkedList<MyNode>();
		
		for (MyModelContainer mc : activeModels)
		{ 
			res.addAll(mc.getAllNodes());
		}
		return res;
	}
	
	public static LinkedList<MyEdge> getAllEdges()
	{
		LinkedList<MyEdge> res = new LinkedList<MyEdge>();
		
		for (MyModelContainer mc : activeModels)
		{ 
			res.addAll(mc.getAllEdges());
		}
		return res;
	}
	
	
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
	}
	
	public static MyNodeTypes getNodeTypes() {
		HashSet<MyNodeType> tmp = new HashSet<MyNodeType>();
		
		for (MyModelContainer mc : activeModels)
		{ 	
			tmp.addAll(mc.getNodeTypes().getAllNodeTypes());
		}
		
		MyNodeTypes nt = new MyNodeTypes(tmp);
		return nt;
	}

	public static MyEdgeTypes getEdgeTypes() {
		HashSet<MyEdgeType> tmp = new HashSet<MyEdgeType>();
		
		for (MyModelContainer mc : activeModels)
		{ 	
			tmp.addAll(mc.getEdgeTypes().getAllEdgeTypes());
		}
		
		MyEdgeTypes et = new MyEdgeTypes(tmp);
		return et;
	}
	
	public static void addCollapserEdge(MyEdge newEdge)
	{
		//TODO no very good implementation
		newEdge.setCollapseEdge(true);
		activeModels.getFirst().addEdge(newEdge);
	}
	
	public static void removeCollapserEdge(MyEdge edge)
	{
		//TODO no very good implementation
		activeModels.getFirst().removeCollapserEdge(edge);
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
	
	public static void addNewNodeFromGUI (String nodeName, MyNodeType type)
	{
		activeModels.getFirst().addNewNodeFromGUI(nodeName, type);
	}
	
	public static void removeNode (MyNode node)
	{
		throw new NullPointerException("Is not implemented in the core");
	}
	
	public static boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype)
	{
		return activeModels.getFirst().addNewEdgeGUI(source, destination, edgetype);
	}
}
