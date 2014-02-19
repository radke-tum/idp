package graph.model2;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.util.PSSIFValue;

/**
 * A Data container for the Edge Type from the PSS-IF Model
 * Helps to manage the visualization/modification of the Edge
 * @author Luc
 *
 */
public class MyEdge2 {
	private MyEdgeType type;
	private MyNode2 source;
	private MyNode2 destination;
	private Edge edge;
	
	/**
	 * Creates a new MyEdge2 Object
	 * @param edge : Edge || an edge from the PSS-IF Model
	 * @param type : MyEdgeType || the type of the edge
	 * @param source : MyNode2 || the startpoint of the Edge
	 * @param destination : MyNode2 || the endpoint of the Edge
	 */
	public MyEdge2(Edge edge, MyEdgeType type, MyNode2 source, MyNode2 destination ) {
		this.type =type;
		this.source=source;
		this.destination = destination;
		this.edge = edge;
	}
	
	/**
	 * Get all the attributes from the PSS-IF Model Edge
	 * @return LinkedList<String> with the formated information from the edge. Might be empty
	 */
	private LinkedList<String> calcAttr()
	{
		LinkedList<String> res = new LinkedList<String>();
		
		// get all the attributes
		Collection<Attribute> attr = type.getType().getAttributes();
		
		// loop over all the attributes
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			PSSIFValue value=null;
			
			// check if there is any value available in this concrete edge for the current attribute
			if (current.get(edge)!=null && current.get(edge).isOne())
				value = current.get(edge).getOne();
			
			String attrValue="";
			if (value !=null)
				attrValue = String.valueOf(value.getValue());
			
			String attrUnit = current.getUnit().getName();
			
			String tmp = attrName+" = "+attrValue+" : "+attrUnit;
			
			// check if the attribute was not empty. If it was empyt do not show it
			if (current.get(edge)!=null && attrValue.length()>0)
				res.add(tmp);
		}
		
		return res;
	}
	
	/*
	public String toString() {
		String s ;
		
		s= "&lt;&lt;  "+type.getName()+"  &gt;&gt;";
			
		for (String a : calcAttr())
		{
			s+="<br>";
			s+=a;
		}
		
		return s;
	}*/
	/**
	 * Get all the Informations about the Edge. Should only be used in the GraphVisualization
	 * @return a HTML String with all the edge informations
	 */
	public String getEdgeInformations()
	{
		String s ;
		
		s= "&lt;&lt;  "+type.getName()+"  &gt;&gt;";
			
		for (String a : calcAttr())
		{
			s+="<br>";
			s+=a;
		}
		
		return s;
	}
	
	public MyEdgeType getEdgeType()
	{
		return type;
	}

	public MyNode2 getSourceNode() {
		return source;
	}

	public MyNode2 getDestinationNode() {
		return destination;
	}
	
	/**
	 * Get all the attributes from the PSS-IF Model Edge
	 * @return LinkedList<String> with the formated information from the edge. Might be empty
	 */
	public List<String> getAttributes()
	{
		return calcAttr();
	}
	
	/**
	 * Get a Mapping from an Attribute name to an Attribute object which contains all the infomations
	 * @return HashMap<String, Attribute> || Might be empty
	 */
	public HashMap<String, Attribute> getAttributesHashMap()
	{
		 HashMap<String, Attribute> res = new  HashMap<String, Attribute>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			
			res.put(attrName, current);
		}
		
		return res;
	}
	
	/**
	 * Get the Edge object from the PSSIF Model
	 * @return Edge
	 */
	public Edge getEdge() {
		return edge;
	}

	
	
}
