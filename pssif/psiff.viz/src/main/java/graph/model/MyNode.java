package graph.model;

import java.util.List;

public class MyNode {
	private int id; 
	private String name;
	private List<String> attributes ;
	private int size;
	private NodeType type;
	private boolean detailedOutput;
	public static int idcounter;
	
	private static int limit = 15;
	private static int heightlimit = 2;
	
	
	public MyNode(String name, List<String> attributes, NodeType type) {
		this.id = idcounter++;
		this.name =name;
		this.attributes = attributes;
		this.type = type;
		//setSize();
		
	}
	
	public static void setidcounter(int lastId)
	{
		idcounter=lastId;
	}
	
	private void setSize()
	{
		int temp = name.length() / limit;
		
		if (temp >0)
			size = temp;
		
		
		for (String s : attributes)
		{
			temp = s.length() / limit;
			
			if (temp >0 && temp > size)
				size = temp;
		}
		
		if (attributes.size() > heightlimit)
		{
			if (size == 0)
				size =2;
		}
			
	
	}
	
	public void update()
	{
		//setSize();
	}

	public String toString() {
		
		String output="";
		if (detailedOutput)
		{
			output ="<table border=\"0\">";
			output+=" <tr> ";
			output+= "<th> <h3>&lt;&lt; "+type+" >> <br>"+name+"</h3> </th>";
			output+=  " </tr> ";
			output+=" <tr> ";
			output+= "<td> <b>Attributes </b></td>";
			output+=  " </tr> ";
			for (String s : attributes)
			{
				output+=" <tr> ";
				output+= "<td> "+s+" </td>";
				output+=  " </tr> ";
			}
			
			output+=" </table>";
		}
		else
		{
			output+="<h3>&lt;&lt; "+type+" >> <br>"+name+"</h3>";
		}
		
		return output;
	}
	
/**
 * Pretty printed Name
 * @return
 */
	public String getName()
	{
		String res = name.replaceAll("&lt;", "<");
		res = res.replaceAll("<br>", "");
		
		return res;
	}
	
	/**
	 * Actual name value
	 */
	public String getRealName()
	{
		return name;
	}
	
	/**
	 * Set actual name value
	 */
	public void setRealName(String name)
	{
		this.name=name;
	}
	
	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public int getSize() {
		return size;
	}

	public NodeType getNodeType() {
		return type;
	}

	public boolean isDetailedOutput() {
		return detailedOutput;
	}

	public void setDetailedOutput(boolean detailedOutput) {
		this.detailedOutput = detailedOutput;
	}
}
