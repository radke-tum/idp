package graph.model;

import java.util.Collection;
import java.util.LinkedList;
import de.tum.pssif.core.metamodel.NodeType;

/**
 * All the possible Node Types of the PSSIF Model
 * @author Luc
 *
 */
public class MyNodeTypes {
	private LinkedList<MyNodeType> types;
	
	public MyNodeTypes (Collection<NodeType> types)
	{
		this.types = new LinkedList<MyNodeType>();
		
		for (NodeType nt : types)
		{
			MyNodeType tmp = new MyNodeType(nt);
				
			this.types.add(tmp);
		}
		
	}
	
	/**
	 * Search a NodeType by name
	 * @param name
	 * @return NodeType or null if there is no NodeType with the correct value
	 */
	public MyNodeType getValue(String name)
	{
		for (MyNodeType type : types)
		{
			if (type.getName().equals(name))
				return type;
			
		}
		
		//No value found
		return null;
	}
	
	public void addNodeTypes (Collection<NodeType> newTypes)
	{
		for (NodeType ntype : newTypes)
		{
			MyNodeType tmp = new MyNodeType(ntype);
			
			if (!types.contains(tmp))
				types.add(tmp);
		}
	}
	
	public void removeNodeType (Collection<NodeType> oldTypes)
	{
		for (NodeType ntype : oldTypes)
		{
			MyNodeType tmp = new MyNodeType(ntype);
			
			types.remove(tmp);
		}
		
	}
	
	public LinkedList<MyNodeType> getAllNodeTypes()
	{
		return types;
	}
	
	public MyNodeType[] getAllNodeTypesArray()
	{
		return types.toArray(new MyNodeType[types.size()]);
	}
}
