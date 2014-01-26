package graph.model2;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.model.Edge;

public class MyEdge2 {
	private MyEdgeType type;
	private int id;
	static int edgeCount;
	//private List<String> attributes;
	private MyNode2 source;
	private MyNode2 destination;
	private Edge edge;
	

	public MyEdge2(Edge edge, MyEdgeType type, MyNode2 source, MyNode2 destination ) {
		this.id = edgeCount++; 
		this.type =type;
		//this.attributes = new LinkedList<String>();
		this.source=source;
		this.destination = destination;
		this.edge = edge;
		
		calcAttr();
	}
	
	private LinkedList<String> calcAttr()
	{
		LinkedList<String> res = new LinkedList<String>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			String attrValue = String.valueOf(current.get(edge));
			String attrUnit = current.getUnit().getName();
			
			String tmp = attrName+" = "+attrValue+" : "+attrUnit;
			
			if (current.get(edge)!=null)
				res.add(tmp);
		}
		
		return res;
	}

	public String toString() {
		String s ;
		
		s= "&lt;&lt;  "+type.getName()+"  &gt;&gt;";
			
		for (String a : calcAttr())
		{
			s+="<br>";
			s+=a;
		}
		
		return s;
	}
	
	public int getId()
	{
		return id;
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
	
	public List<String> getAttributes()
	{
		return calcAttr();
		//return this.attributes;
	}
	
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

	public Edge getEdge() {
		return edge;
	}

	
	
}
