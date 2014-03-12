package graph.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;

/**
 * A Data container for the Nodes the PSS-IF Model
 * Helps to manage the visualization/modification of the Node
 * @author Luc
 *
 */
public class MyNode{
	private Node node;
	private double sizeheight;
	private double sizewidth;
	private MyNodeType type;
	private boolean detailedOutput;
	private boolean visible;
	private boolean collapseNode;
	
	private static int limit = 5;
	
	public MyNode(Node node, MyNodeType type) {
		this.node = node;
		this.type = type;
		this.visible = true;
		this.collapseNode = false;
		
		this.update();
	}
	
	/**
	 * Get all the Attributes from this node
	 * @return List with the attributes. Format : Name = Value in (Unit) Datatype 
	 */
	private List<String> calcAttr()
	{
		List<String> attributes = new LinkedList<String>();
		Attribute nodeName = type.getType().findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			if (!nodeName.equals(current))
			{
				String attrName = current.getName();
				
				PSSIFValue value=null;
				
				if (current.get(node)!=null && current.get(node).isOne())
					value = current.get(node).getOne();
				
				String attrValue="";
				if (value !=null)
					attrValue = String.valueOf(value.getValue());
				String attrUnit = current.getUnit().getName();
				
				String res;
				
				if (attrUnit.equals("none"))
					res = attrName+" = "+attrValue+" : "+((PrimitiveDataType)current.getType()).getName();
				else
					res = attrName+" = "+attrValue+" in "+attrUnit+ " : "+((PrimitiveDataType)current.getType()).getName();
				
				if (current.get(node)!=null && attrValue.length()>0)
					attributes.add(res);
			}
		}
		
		return attributes;
	}
	
	/**
	 * Get a list with all the attributes from this Node
	 * @return A list which contains a list with all the attribute information. Information Order in the list : Name, Value, Unit, Datatype
	 */
	public LinkedList<LinkedList<String>> getAttributes()
	{
		LinkedList<LinkedList<String>> attributes = new LinkedList<LinkedList<String>>();
		
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			LinkedList<String> currentAttr = new LinkedList<String>();
			
			String attrName = current.getName();
			
			currentAttr.add(attrName);
			
			PSSIFValue value=null;
			
			if (current.get(node)!=null && current.get(node).isOne())
				value = current.get(node).getOne();
			
			String attrValue="";
			if (value !=null)
				attrValue = String.valueOf(value.getValue());
			
			currentAttr.add(attrValue);
			String attrUnit = current.getUnit().getName();
			currentAttr.add(attrUnit);
			
			currentAttr.add(((PrimitiveDataType)current.getType()).getName());
			
			attributes.add(currentAttr);
		}
		
		return attributes;
	}
	
	/**
	 * Update the value of a given attribute
	 * @param attributeName
	 * @param value
	 * @return true if everything went fine, otherwise false
	 */
	public boolean updateAttribute(String attributeName, Object value)
	{		
		DataType attrType = type.getType().findAttribute(attributeName).getType();
		
		if (attrType.equals(PrimitiveDataType.BOOLEAN))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.BOOLEAN.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.DATE))
		{
			try 
			{
				Date tmp = parseDate((String) value);
				
				PSSIFValue res = PrimitiveDataType.DATE.fromObject(tmp);
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
			}
			catch (IllegalArgumentException e)
			{
				System.out.println(e.getMessage());
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.DECIMAL))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.DECIMAL.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.INTEGER))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.INTEGER.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));

			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.STRING))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.STRING.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));

			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		this.update();
		return true;
	}
	
	/**
	 * We only accepts certain date formats. Checks different date formats and returns a date object
	 * @param dateInString
	 * @return a date object, if the given String is not coded in one of the given date formats, null is returned
	 */
	private Date parseDate(String dateInString)
	{
		SimpleDateFormat formatter;
	
		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd/M/yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd-M-yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd.MM.yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		
		try {
			formatter = new SimpleDateFormat("dd.M.yyyy");
			return formatter.parse(dateInString);
		} catch (ParseException e) { }
		return null;
		
	}
	
	/**
	 * Get a HashMap with all the Attributes from this node
	 * @return A Mapping from Attributename to Attrbiute
	 */
	public HashMap<String, Attribute> getAttributesHashMap()
	{
		 HashMap<String, Attribute> res = new  HashMap<String, Attribute>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			
			res.put(attrName, current);
		}
		
		return res;
	}

	/**
	 * define the width and height of the Node during the graph viz, according to the node information (name, type,...)
	 */
	private void setSize()
	{
		
		int temp = findName().length() / limit;
		
		if (temp >0)
			sizewidth = temp;
		
		temp = (type.getName().length()+6)/ limit;
		
		if (temp >sizewidth)
			sizewidth = temp;
		
		List<String> attr = calcAttr();
		for (String s : attr)
		{
			temp = s.length() / limit;
			
			if (temp > sizewidth)
				sizewidth = temp;
		}
				
		sizeheight = attr.size();

		if (sizeheight==1)
			sizeheight= sizeheight+1.5;
		
		//System.out.println(getRealName()+"|| width "+sizewidth+" height "+sizeheight);	
	
	}
	
	/**
	 * After a change of the Node information, the visualization might be changed. Do an update
	 */
	public void update()
	{
		setSize();
	}
	
	/**
	 * Get all the Informations about the Node. Should only be used in the GraphVisualization
	 * @param details true show all details, false only name
	 * @return a HTML String with all the node informations
	 */
	public String getNodeInformations(boolean details)
	{
		String output="";
		if (details)
		{
			output ="<table border=\"0\">";
			output+=" <tr> ";
			output+= "<th> <h3>&lt;&lt; "+type.getName()+" >> <br>"+findName()+"</h3> </th>";
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
			output+="<h3>&lt;&lt; "+type.getName()+" >> <br>"+findName()+"</h3>";
		}
		
		return output;
	}
	
/**
 * Pretty printed Name
 * @return a html name
 */
	public String getName()
	{
		String res = findName().replaceAll("&lt;", "<");
		res = res.replaceAll("<br>", "");
		
		return res;
	}
	
	/**
	 * Actual name value
	 * @return the name
	 */
	public String getRealName()
	{
		return findName();
	}
	
	/**
	 * Get the name from the Node object
	 * @return the actual name or "Name not available" if the name was not defined
	 */
	private String findName()
	{
		String name="";
		// find the name of the Node
		Attribute nodeName = type.getType().findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		
		if (nodeName.get(node)!=null)
		{
			PSSIFValue value =null;
			if (nodeName.get(node).isOne())
			{
				value = nodeName.get(node).getOne();
				name = value.asString();
			}
			if (nodeName.get(node).isNone())
			{
				name ="Name not available";
			}
		}
		else
			name ="Name not available";
		
		return name;
	}

	public double getHeight() {
		return sizeheight;
	}
	
	public double getWidth() {
		return sizewidth;
	}

	public MyNodeType getNodeType() {
		return type;
	}

	public boolean isDetailedOutput() {
		return detailedOutput;
	}
	
	/**
	 * Defines if all the attributes and additional information is shown in the GUI
	 * @param detailedOutput
	 */
	public void setDetailedOutput(boolean detailedOutput) {
		this.detailedOutput = detailedOutput;
	}

	public Node getNode() {
		return node;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isCollapseNode() {
		return collapseNode;
	}

	public void setCollapseNode(boolean collapseNode) {
		this.collapseNode = collapseNode;
	}
}
