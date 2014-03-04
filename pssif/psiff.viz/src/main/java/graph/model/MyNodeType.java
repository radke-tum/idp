package graph.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import model.ModelBuilder;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;

/**
 * A datacontainer with the node type information of the PSSIF- Model for the Jung API 
 * @author Luc
 *
 */
public class MyNodeType {

	private NodeType type;

	public MyNodeType(NodeType type) {
		this.type = type;
	}

	public String getName() {
		return type.getName();
	}

	/*public int getID() {
		return id;
	}

	private int getParentID() {
		return parentType;
	}*/

	public NodeType getParentType() {

		return type.getGeneral();
	}
	
	public NodeType getType()
	{
		return type;
	}
	
	public String toString() {
		return type.getName();
	}
	
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    
	    MyNodeType other = (MyNodeType) obj;
	    
	    if (this.getName()==other.getName())
	    	return true;
	    else
	    	return false;
	}
	
	public LinkedList<MyEdgeType> getpossibleEdgeTypes()
	{
		LinkedList<MyEdgeType> res = new LinkedList<MyEdgeType>();
		for(EdgeType et: type.getOutgoings())
		{
			
			MyEdgeType met = ModelBuilder.getEdgeTypes().getValue(et.getName());
			res.add(met);
		}
		
		
		Collections.sort(res, new MyEdgeTypeComparator());
		
		return res;
	}
	
	/**
	 * provides a possibility to compare the EdgeTypes
	 * @author Luc
	 *
	 */
	protected class MyEdgeTypeComparator implements Comparator<MyEdgeType>
	{
	  @Override public int compare( MyEdgeType type1, MyEdgeType type2 )
	  {
	    return type1.getName().compareTo(type2.getName());
	  }
	}
}
