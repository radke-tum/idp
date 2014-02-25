package graph.operations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.lang.model.type.PrimitiveType;

import model.ModelBuilder;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import graph.model.MyEdge;
import graph.model.MyNode;

/**
 * Allows to filter the Nodes and Edges by a specific attribute
 * @author Luc
 *
 */
public class AttributeFilter {
	
	/*public static Graph<MyNode2, MyEdge2> filterNode(Graph<MyNode2, MyEdge2> graph, String attributeName, AttributeOperations op, Object RefValue) throws Exception
	{
		LinkedList<MyNode2> allNodes = new LinkedList<MyNode2>(graph.getVertices());
		
		for (MyNode2 currentNode : allNodes)
		{
			HashMap<String, Attribute> attributes = currentNode.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
				graph.removeVertex(currentNode);
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentNode.getNode())!=null)
					{
						PSSIFValue attrValue=attr.get(currentNode.getNode()).getOne();
						
						PrimitiveDataType currentType = (PrimitiveDataType) attr.getType();
						
						
						if (currentType.equals(PrimitiveDataType.BOOLEAN))
							result = BooleanEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DATE))
							result = DateEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DECIMAL))
							result = DecimalEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.INTEGER))
							result = IntegerEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.STRING))
							result = StringEval(attrValue, op, RefValue);
					}
					
					if (result == false)
					{
						graph.removeVertex(currentNode);
					}
				}
			}
		}
		return graph;
	}*/
	/**
	 * Filters the graph by a given condition on an attribute of a Node.
	 * All the Nodes which do not fulfill the condition will not be displayed later in the graph
	 * @param attributeName  the attribute name on which the Nodes should be filtered
	 * @param op the operation ( less, greater, equal,...) which should be executed on the attribute
	 * @param RefValue the given value to which the node attribute values should be compared to
	 * @throws Exception if the given condition contains a problem. Datatypes cannot be compared, Wrong data format,..
	 */
	public static void filterNode(String attributeName, AttributeOperations op, Object RefValue) throws Exception
	{
		LinkedList<MyNode> allNodes = ModelBuilder.getAllNodes();
		
		for (MyNode currentNode : allNodes)
		{
			HashMap<String, Attribute> attributes = currentNode.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
			{
				currentNode.setVisible(false);
			}
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentNode.getNode())!=null)
					{
						PSSIFValue attrValue=attr.get(currentNode.getNode()).getOne();
						
						PrimitiveDataType currentType = (PrimitiveDataType) attr.getType();
						
						
						if (currentType.equals(PrimitiveDataType.BOOLEAN))
							result = BooleanEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DATE))
							result = DateEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DECIMAL))
							result = DecimalEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.INTEGER))
							result = IntegerEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.STRING))
							result = StringEval(attrValue, op, RefValue);
					}
					
					if (result == false)
					{
						currentNode.setVisible(false);
					}
				}
			}
		}
	}
	
	/*public static Graph<MyNode2, MyEdge2> filterEdge(Graph<MyNode2, MyEdge2> graph, String attributeName, AttributeOperations op, Object RefValue) throws Exception
	{
		LinkedList<MyEdge2> allEdges = new LinkedList<MyEdge2>(graph.getEdges());
		
		for (MyEdge2 currentEdge : allEdges)
		{
			HashMap<String, Attribute> attributes = currentEdge.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
				graph.removeEdge(currentEdge);
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					//PSSIFOption<PSSIFValue>a = attr.get(currentEdge.getEdge());
					//System.out.println("No value "+a ==null);
					if (attr.get(currentEdge.getEdge())!=null)
					{
						PSSIFValue attrValue=attr.get(currentEdge.getEdge()).getOne();
						
						PrimitiveDataType currentType = (PrimitiveDataType) attr.getType();
						
						
						if (currentType.equals(PrimitiveDataType.BOOLEAN))
							result = BooleanEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DATE))
							result = DateEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DECIMAL))
							result = DecimalEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.INTEGER))
							result = IntegerEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.STRING))
							result = StringEval(attrValue, op, RefValue);
					}
					
					if (result == false)
						{
							graph.removeEdge(currentEdge);
						}
				}
			}
		}
		return graph;
	}*/
	
	/**
	 * Filters the graph by a given condition on an attribute of an Edge.
	 * All the Edges which do not fulfill the condition will not be displayed later in the graph
	 * @param attributeName  the attribute name on which the Edges should be filtered
	 * @param op the operation ( less, greater, equal,...) which should be executed on the attribute
	 * @param RefValue the given value to which the node attribute values should be compared to
	 * @throws Exception if the given condition contains a problem. Datatypes cannot be compared, Wrong data format,..
	 */
	public static void filterEdge(String attributeName, AttributeOperations op, Object RefValue) throws Exception
	{
		LinkedList<MyEdge> allEdges = ModelBuilder.getAllEdges();
		
		for (MyEdge currentEdge : allEdges)
		{
			HashMap<String, Attribute> attributes = currentEdge.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
			{
				//graph.removeEdge(currentEdge);
				currentEdge.setVisible(false);
			}
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentEdge.getEdge())!=null)
					{
						PSSIFValue attrValue=attr.get(currentEdge.getEdge()).getOne();
						
						PrimitiveDataType currentType = (PrimitiveDataType) attr.getType();
						
						
						if (currentType.equals(PrimitiveDataType.BOOLEAN))
							result = BooleanEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DATE))
							result = DateEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.DECIMAL))
							result = DecimalEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.INTEGER))
							result = IntegerEval(attrValue, op, RefValue);
						if (currentType.equals(PrimitiveDataType.STRING))
							result = StringEval(attrValue, op, RefValue);
					}
					
					if (result == false)
					{
						currentEdge.setVisible(false);
					}
				}
			}
		}
	}
	
	/**
	 * Test if a given operation is applicable to a given Attribute
	 * @param attr the Attribute on which the operation should be applied
	 * @param op the compare operation
	 * @return true, if possible, false if not
	 */
	private static boolean testPossibleOperation(Attribute attr, AttributeOperations op )
	{
		PrimitiveDataType currentType = (PrimitiveDataType) attr.getType();
		
		if (currentType.equals(PrimitiveDataType.BOOLEAN))
		{
			if (op.equals(AttributeOperations.EQUAL) || op.equals(AttributeOperations.NOT_EQUAL))
				return true;
			else
				return false;
		}
		
		if (currentType.equals(PrimitiveDataType.DATE))
		{
			return true;
		}
		
		if (currentType.equals(PrimitiveDataType.DECIMAL))
		{
			return true;
		}
		
		if (currentType.equals(PrimitiveDataType.INTEGER))
		{
			return true;
		}
		
		if (currentType.equals(PrimitiveDataType.STRING))
		{
			if (op.equals(AttributeOperations.EQUAL) || op.equals(AttributeOperations.NOT_EQUAL))
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	/**
	 * Evaluates the given condition
	 * @param attrValue the value of the attribute 
	 * @param op the operation which should be applied
	 * @param RefValue the value to which the attribute value should be compared to
	 * @return true if the condition is fulfilled, false otherwise 
	 */
	private static boolean BooleanEval (PSSIFValue attrValue, AttributeOperations op , Object RefValue)
	{
		boolean result = false;
		
		Boolean value = PrimitiveDataType.BOOLEAN.fromObject(attrValue.getValue()).asBoolean();
		Boolean refvalue;
		if (RefValue instanceof String)
		{
			Boolean castValue = Boolean.valueOf((String)RefValue);
			refvalue = PrimitiveDataType.BOOLEAN.fromObject(castValue).asBoolean();
		}
		else
			refvalue = PrimitiveDataType.BOOLEAN.fromObject(RefValue).asBoolean();
		
		switch (op)
		{
		case EQUAL: result = (value == refvalue) ; break;
		case NOT_EQUAL: result = (value != refvalue); break;
		default : result=false;
		}
		
		return result;
	}
	
	/**
	 * Evaluates the given condition
	 * @param attrValue the value of the attribute 
	 * @param op the operation which should be applied
	 * @param RefValue the value to which the attribute value should be compared to
	 * @return true if the condition is fulfilled, false otherwise 
	 */
	private static boolean StringEval (PSSIFValue attrValue, AttributeOperations op , Object RefValue)
	{
		boolean result = false;
		
		String value = PrimitiveDataType.STRING.fromObject(attrValue.getValue()).asString();
		String refvalue;
		//System.out.println(RefValue ==null);
		if (RefValue instanceof String)
		{
			String castValue = String.valueOf(RefValue);
			refvalue = PrimitiveDataType.STRING.fromObject(castValue).asString();
		}
		else
			refvalue = PrimitiveDataType.STRING.fromObject(RefValue).asString();
		
		
		
		switch (op)
		{
		case EQUAL: result = (value.equals(refvalue)) ; break;
		case NOT_EQUAL: result = !(value.equals(refvalue)); break;
		default : result=false;
		}
		
		return result;
	}
	
	/**
	 * Evaluates the given condition
	 * @param attrValue the value of the attribute 
	 * @param op the operation which should be applied
	 * @param RefValue the value to which the attribute value should be compared to
	 * @return true if the condition is fulfilled, false otherwise 
	 */
	private static boolean DateEval (PSSIFValue attrValue, AttributeOperations op , Object RefValue) throws ParseException
	{
		boolean result = false;
		
		Date value = PrimitiveDataType.DATE.fromObject(attrValue.getValue()).asDate();
		Date refvalue;
		if (RefValue instanceof String)
		{
			Date castValue = DateFormat.getInstance().parse((String)RefValue);
			refvalue = PrimitiveDataType.DATE.fromObject(castValue).asDate();
		}
		else
			refvalue = PrimitiveDataType.DATE.fromObject(RefValue).asDate();
		
		switch (op)
		{
		case EQUAL: result = value == refvalue; break;
		case NOT_EQUAL: result = value != refvalue; break;
		case GREATER: result = value.compareTo(refvalue)>0; break;
		case GREATER_EQUAL: result = value.compareTo(refvalue)>=0; break;
		case LESS: result = value.compareTo(refvalue)<0; break;
		case LESS_EQUAL: result = value.compareTo(refvalue)<=0; break;
		}
		
		return result;
	}
	
	/**
	 * Evaluates the given condition
	 * @param attrValue the value of the attribute 
	 * @param op the operation which should be applied
	 * @param RefValue the value to which the attribute value should be compared to
	 * @return true if the condition is fulfilled, false otherwise 
	 */
	private static boolean DecimalEval (PSSIFValue attrValue, AttributeOperations op , Object RefValue)
	{
		boolean result = false;
		
		BigDecimal value = PrimitiveDataType.DECIMAL.fromObject(attrValue.getValue()).asDecimal();
		BigDecimal refvalue;
		if (RefValue instanceof String)
		{
			Float castValue = Float.valueOf((String)RefValue);
			refvalue = PrimitiveDataType.DECIMAL.fromObject(castValue).asDecimal();
		}
		else
			refvalue = PrimitiveDataType.DECIMAL.fromObject(RefValue).asDecimal();
		
		switch (op)
		{
		case EQUAL: result = value == refvalue; break;
		case NOT_EQUAL: result = value != refvalue; break;
		case GREATER: result = value.compareTo(refvalue)>0; break;
		case GREATER_EQUAL: result = value.compareTo(refvalue)>=0; break;
		case LESS: result = value.compareTo(refvalue)<0; break;
		case LESS_EQUAL: result = value.compareTo(refvalue)<=0; break;

		default : result=false;
		}
		
		return result;
	}
	
	/**
	 * Evaluates the given condition
	 * @param attrValue the value of the attribute 
	 * @param op the operation which should be applied
	 * @param RefValue the value to which the attribute value should be compared to
	 * @return true if the condition is fulfilled, false otherwise 
	 */
	private static boolean IntegerEval (PSSIFValue attrValue, AttributeOperations op , Object RefValue)
	{
		boolean result = false;
		
		BigInteger value = PrimitiveDataType.INTEGER.fromObject(attrValue.getValue()).asInteger();
		BigInteger refvalue;
		if (RefValue instanceof String)
		{
			Integer castValue = Integer.valueOf((String)RefValue);
			refvalue = PrimitiveDataType.INTEGER.fromObject(castValue).asInteger();
		}
		else
			refvalue = PrimitiveDataType.INTEGER.fromObject(RefValue).asInteger();
		
		switch (op)
		{
		case EQUAL: result = value == refvalue; break;
		case NOT_EQUAL: result = value != refvalue; break;
		case GREATER: result = value.compareTo(refvalue)>0; break;
		case GREATER_EQUAL: result = value.compareTo(refvalue)>=0; break;
		case LESS: result = value.compareTo(refvalue)<0; break;
		case LESS_EQUAL: result = value.compareTo(refvalue)<=0; break;

		default : result=false;
		}
		
		return result;
	}
}
