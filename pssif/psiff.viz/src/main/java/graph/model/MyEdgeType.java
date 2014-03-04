package graph.model;

import de.tum.pssif.core.metamodel.EdgeType;

/**
 * A container for the EdgeType from the PSSIF Model
 * Adds an easier API 
 * @author Luc
 *
 */
public class MyEdgeType {
	private EdgeType type; 
    private int lineType;
    
    /**
     * Creates a new MyEdgeType Object
     * @param type : EdgeType from the PSSIF Model
     * @param lineType : a number to identify the MyEdgeType
     */
    public MyEdgeType (EdgeType type, int lineType)
    {
    	this.type = type;
    	this.lineType = lineType;
    	
    }

	public EdgeType getType() {
		return type;
	}

	public int getLineType() {
		return lineType;
	}
    
	 public String getName() {
			return type.getName();
	}
	 
	 @Override
	 public boolean equals(Object obj) {
	     if (this == obj)
	         return true;
	     if (obj == null)
	         return false;
	     if (getClass() != obj.getClass())
	         return false;
	     
	     MyEdgeType tmp = (MyEdgeType) obj;
	     
	     if (tmp.getName().equals(getName()))
	    	 return true;
	     else
	    	 return false;
	 }
	 
	 public String toString() {
		return type.getName();
	}

    
}
