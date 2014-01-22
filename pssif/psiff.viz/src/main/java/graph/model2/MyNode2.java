package graph.model2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.util.PSSIFValue;


public class MyNode2{

	private Node node;
	private String name;
	//private List<String> attributes ;
	private int size;
	private MyNodeType type;
	private boolean detailedOutput;
	//public static int idcounter;
	
	private static int limit = 15;
	private static int heightlimit = 2;
	
	
	public MyNode2(Node node, MyNodeType type) {
		this.node = node;
		this.type = type;
		
		Attribute nodeName = type.getType().findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		
		if (nodeName.get(node)!=null)
		{
			PSSIFValue value = nodeName.get(node).getOne();
			name = value.asString();
			
		}
		else
			name ="blabla";
		
		//calcAttr();
	}
	
	private List<String> calcAttr()
	{
		List<String> attributes = new LinkedList<String>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			
			PSSIFValue value=null;
			
			if (current.get(node)!=null && current.get(node).isOne())
				value = current.get(node).getOne();
			
			String attrValue="";
			if (value !=null)
				attrValue = (String) value.getValue();
			String attrUnit = current.getUnit().getName();
			
			String res = attrName+" = "+attrValue+" : "+attrUnit;
			
			if (current.get(node)!=null)
				attributes.add(res);
		}
		
		return attributes;
	}

	
	/*private void setSize()
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
			
	
	}*/
	
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
			output+= "<th> <h3>&lt;&lt; "+type.getName()+" >> <br>"+name+"</h3> </th>";
			output+=  " </tr> ";
			output+=" <tr> ";
			output+= "<td> <b>Attributes </b></td>";
			output+=  " </tr> ";
			for (String s : calcAttr())
			{
				output+=" <tr> ";
				output+= "<td> "+s+" </td>";
				output+=  " </tr> ";
			}
			
			output+=" </table>";
		}
		else
		{
			output+="<h3>&lt;&lt; "+type.getName()+" >> <br>"+name+"</h3>";
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
		return calcAttr();
	}

	/*public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}*/

	public int getSize() {
		return size;
	}

	public MyNodeType getNodeType() {
		return type;
	}

	public boolean isDetailedOutput() {
		return detailedOutput;
	}

	public void setDetailedOutput(boolean detailedOutput) {
		this.detailedOutput = detailedOutput;
	}
	
	public boolean compareTo (Node n)
	{
		if (this.node.equals(n))
			return true;
		else
			return false;
	}

	public Node getNode() {
		return node;
	}
}
