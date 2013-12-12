package graph.model;

public enum ConnectionType {
    RELATIONSHIP ("Relationship", 1,1),
    ATTRIBUTIONALRELATION   ("Attributional Relation", 2,1),
    CHRONOLOGICRELATION ("Chronological Relation", 3,1),
    REFERENCIALRELATION ("Referencial Relation",4,1),
    INCLUSIONRELATION ("Inclusion Relation",5,1),
    CAUSALRELATION ("Causal Relation",6,1),
    LOGICALRELATION ("Logical Relation",7,1),
    PERFORMS ("Performs",8,2),
    ACCOUNTSFOR ("Accounts for",9,2),
    EVOLVESTO ("Evolves to",10,3),
    REPLACES ("Replaces",11,3),
    BASEDON ("Based on",12,3),
    REFINES ("Refines",13,3),
    PRECONDITION ("Precondition",14,3),
    DESCRIBES ("Describes",15,4),
    DEFINES ("Defines",16,4),
    TRACE ("Trace",17,4),
    USES ("Uses",18,4),
    CONTAINS ("Contains",19,5),
    INCLUDES ("Includes",20,5),
    GENERALIZES ("Generalizes",21,5),
    CREATES ("Creates",22,6),
    REQUESTS ("Requests",23,6),
    REQUIRES ("Requires",24,6),
    IMPLEMENTS ("Implements",25,6),
    REALIZES ("Realizes",26,6),
    CONFLICTS ("Conflicts",27,7),
    EXTENDS ("Extends",28,7),
    SATISFIES ("Satisifies",29,7),
    VERIFIES ("Verifies",30,7),
    OVERLAPS ("Overlaps",31,7),
    ISALTERNATIVE ("Is Alternative",32,7),
    AVOIDS ("Avoids",33,2);
    
    private final String name; 
    private final int lineType;
    private final int parentEdge;
    
    ConnectionType (String name, int lineType, int parent)
    {
    	this.name=name;
    	this.lineType=lineType;
    	this.parentEdge = parent;
    }
    
    public String getName() {
		return name;
	}

	public int getLineType() {
		return lineType;
	}
	
	private int getParent()
	{
		return parentEdge;
	}
	
	public ConnectionType getParentRelationShip()
	{
		ConnectionType[] all = ConnectionType.values();
		
		for (ConnectionType c: all)
		{
			if (getParent() == c.getParent())
				return c;
		}
		return null;
	}
	
	public static ConnectionType getValue (String s)
	{
		ConnectionType[] all = ConnectionType.values();
		
		for (ConnectionType c : all)
		{
			if (c.getName().equals(s))
				return c;
		}
		return null;
	}
}
