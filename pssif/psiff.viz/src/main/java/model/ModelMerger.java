package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import de.tum.pssif.core.metamodel.Annotation;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
/**
 * Very basic Model Merger. Can merge two models into one model. Does only copy everything from one model to the other. No matching at all!
 * @author Luc
 *
 */
public class ModelMerger {
	
	private Model model1;
	private Model model2;
	private Metamodel meta;
	private HashMap<Node, NodeType> transferNodes;
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
		this.transferNodes = new HashMap<Node, NodeType>();
		this.oldToNewNodes = new HashMap<Node, Node>();
		
		//printNbEdges(model1);
	//	printNbNodes(model1);
		// start transformation operations
		addAllNodes();
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
		      for (ConnectionMapping mapping : t.getMappings()) 
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
					transferNodes.put(n, t);
				}
			}
			else 
			{
				if (tempNodes.isOne())
				{
					Node current = tempNodes.getOne();
					// copy it to model1
					addNode(current, t);
					transferNodes.put(current, t);
				}
			}
		}
	}
	
	/**
	 * Add all the outgoing Edges of the given Node to the model1
	 * @param sourceNode the Node where the edges start
	 * @param sourceNodeType the type of the sourceNode
	 */
	private void addAllEdges()
	{
		 //TODO handle hyperedges correctly
		for (EdgeType t : meta.getEdgeTypes()) {
		      for (ConnectionMapping mapping : t.getMappings()) {
		        EdgeEnd from = mapping.getFrom();
		        EdgeEnd to = mapping.getTo();
		        
		        PSSIFOption<Edge> edges = mapping.apply(model2);
		        if (edges.isMany())
		        {
		        	//System.out.println("Multi Edge");
		        	for (Edge e : edges.getMany()) {
			        	if (from.apply(e).isOne() && to.apply(e).isOne())
			        	{
			        		Node source = from.apply(e).getOne();
			        		Node target = to.apply(e).getOne();
			        		//System.out.println("only one source and dest");
			        		//System.out.println("Create edge from "+oldToNewNodes.get(source)+ " to "+ oldToNewNodes.get(target) +" EdgeType "+ t );
			        		Edge newEdge = mapping.create(model1, oldToNewNodes.get(source), oldToNewNodes.get(target));
							
							transferEdgeAttributes(e, newEdge, t);
			        		
			        	}
			        	else
			        	{
				        	for (Node source : from.apply(e).getMany()) 
					          {
					            for (Node target : to.apply(e).getMany()) 
					            {
					            //	System.out.println("more sources and dests");
					            //	System.out.println("Create edge from "+oldToNewNodes.get(source)+ " to "+ oldToNewNodes.get(target) +" EdgeType "+ t );
					            	Edge newEdge = mapping.create(model1, oldToNewNodes.get(source), oldToNewNodes.get(target));
									
									transferEdgeAttributes(e, newEdge, t);
					            }
					          }
			        	}
			        }
		        }
		        else if (edges.isOne())
		        {
		        //	System.out.println("Single Edge");
		        	Edge e = edges.getOne();
		        	for (Node source : from.apply(e).getMany()) {
			            for (Node target : to.apply(e).getMany()) {
			            	//System.out.println("Create edge from "+oldToNewNodes.get(source)+ " to "+ oldToNewNodes.get(target) +" EdgeType "+ t );
			            	Edge newEdge = mapping.create(model1, oldToNewNodes.get(source), oldToNewNodes.get(target));
							
							transferEdgeAttributes(e, newEdge, t);
			            }
			        }
		        }
		      }
		    }
	}
	
	/**
	 * add all the Edges from model2 to model1
	 */
	/*private void addAllEdges()
	{
		//check all the Nodes which were transferde from model1 to model2
		for (Entry<Node, NodeType> entry : transferNodes.entrySet())
		{
			addEdge(entry.getKey(),entry.getValue());
		}
	}*/
	
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
						currentType.findAttribute(a.getName()).set(newNode, attrvalue);
					}
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
	
	/**
	 * Add all the outgoing Edges of the given Node to the model1
	 * @param sourceNode the Node where the edges start
	 * @param sourceNodeType the type of the sourceNode
	 */
	/*private void addEdge(Node sourceNode, NodeType sourceNodeType)
	{
		// loop over all Edge types
		for(EdgeType currentEdgeType : meta.getEdgeTypes())
		{
			
			// get all the outgoing edges of the sourceNode with the current EdgeType
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
			
			// loop over all the outgoing edges
			for (Edge currentEdge : tmpedges)
			{
				// get all the destinations of the current Edge
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
				
				// loop over all the destinations and add them to the model1
				for (Node destNode : tmpdestinations)
				{
					//TODO check what should be done with edges with multiple destinations
					
					
					// Add the Edge to model1
					NodeType destNodeType = findNodeType(destNode);
					
					if (destNodeType!=null)
					{
						ConnectionMapping mapping = currentEdgeType.getMapping(sourceNodeType, destNodeType);
						
						System.out.println("Test mapping null ? "+mapping==null);
						System.out.println("sourceNode ? "+sourceNode==null);
						System.out.println("destNode ? "+destNode==null);
						Edge newEdge = mapping.create(model1, oldToNewNodes.get(sourceNode), oldToNewNodes.get(destNode));
						
						transferEdgeAttributes(currentEdge, newEdge, currentEdgeType);
					}
					else
						throw new NullPointerException("Could not find the correct NodeType");
					
				}
			}
		}
	
	}*/
	
	
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
						type.findAttribute(a.getName()).set(newEdge, attrvalue);
					}
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
	
	/**
	 * Get the NodeType of the given Node. Only works for Nodes which are transfered from model2 to model1
	 * @param n the Node in context
	 * @return the NodeType or null
	 */
	private NodeType findNodeType (Node n)
	{
		return transferNodes.get(n);
	}
	
	
}
