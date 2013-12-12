package graph.model;

public enum NodeType {
		NODE ("Node",1,1),
	    REQUIREMENT ("Requirement", 2, 17),
	    USECASE   ("Usecase", 3, 17),
	    TESTCASE ("Testcase", 4, 17),
	    ISSUE ("Issue",5,19),
	    DECISION ("Decision",6,19),
	    CHANGEEVENT ("ChangeEvent",7,19),
	    VIEW ("View",8,17),
	    BLOCK ("Block",9,18),
	    ACTOR ("Actor",10,9),
	    SERVICE ("Service",11,9),
	    SOFTWARE ("Software",12,9),
	    HARDWARE ("Hardware",13,9),
	    FUNCTION ("Function",14,18),
	    ACTIVITY ("Activity",15,14),
	    STATE ("State",16,14),
	    DEVELOPMENTARTIFACT ("DevelopmentArtifact",17,1),
	    SOLUTIONARTIFACT ("SolutionArtifact",18,1),
	    EVENT ("Event",19,17);
	    
	    private final String name; 
	    private final int id;
	    private final int parentType;
	    
	    NodeType (String name, int id, int parent)
	    {
	    	this.name=name;
	    	this.id=id;
	    	this.parentType=parent;
	    }
	    
	    public String getName() {
			return name;
		}

		public int getID() {
			return id;
		}
		
		private int getParentID()
		{
			return parentType;
		}
		
		public NodeType getParentType()
		{
			NodeType[] all = NodeType.values();
			
			for (NodeType nt : all)
			{
				if (nt.getParentID() == parentType)
					return nt;
			}
			
			return null;
		}
		
		public static NodeType getValue (String s)
		{
			NodeType[] all = NodeType.values();
			
			for (NodeType nt : all)
			{
				if (nt.getName().equals(s))
					return nt;
			}
			return null;
		}
}
