package graph.operations;

public enum AttributeOperations {
	
	GREATER("Greater than"),
	LESS ("Less than"),
	GREATER_EQUAL("Greater or equal than"),
	LESS_EQUAL("LESS or equal than"),
	EQUAL ("Equal"),
	NOT_EQUAL ("Not equal");
	
	private final String name;
	
	AttributeOperations (String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static AttributeOperations getValueOf(String s)
	{
		AttributeOperations res =null;
		AttributeOperations[] all = AttributeOperations.values();
		
		for (AttributeOperations tmp : all)
		{
			if (tmp.getName().equals(s))
				res = tmp;
		}
		
		return res;
	}
}
