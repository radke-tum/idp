package graph.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import de.tum.pssif.core.metamodel.EdgeType;

/**
 * A container for all the EdgeTypes which pop up in the PSSIF Model
 * @author Luc
 *
 */
public class MyEdgeTypes {
	
	private LinkedList<MyEdgeType> types;
	private static int idcounter;
	public static String CONTAINMENT = "containment";
	
	/**
	 * Create a new MyEdgeTypes Object
	 * @param types : Collection<EdgeType> || all the EdgeTypes found in the PSSIF Model
	 */
	public MyEdgeTypes (Collection<EdgeType> types)
	{
		idcounter =0;
		this.types = new LinkedList<MyEdgeType>();
		
		for (EdgeType et : types)
		{
			MyEdgeType tmp = new MyEdgeType(et, idcounter++);
				
			this.types.add(tmp);
		}
	}
	
	public MyEdgeTypes (HashSet<MyEdgeType> newTypes)
	{
		this.types = new LinkedList<MyEdgeType>();
		
		Iterator<MyEdgeType> it = newTypes.iterator();
		
		while (it.hasNext())
		{
			MyEdgeType tmp = it.next();
			
			if (!types.contains(tmp))
				types.add(tmp);
		}
	}
	
	/**
	 * Search an EdgeType by a given name
	 * @param name :String
	 * @return EdgeType or null if there is no EdgeType with the given value
	 */
	public MyEdgeType getValue(String name)
	{
		for (MyEdgeType type : types)
		{
			if (type.getName().equals(name))
				return type;
			
		}
		
		//No value found
		return null;
	}
	
	/**
	 * Adds a collection of new EdgeTypes  
	 * @param newTypes : Collection<EdgeType>
	 */
	public void addEdgeTypes (Collection<EdgeType> newTypes)
	{
		for (EdgeType ntype : newTypes)
		{
			MyEdgeType tmp = new MyEdgeType(ntype, idcounter++);
			
			// check if they are not already contained
			if (!types.contains(tmp))
				types.add(tmp);
		}
	}
	/**
	 * Removes a collection of EdgeTypes
	 * @param oldTypes : Collection<EdgeType>
	 */
	public void removEdgeType (Collection<EdgeType> oldTypes)
	{
		for (EdgeType otype : oldTypes)
		{
			MyEdgeType tmp = new MyEdgeType(otype, idcounter++);
			
			// check if it his even contained
			if (types.contains(tmp))
				types.remove(tmp);
		}
	}
	
	public LinkedList<MyEdgeType> getAllEdgeTypes()
	{
		return types;
	}
	
	public MyEdgeType[] getAllEdgeTypesArray()
	{
		return types.toArray(new MyEdgeType[types.size()]);
	}
}
