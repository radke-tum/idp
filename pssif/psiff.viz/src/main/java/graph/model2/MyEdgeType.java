package graph.model2;

import de.tum.pssif.core.metamodel.EdgeType;

public class MyEdgeType {
	private EdgeType type; 
    private int lineType;
    
    
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
