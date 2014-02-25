package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.Collection;
import java.util.LinkedList;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
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
	
	public Model model;
	public MutableMetamodel meta;
	private static MyNodeTypes nodeTypes;
	private static MyEdgeTypes edgeTypes;
	private static LinkedList<MyNode> nodes;
	private static LinkedList<MyEdge> edges;
	
	/**
	 * Initializes all the content
	 * @param meta
	 * @param model
	 */
	public ModelBuilder(MutableMetamodel meta, Model model)
	{
		this.model = model;
		this.meta = meta;
		
		nodes = new LinkedList<MyNode>();
		edges = new LinkedList<MyEdge>();
		
		createNodeTypes();
		createEdgeTypes();
		
		createNodes();
		createEdges();
		
	}
	
	/**
	 * Should not be used!! Only for test purposes
	 */
	public ModelBuilder()
	{
		mockData();
		
		nodes = new LinkedList<MyNode>();
		edges = new LinkedList<MyEdge>();
		
		createNodeTypes();
		createEdgeTypes();
		
		createNodes();
		createEdges();
		
	}
	
	private void createNodeTypes()
	{
		Collection<NodeType> types = this.meta.getNodeTypes();
		
		nodeTypes = new MyNodeTypes(types);
	}
	
	private void createEdgeTypes()
	{
		Collection<EdgeType> types = this.meta.getEdgeTypes();
		
		edgeTypes = new MyEdgeTypes(types);
	}
	
	private void createNodes()
	{
		for (MyNodeType t : nodeTypes.getAllNodeTypes())
		{
			PSSIFOption<Node> tempNodes = t.getType().apply(model,true);
			
			for (Node tempNode : tempNodes.getMany())
			{
				nodes.add(new MyNode(tempNode, t));
			}
			
		}
	}
	
	private void createEdges()
	{
		for (MyNode n: nodes)
		{
			createEdge(n.getNode());
		}
	}
	
	private void createEdge(Node sourceNode)
	{
		for (MyEdgeType t : edgeTypes.getAllEdgeTypes())
		{
			PSSIFOption<Edge> outgoingEdges = t.getType().getOutgoing().apply(sourceNode);
			
			for (Edge e : outgoingEdges.getMany())
			{
				PSSIFOption<Node> destinations = t.getType().getIncoming().apply(e);
				
				if (destinations.getMany().size()>1)
					throw new NullPointerException("Edge with more than one EndPoint???");
				
				Node destinationNode = destinations.getOne();
				
				MyEdge tmp;
				
				/*if (t.getType().getName().equals(MyEdgeTypes.CONTAINMENT))
				{
					// Their Edges are organized the other way. Don't be confused by the MyEdge2 call
					tmp = new MyEdge2(e, t, findNode(sourceNode), findNode(destinationNode));
				}
				else*/
					tmp = new MyEdge(e, t, findNode(destinationNode), findNode(sourceNode));
				
				edges.add(tmp);
			}
		}
		
	}
	
	public void addNode(MyNode node)
	{
		if (!isContained(node))
			nodes.add(node);
	}
	
	public void addEdge (MyEdge edge)
	{
		if (!isContained(edge))
			edges.add(edge);
	}
	
	public static  LinkedList<MyNode> getAllNodes()
	{
		return nodes;
	}
	
	public static LinkedList<MyEdge> getAllEdges()
	{
		return edges;
	}
	
	public boolean isContained (MyNode node)
	{
		return nodes.contains(node);
	}
	
	public boolean isContained (MyEdge edge)
	{
		return edges.contains(edge);
	}
	
	
	public static MyNode findNode (Node n)
	{
		for (MyNode current : nodes)
		{
			if (current.getNode().equals(n))
				return current;
		}
		
		return null;
	}
	
	public static MyEdge findEdge (Edge e)
	{
		for (MyEdge current : edges)
		{
			if (current.getEdge().equals(e))
				return current;
		}
		
		return null;
	}
	
	private void mockData ()
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
	    
	   /* for (int i =0;i<1000;i++)
	    {
	    	Node test = node("hardware", meta).create(model);
	    	node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(gpsApp, PSSIFOption.one(PSSIFValue.create("test"+i)));
	    	if ((i % 3) ==0)
	    		hw2hw.create(model, test, battery);
	    	if ((i % 3) ==1)
	    		hw2hw.create(model, test, smartphone);
	    	if ((i % 3) ==2)
	    		hw2hw.create(model, test, ebike);
	    	
	    }*/
	    
	    
	    
	    hw2hw.create(model, ebike, battery);
	    hw2hw.create(model, ebike, smartphone);
	    hw2sw.create(model, smartphone, rentalApp);
	    
	    EdgeType hwUses = node("hardware", meta).findOutgoingEdgeType("uses");
	    ConnectionMapping hw2swUses = hwUses.getMapping(node("hardware", meta), node("software", meta));
	    
	    hw2swUses.create(model, smartphone, gpsApp);
	  }
	
   private NodeType node(String name, MutableMetamodel metamodel) {
	   return metamodel.findNodeType(name);
   }

	public static MyNodeTypes getNodeTypes() {
		return nodeTypes;
	}

	public static MyEdgeTypes getEdgeTypes() {
		return edgeTypes;
	}
	
	public static void addCollapserEdge(MyEdge newEdge)
	{
		//MyEdge2 newEdge = new MyEdge2(edge, type, source, destination);
		newEdge.setCollapseEdge(true);
		edges.add(newEdge);
	}
	
	public static void removeCollapserEdge(MyEdge edge)
	{
		if (edge.isCollapseEdge())
			edges.remove(edge);
	}
	
	public static void printVisibleStuff ()
	{
		System.out.println("------visible Nodes----------");
		for (MyNode n : nodes)
		{
			if (n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
		System.out.println("------invisible Nodes----------");
		for (MyNode n : nodes)
		{
			if (!n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
	}
	
}
