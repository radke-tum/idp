package model;

import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyEdgeTypes;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;
import graph.model2.MyNodeTypes;

import java.util.Collection;
import java.util.LinkedList;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.Units;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.MetamodelImpl;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;

public class ModelBuilder {
	
	public Model model;
	public MutableMetamodel meta;
	private static MyNodeTypes nodeTypes;
	private static MyEdgeTypes edgeTypes;
	private static LinkedList<MyNode2> nodes;
	private static LinkedList<MyEdge2> edges;
	
	
	public ModelBuilder(MutableMetamodel meta, Model model)
	{
		this.model = model;
		this.meta = meta;
		
		nodes = new LinkedList<MyNode2>();
		edges = new LinkedList<MyEdge2>();
		
		createNodeTypes();
		createEdgeTypes();
		
		createNodes();
		createEdges();
		
	}
	
	public ModelBuilder()
	{
		mockData();
		
		nodes = new LinkedList<MyNode2>();
		edges = new LinkedList<MyEdge2>();
		
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
			PSSIFOption<Node> tempNodes = t.getType().apply(model);
			
			for (Node tempNode : tempNodes.getMany())
			{
				nodes.add(new MyNode2(tempNode, t));
			}
			
		}
	}
	
	private void createEdges()
	{
		for (MyNode2 n: nodes)
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
				
				MyEdge2 tmp = new MyEdge2(e, t, findNode(sourceNode), findNode(destinationNode));
				
				edges.add(tmp);
			}
		}
		
	}
	
	public void addNode(MyNode2 node)
	{
		if (!isContained(node))
			nodes.add(node);
	}
	
	public void addEdge (MyEdge2 edge)
	{
		if (!isContained(edge))
			edges.add(edge);
	}
	
	public static  LinkedList<MyNode2> getAllNodes()
	{
		return nodes;
	}
	
	public static LinkedList<MyEdge2> getAllEdges()
	{
		return edges;
	}
	
	public boolean isContained (MyNode2 node)
	{
		return nodes.contains(node);
	}
	
	public boolean isContained (MyEdge2 edge)
	{
		return edges.contains(edge);
	}
	
	
	private MyNode2 findNode (Node n)
	{
		for (MyNode2 current : nodes)
		{
			if (current.compareTo(n))
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
	    
	    model = new ModelImpl();
	    
	    Node ebike = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(ebike, PSSIFValue.create("Ebike"));
	    
	    Node smartphone = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(smartphone, PSSIFValue.create("Smartphone"));
	    Node battery = node("hardware", meta).create(model);
	    node("hardware", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(battery, PSSIFValue.create("Battery"));
	    Node rentalApp = node("software", meta).create(model);
	    node("software", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(rentalApp, PSSIFValue.create("RentalApp"));
	    Node gpsApp = node("software", meta).create(model);
	    node("software", meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(gpsApp, PSSIFValue.create("GpsApp"));

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
	
	  private NodeType node(String name, MutableMetamodel metamodel) {
		    return metamodel.findNodeType(name);
		  }

	public static MyNodeTypes getNodeTypes() {
		return nodeTypes;
	}

	public static MyEdgeTypes getEdgeTypes() {
		return edgeTypes;
	}
}
