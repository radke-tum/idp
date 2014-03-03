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
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;

public class MyModelContainer {

	private Model model;
	private MutableMetamodel meta;
	private MyNodeTypes nodeTypes;
	private MyEdgeTypes edgeTypes;
	private LinkedList<MyNode> nodes;
	private LinkedList<MyEdge> edges;
	
	public MyModelContainer( Model model,  Metamodel meta)
	{
		this.model = model;
		this.meta = (MutableMetamodel) meta;
		
		nodes = new LinkedList<MyNode>();
		edges = new LinkedList<MyEdge>();
		
		createNodeTypes();
		createEdgeTypes();
		
		createNodes();
		createEdges();
		
	}
	
	private void createNodeTypes()
	{
		Collection<NodeType> types = meta.getNodeTypes();
		
		nodeTypes = new MyNodeTypes(types);
	}
	
	private void createEdgeTypes()
	{
		Collection<EdgeType> types = meta.getEdgeTypes();
		
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
	
	public LinkedList<MyNode> getAllNodes()
	{
		return nodes;
	}
	
	public LinkedList<MyEdge> getAllEdges()
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
	
	
	public MyNode findNode (Node n)
	{
		for (MyNode current : nodes)
		{
			if (current.getNode().equals(n))
				return current;
		}
		
		return null;
	}
	
	public MyEdge findEdge (Edge e)
	{
		for (MyEdge current : edges)
		{
			if (current.getEdge().equals(e))
				return current;
		}
		
		return null;
	}
	
	public MyNodeTypes getNodeTypes() {
		return nodeTypes;
	}

	public MyEdgeTypes getEdgeTypes() {
		return edgeTypes;
	}
	
	public void addCollapserEdge(MyEdge newEdge)
	{
		//MyEdge2 newEdge = new MyEdge2(edge, type, source, destination);
		newEdge.setCollapseEdge(true);
		edges.add(newEdge);
	}
	
	public void removeCollapserEdge(MyEdge edge)
	{
		if (edge.isCollapseEdge())
			edges.remove(edge);
	}
	
	public void addNewNodeFromGUI (String nodeName, MyNodeType type)
	{
		NodeType nodeType = meta.findNodeType(type.getName());
		
		Node newNode =nodeType.create(model);
		
		nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(newNode, PSSIFOption.one(PSSIFValue.create(nodeName)));
		
		nodes.add(new MyNode(newNode, type));
	}
	
	public static void removeNode (MyNode node)
	{
		//node.getNode().apply(new Disco);
		
		//Node newNode = node(type.getName(), meta)
		
		//node(type.getName(), meta).findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).set(newNode, PSSIFOption.one(PSSIFValue.create(nodeName)));
		
		//newNode.apply();
		//node.getNode().apply(new DisconnectOperation(null, null, null));
	}
	
	public boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype)
	{
		ConnectionMapping mapping = edgetype.getType().getMapping(source.getNodeType().getType(), destination.getNodeType().getType());
		
		if (mapping!=null)
		{
			Edge newEdge = mapping.create(model, source.getNode(), destination.getNode());
				
			MyEdge e  = new MyEdge(newEdge, edgetype, source, destination);
			
			edges.add(e);
			return true;
		}
		else
			return false;
	}
}
