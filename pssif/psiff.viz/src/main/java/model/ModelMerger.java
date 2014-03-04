package model;



import graph.model.MyEdge;
import graph.model.MyEdgeType;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.functors.NullIsExceptionPredicate;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Annotation;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
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

public class ModelMerger {
	
	private Model model1;
	private Model model2;
	private MutableMetamodel meta;
	private HashMap<Node, NodeType> transferNodes;
	private HashMap<Node, Node> oldToNewNodes;
	
	public Model mergeModels (Model model1, Model model2, Metamodel meta)
	{
		this.model1 = model1;
		this.model2 = model2;
		this.meta = (MutableMetamodel)meta;
		this.transferNodes = new HashMap<Node, NodeType>();
		this.oldToNewNodes = new HashMap<Node, Node>();
		
		// start transformation operations
		addAllNodes();
		addAllEdges();
		
		return this.model1;
	}

	
	private void addAllNodes()
	{
		for (NodeType t : meta.getNodeTypes())
		{
			//TODO add all Node from model2 to model1
			PSSIFOption<Node> tempNodes = t.apply(model2,true);
			
			if (tempNodes.isOne())
			{
				Node current = tempNodes.getOne();
				addNode(current, t);
				transferNodes.put(current, t);
			}
			
			if (tempNodes.isMany())
			{
				Set<Node> many = tempNodes.getMany();
				for (Node n : many)
				{
					addNode(n, t);
					transferNodes.put(n, t);
				}
			}
		}
	}
	
	private void addAllEdges()
	{
		for (Entry<Node, NodeType> entry : transferNodes.entrySet())
		{
			//TODO add all Edges
			addEdge(entry.getKey(),entry.getValue());
		}
	}
	
	private void addNode(Node dataNode, NodeType currentType)
	{
		// create Node
		Node newNode = currentType.create(model1);
		
		oldToNewNodes.put(dataNode, newNode);
		
		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = currentType.getAttributeGroups();
		
		if (attrgroups !=null)
		{
			for (AttributeGroup ag : attrgroups)
			{
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();
				
				for (Attribute a : attr)
				{
					//if (!a.getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_ID))
				//	{
						PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);
						
						if (attrvalue!= null)
						{
							currentType.findAttribute(a.getName()).set(newNode, attrvalue);
						}
				//	}
				}
			}
		}
		
		// transfer annotations
		
		Collection<Annotation> annotations = currentType.getAnnotations(dataNode);
		
		if (annotations!=null)
		{
			for (Annotation a : annotations)
			{
				PSSIFOption<String> value =  a.getValue();
				if (value!=null && value.isOne())
				{
					currentType.setAnnotation(newNode, a.getKey(),value.getOne());
				}
				
				if (value!=null && value.isMany())
				{
					Set<String> concreteValues = value.getMany();
					for (String s : concreteValues)
					{
						currentType.setAnnotation(newNode, a.getKey(),s);
					}
				}
			}
		}
	}
	
	private void addEdge(Node sourceNode, NodeType sourceNodeType)
	{
		for(EdgeType currentEdgeType : meta.getEdgeTypes())
		{
			//System.out.println("found EdgeType "+currentEdgeType.getName());
			PSSIFOption<Edge> outgoingEdges = currentEdgeType.getOutgoing().apply(sourceNode);
			
			LinkedList<Edge> tmpedges = new LinkedList<Edge>();
			if (outgoingEdges!=null && outgoingEdges.isMany())
			{
				for (Edge e : outgoingEdges.getMany())
				{
					tmpedges.add(e);
				}
			}
			
			if (outgoingEdges!=null && outgoingEdges.isOne())
			{
				tmpedges.add(outgoingEdges.getOne());
				
			}
			
			//System.out.println("nb of edges found "+ tmpedges.size());
			for (Edge currentEdge : tmpedges)
			{
				PSSIFOption<Node> destinations = currentEdgeType.getIncoming().apply(currentEdge);
				
				LinkedList<Node> tmpdestinations = new LinkedList<Node>();
				
				if (destinations!= null && destinations.isOne())
				{
					tmpdestinations.add(destinations.getOne());
				}
				
				if (destinations!= null && destinations.isMany())
				{
					for (Node n : destinations.getMany())
					{
						tmpdestinations.add(n);
					}
					throw new NullPointerException("What to do with edges with multiple ends");
				}
				
				System.out.println("nb of destinations found "+ tmpdestinations.size());
				for (Node destNode : tmpdestinations)
				{
					NodeType destNodeType = findNodeType(destNode);
					
					ConnectionMapping mapping = currentEdgeType.getMapping(sourceNodeType, destNodeType);
					
					//System.out.println("			created edge from "+ sourceNode +" to "+destNode);
					Edge newEdge = mapping.create(model1, oldToNewNodes.get(sourceNode), oldToNewNodes.get(destNode));
					
					transferEdgeAttributes(currentEdge, newEdge, currentEdgeType);
					
				}
			}
		}
	
	}
	
	private void transferEdgeAttributes (Edge oldEdge, Edge newEdge, EdgeType type)
	{
		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = type.getAttributeGroups();
		
		if (attrgroups!=null)
		{
			for (AttributeGroup ag : attrgroups)
			{
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();
				
				for (Attribute a : attr)
				{
					//if (!a.getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_ID))
					//{
						PSSIFOption<PSSIFValue> attrvalue = a.get(oldEdge);
						
						if (attrvalue!= null)
						{
							type.findAttribute(a.getName()).set(newEdge, attrvalue);
						}
					//}
				}
			}
		}
			
		// transfer annotations
		
		Collection<Annotation> annotations = type.getAnnotations(oldEdge);
		
		if (annotations!=null)
		{
			for (Annotation a : annotations)
			{
				PSSIFOption<String> value =  a.getValue();
				if (value!=null && value.isOne())
				{
					type.setAnnotation(newEdge, a.getKey(),value.getOne());
				}
				
				if (value!=null && value.isMany())
				{
					Set<String> concreteValues = value.getMany();
					for (String s : concreteValues)
					{
						type.setAnnotation(newEdge, a.getKey(),s);
					}
				}
			}
		}
	}
	
	private NodeType findNodeType (Node n)
	{
		return transferNodes.get(n);
	}
	
	
}
