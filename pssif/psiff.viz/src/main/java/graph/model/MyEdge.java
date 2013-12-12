package graph.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class MyEdge {
	private ConnectionType type;
	private int id;
	static int edgeCount;
	private List<String> attributes;
	private MyNode source;
	private MyNode destination;
	

	public MyEdge(ConnectionType type, MyNode source, MyNode destination ) {
		this.id = edgeCount++; 
		this.type =type;
		this.attributes = new LinkedList<String>();
		this.source=source;
		this.destination = destination;
		
	}
	
	public MyEdge(ConnectionType type, List<String> attributes, MyNode source, MyNode destination) {
		this.id = edgeCount++; 
		this.type =type;
		this.attributes = attributes;
		this.source=source;
		this.destination = destination;
		
	}

	public String toString() {
		String s ;
		
		s= "&lt;&lt;  "+type.getName()+"  &gt;&gt;";
			
		for (String a : attributes)
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
	
	public ConnectionType getConnectionType()
	{
		return type;
	}

	public MyNode getSourceNode() {
		return source;
	}

	public MyNode getDestinationNode() {
		return destination;
	}
	
	
}
