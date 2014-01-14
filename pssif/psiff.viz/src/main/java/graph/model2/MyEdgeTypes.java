package graph.model2;

import java.util.Collection;
import java.util.LinkedList;

import de.tum.pssif.core.metamodel.EdgeType;

public class MyEdgeTypes {
private LinkedList<MyEdgeType> types;
	
	private static int idcounter;
	public static String CONTAINMENT = "containment";
	
	public MyEdgeTypes (Collection<EdgeType> types)
	{
		idcounter =0;
		this.types = new LinkedList<MyEdgeType>();
		
		for (EdgeType et : types)
		{
			if (et.getName()!="Edge")
			{
				MyEdgeType tmp = new MyEdgeType(et, idcounter++);
				
				this.types.add(tmp);
			}
		}
	}
	
	/**
	 * Search a EdgeType by name
	 * @param name
	 * @return EdgeType or null if there is no EdgeType with the correct value
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
	
	public void addNodeTypes (Collection<EdgeType> newTypes)
	{
		for (EdgeType ntype : newTypes)
		{
			MyEdgeType tmp = new MyEdgeType(ntype, idcounter++);
			
			if (!types.contains(tmp))
				types.add(tmp);
		}
	}
	
	public void removeNodeType (Collection<EdgeType> oldTypes)
	{
		for (EdgeType otype : oldTypes)
		{
			MyEdgeType tmp = new MyEdgeType(otype, idcounter++);
			
			if (!types.contains(tmp))
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
