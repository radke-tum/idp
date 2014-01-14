package graph.model2;

import de.tum.pssif.core.metamodel.NodeType;

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
	
}
