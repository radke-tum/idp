package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;







import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * Very basic Model Merger. Can merge two models into one model. Does only copy everything from one model to the other. No matching at all!
 * @author Luc
 *
 */
public class ModelMerger {
	
	private Model model1;
	private Model model2;
	private Metamodel meta;

	private HashMap<Node, Node> oldToNewNodes;
	
	/**
	 * merge two models into one model in respect of the given metamodel
	 * @param model1 first model
	 * @param model2 second model
	 * @param meta metamodel
	 * @return the merged model
	 */
	public Model mergeModels (Model model1, Model model2, Metamodel meta)
	{
		this.model1 = model1;
		this.model2 = model2;
		this.meta = meta;
		this.oldToNewNodes = new HashMap<Node, Node>();
		
		//printNbEdges(model1);
	//	printNbNodes(model1);
		// start transformation operations
		addAllNodes();
		addAllJunctionNodes();
		addAllEdges();
		
	//	printNbEdges(model1);
	//	printNbNodes(model1);
		
		return this.model1;
	}
	
	private void printNbEdges(Model model)
	{
		int counter =0;
		for (EdgeType t : meta.getEdgeTypes()) 
		{
		    PSSIFOption<ConnectionMapping>tmp = t.getMappings();
		    
		    if (tmp!= null && (tmp.isMany() || tmp.isOne()))
		    {
				Set<ConnectionMapping> mappings;
				
				if (tmp.isMany())
					mappings = tmp.getMany();
				else
				{
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}
		    	
		    	for (ConnectionMapping mapping :mappings) 
			      {
			        PSSIFOption<Edge> edges = mapping.apply(model);
			        
			        if (edges.isMany())
			        {
				        for (Edge e : edges.getMany()) {
				        	counter++;
				        }
			        }
			        else 
			        	{
			        		if (edges.isOne())
			        		{
			        			counter++;
			        		}
			        	}
			      }
		    }
		}
		System.out.println("Nb edges :"+counter);	        
	}
	
	private void printNbNodes(Model model)
	{
		int counter =0;
		for (NodeType t : meta.getNodeTypes())
		{
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(model,true);
			
			 if (tempNodes.isMany())
				{
					Set<Node> many = tempNodes.getMany();
					for (Node n : many)
					{
						counter++;
					}
				}
			else 
			if (tempNodes.isOne())
			{
				counter++;
			}

		}
		System.out.println("Nb nodes :"+counter);	        
	}

	/**
	 * add all the Nodes from model2 to model1
	 */
	private void addAllNodes()
	{	
		// loop over all Node types
		for (NodeType t : meta.getNodeTypes())
		{
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(model2,false);
			
			if (tempNodes.isMany())
			{
				Set<Node> many = tempNodes.getMany();
				for (Node n : many)
				{
					// copy it to model1
					addNode(n, t);
				}
			}
			else 
			{
				if (tempNodes.isOne())
				{
					Node current = tempNodes.getOne();
					// copy it to model1
					addNode(current, t);
				}
			}
		}
	}
	
	/**
	 * add all the JunctionNodes from model2 to model1
	 */
	private void addAllJunctionNodes()
	{	
		// loop over all JunctionNode types
		for (JunctionNodeType t : meta.getJunctionNodeTypes())
		{
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(model2,false);
			
			if (tempNodes.isMany())
			{
				Set<Node> many = tempNodes.getMany();
				for (Node n : many)
				{
					// copy it to model1
					addJunctionNode(n, t);
				}
			}
			else 
			{
				if (tempNodes.isOne())
				{
					Node current = tempNodes.getOne();
					// copy it to model1
					addJunctionNode(current, t);
				}
			}
		}
	}
	
