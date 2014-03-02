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
	
	/*public static Model model;
	public static MutableMetamodel meta;
	private static MyNodeTypes nodeTypes;
	private static MyEdgeTypes edgeTypes;
	private static LinkedList<MyNode> nodes;
	private static LinkedList<MyEdge> edges;
	*/
	private static LinkedList<MyModelContainer> activeModels;
	/*private static HashMap<MyNode, MyModelContainer> mappingNodeModel;
	private static HashMap<MyEdge, MyModelContainer> mappingEdgeModel;*/
	
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
			/*mappingEdgeModel = new HashMap<MyEdge, MyModelContainer>();
			mappingNodeModel = new HashMap<MyNode, MyModelContainer>();*/
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
		/*mappingEdgeModel = new HashMap<MyEdge, MyModelContainer>();
		mappingNodeModel = new HashMap<MyNode, MyModelContainer>();*/
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
	
/*	private void mockData ()
	{
		meta = new MetamodelImpl();
	    NodeType development = meta.createNodeType("development artifact");

	    NodeType solution = meta.createNodeType("solution artifact");
	    solution.inherit(development);
	    NodeType hardware = meta.createNodeType("hardware");
	    hardware.inherit(solution);
	    NodeType software = meta.createNodeType("software");
	    software.inherit(solution);
	    NodeType usecase = meta.createNodeType("usecase");
	    usecase.inherit(development);
	    NodeType requirement = meta.createNodeType("requirement");
	    requirement.inherit(development);

	    EdgeType containment = meta.createEdgeType("containment");
	    containment.createMapping("contains", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
	        "contained in", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
	    containment.createMapping("contains", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
	        "contained in", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
	    containment.createMapping("contains", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
	        "contained in", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
	    
	    EdgeType uses = meta.createEdgeType("uses");
	    uses.createMapping("uses", hardware, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 0, UnlimitedNatural.UNLIMITED),
		        "used by", software, MultiplicityContainer.of(1, UnlimitedNatural.of(1), 1, UnlimitedNatural.of(1)));
	    
	    node("hardware", meta).createAttribute(node("hardware", meta).getDefaultAttributeGroup(), "testattr", PrimitiveDataType.INTEGER, Units.NONE, true, AttributeCategory.METADATA);
	    node("hardware", meta).createAttribute(node("hardware", meta).getDefaultAttributeGroup(), "testattr2", PrimitiveDataType.BOOLEAN, Units.NONE, true, AttributeCategory.METADATA);
	    node("hardware", meta).createAttribute(node("hardware", meta).getDefaultAttributeGroup(), "testattr3", PrimitiveDataType.DECIMAL, Units.KILOGRAM, true, AttributeCategory.METADATA);
	    
	    
	    model = new ModelImpl();
	    
	    Node ebike = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute("testattr").set(ebike, PSSIFOption.one(PSSIFValue.create(5)));
	    node("hardware", meta).findAttribute("testattr2").set(ebike, PSSIFOption.one(PSSIFValue.create(true)));
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(ebike, PSSIFOption.one(PSSIFValue.create("Ebike")));
	    Node smartphone = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute("testattr2").set(smartphone, PSSIFOption.one(PSSIFValue.create(true)));
	    node("hardware", meta).findAttribute("testattr").set(smartphone, PSSIFOption.one(PSSIFValue.create(10)));
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(smartphone, PSSIFOption.one(PSSIFValue.create("Smartphone")));
	    Node battery = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute("testattr2").set(battery, PSSIFOption.one(PSSIFValue.create(false)));
	    node("hardware", meta).findAttribute("testattr").set(battery, PSSIFOption.one(PSSIFValue.create(15)));
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(battery, PSSIFOption.one(PSSIFValue.create("Battery")));
	    Node rentalApp = node("software", meta).create(model);
	    node("software", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(rentalApp, PSSIFOption.one(PSSIFValue.create("RentalApp")));
	    Node gpsApp = node("software", meta).create(model);
	    node("software", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(gpsApp, PSSIFOption.one(PSSIFValue.create("GpsApp")));
	   // node("hardware", meta).createAttribute(group, name, dataType, visible, category)
	    
	    EdgeType hwContainment = node("hardware", meta).findOutgoingEdgeType("containment");
	    ConnectionMapping hw2hw = hwContainment.getMapping(node("hardware", meta), node("hardware", meta));
	    ConnectionMapping hw2sw = hwContainment.getMapping(node("hardware", meta), node("software", meta));
	    

	    
	    
	    
	    hw2hw.create(model, ebike, battery);
	    hw2hw.create(model, ebike, smartphone);
	    hw2sw.create(model, smartphone, rentalApp);
	    
	    EdgeType hwUses = node("hardware", meta).findOutgoingEdgeType("uses");
	    ConnectionMapping hw2swUses = hwUses.getMapping(node("hardware", meta), node("software", meta));
	    
	    hw2swUses.create(model, smartphone, gpsApp);
	  }
	
   private static NodeType node(String name, MutableMetamodel metamodel) {
	   return metamodel.findNodeType(name);
   }*/

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
		//MyEdge2 newEdge = new MyEdge2(edge, type, source, destination);
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
		/*Node newNode = node(type.getName(), meta).create(model);
		
		node(type.getName(), meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(newNode, PSSIFOption.one(PSSIFValue.create(nodeName)));
		
		nodes.add(new MyNode(newNode, type));*/
		activeModels.getFirst().addNewNodeFromGUI(nodeName, type);
	}
	
	public static void removeNode (MyNode node)
	{
		//node.getNode().apply(new Disco);
		
		//Node newNode = node(type.getName(), meta)
		
		//node(type.getName(), meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(newNode, PSSIFOption.one(PSSIFValue.create(nodeName)));
		
		//newNode.apply();
		//node.getNode().apply(new DisconnectOperation(null, null, null));
	}
	
	public static boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype)
	{
		/*ConnectionMapping mapping = edgetype.getType().getMapping(source.getNodeType().getType(), destination.getNodeType().getType());
		
		if (mapping!=null)
		{
			Edge newEdge = mapping.create(model, source.getNode(), destination.getNode());
				
			MyEdge e  = new MyEdge(newEdge, edgetype, source, destination);
			
			edges.add(e);
			return true;
		}
		else
			return false;*/
		return activeModels.getFirst().addNewEdgeGUI(source, destination, edgetype);
		
	}
}