	/**
	 * Add all the Edges from model2 to model1
	 */
	private void addAllEdges()
	{
		for (EdgeType t : meta.getEdgeTypes()) {
			PSSIFOption<ConnectionMapping>tmp = t.getMappings();
		    
		    if (tmp!= null && (tmp.isMany() || tmp.isOne()))
		    {
				Set<ConnectionMapping> mappings;
				
				if (tmp.isMany())
					mappings = tmp.getMany();
				else
				{
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}
			
			      for (ConnectionMapping mapping : mappings) {
			        PSSIFOption<Edge> edges = mapping.apply(model2);
			        if (edges.isMany())
			        {
			        	for (Edge e : edges.getMany()) 
			        	{
			        		Node source = mapping.applyFrom(e);
			        		Node target = mapping.applyTo(e);
			        		
			        		Edge newEdge = mapping.create(model1, oldToNewNodes.get(source), oldToNewNodes.get(target));
							transferEdgeAttributes(e, newEdge, t);
				        }
			        }
			        else if (edges.isOne())
			        {
			        	Edge e = edges.getOne();
			        	Node source = mapping.applyFrom(e);
		        		Node target = mapping.applyTo(e);
		        		
		        		Edge newEdge = mapping.create(model1, oldToNewNodes.get(source), oldToNewNodes.get(target));
						transferEdgeAttributes(e, newEdge, t);
			        }
		      }
		    }
		 }
	}
	
	/**
	 * Add a given Node to Model1
	 * @param dataNode the model which should be transfered to model1
	 * @param currentType the type of the dataNode
	 */
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
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);
					
					if (attrvalue!= null)
					{
						currentType.getAttribute(a.getName()).getOne().set(newNode, attrvalue);
					}
				}
			}
		}
		
		// transfer annotations
		
		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();
		
		Set<Entry<String, String>> annotations =null;
		
		if (tmp!=null && (tmp.isMany() || tmp.isOne()))
		{
			if (tmp.isMany())
				annotations = tmp.getMany();
			else
			{
				annotations = new HashSet<Entry<String,String>>();
				annotations.add(tmp.getOne());
			}
		}
		
		if (annotations!=null)
		{
			for (Entry<String,String> a : annotations)
			{
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
		
		/*
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
		}*/
	}
	
	/**
	 * Add a given JunctionNode to Model1
	 * @param dataNode the model which should be transfered to model1
	 * @param currentType the type of the dataNode
	 */
	private void addJunctionNode(Node dataNode, JunctionNodeType currentType)
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
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);
					
					if (attrvalue!= null)
					{
						currentType.getAttribute(a.getName()).getOne().set(newNode, attrvalue);
					}
				}
			}
		}
		
		// transfer annotations
		
		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();
		
		Set<Entry<String, String>> annotations =null;
		
		if (tmp!=null && (tmp.isMany() || tmp.isOne()))
		{
			if (tmp.isMany())
				annotations = tmp.getMany();
			else
			{
				annotations = new HashSet<Entry<String,String>>();
				annotations.add(tmp.getOne());
			}
		}
		
		if (annotations!=null)
		{
			for (Entry<String,String> a : annotations)
			{
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
	}
	
	/**
	 * transfer all the attributes and annotations from one Edge to the other
	 * @param oldEdge contains all the information which should be transfered
	 * @param newEdge the edge which should get all the information
	 * @param type the type of both edges
	 */
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
					PSSIFOption<PSSIFValue> attrvalue = a.get(oldEdge);
					
					if (attrvalue!= null)
					{
						type.getAttribute(a.getName()).getOne().set(newEdge, attrvalue);
					}
				}
			}
		}
			
		// transfer annotations
		
		PSSIFOption<Entry<String, String>> tmp = oldEdge.getAnnotations();
		
		Set<Entry<String, String>> annotations =null;
		
		if (tmp!=null && (tmp.isMany() || tmp.isOne()))
		{
			if (tmp.isMany())
				annotations = tmp.getMany();
			else
			{
				annotations = new HashSet<Entry<String,String>>();
				annotations.add(tmp.getOne());
			}
		}
		
		if (annotations!=null)
		{
			for (Entry<String,String> a : annotations)
			{
				newEdge.annotate(a.getKey(), a.getValue());
			}
		}
		
	/*	Collection<Annotation> annotations = type.getAnnotations(oldEdge);
		
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
		}*/
	}
}
