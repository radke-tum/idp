package graph.operations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

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
	/**
	 * Mapping from Condition String to all Condition Data
	 */
	private static HashMap<String,ConditionContainer> nodeConditions;
	/**
	 * Mapping from Condition String to all Condition Data
	 */
	private static HashMap<String,ConditionContainer> edgeConditions;
	
	
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
		/*init();
		
		LinkedList<MyNode> allNodes = ModelBuilder.getAllNodes();
		LinkedList<MyNode> invisibleNodes = new LinkedList<MyNode>();
		
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
						if (attr.get(currentNode.getNode()).isOne())
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
						if (attr.get(currentNode.getNode()).isMany())
						{
							throw new NullPointerException("Don't know what do to do with many values in one Attribut");
						}
						
						if (attr.get(currentNode.getNode()).isNone())
						{
							result=false;
						}
					}
					
					if (result == false)
					{
						currentNode.setVisible(false);
						invisibleNodes.add(currentNode);
					}
				}
			}
		}*/

		filterNodeWithResult(attributeName, op, RefValue, false);
		

	}
	
	private static void filterNodeWithResult(String attributeName, AttributeOperations op, Object RefValue, boolean visiblity) throws Exception
	{
		init();
		
		LinkedList<MyNode> allNodes = ModelBuilder.getAllNodes();
		
		for (MyNode currentNode : allNodes)
		{
			HashMap<String, Attribute> attributes = currentNode.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
			{
				currentNode.setVisible(visiblity);
			}
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentNode.getNode())!=null)
					{
						if (attr.get(currentNode.getNode()).isOne())
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
						if (attr.get(currentNode.getNode()).isMany())
						{
							throw new NullPointerException("Don't know what do to do with many values in one Attribut");
						}
						
						if (attr.get(currentNode.getNode()).isNone())
						{
							result=false;
						}
					}
					
					if (result == false)
					{
						currentNode.setVisible(visiblity);
					}
				}
			}
		}
	}
	
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
		/*init();
		
		LinkedList<MyEdge> allEdges = ModelBuilder.getAllEdges();
		//LinkedList<MyEdge> inVisibleEdges = new LinkedList<MyEdge>();
		
		for (MyEdge currentEdge : allEdges)
		{
			HashMap<String, Attribute> attributes = currentEdge.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
			{
				currentEdge.setVisible(false);
			}
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentEdge.getEdge())!=null)
					{
						if (attr.get(currentEdge.getEdge()).isOne())
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
						
						if (attr.get(currentEdge.getEdge()).isNone())
						{
							result = false;
						}
						
						if (attr.get(currentEdge.getEdge()).isMany())
						{
							throw new NullPointerException("Don't know what do to do with many values in one Attribut");
						}
							
					}
					
					if (result == false)
					{
						currentEdge.setVisible(false);
					//	inVisibleEdges.add(currentEdge);
					}
				}
			}
		}*/
		
		filterEdgeWithResult(attributeName, op, RefValue, false);
	}
	
	private static void filterEdgeWithResult(String attributeName, AttributeOperations op, Object RefValue, boolean visiblity) throws Exception
	{
		init();
		
		LinkedList<MyEdge> allEdges = ModelBuilder.getAllEdges();
		
		for (MyEdge currentEdge : allEdges)
		{
			HashMap<String, Attribute> attributes = currentEdge.getAttributesHashMap();
			
			Attribute attr = attributes.get(attributeName);
			
			if (attr== null)
			{
				currentEdge.setVisible(visiblity);
			}
			else
			{
				if (testPossibleOperation(attr, op))
				{
					boolean result = false;
					
					if (attr.get(currentEdge.getEdge())!=null)
					{
						if (attr.get(currentEdge.getEdge()).isOne())
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
						
						if (attr.get(currentEdge.getEdge()).isNone())
						{
							System.out.println("Found no value of Attribute");
							result = false;
						}
						
						if (attr.get(currentEdge.getEdge()).isMany())
						{
							throw new NullPointerException("Don't know what do to do with many values in one Attribut");
						}
							
					}
					
					if (result == false)
					{
						currentEdge.setVisible(visiblity);
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
	
	private static void init()
	{
		if (edgeConditions==null)
			edgeConditions = new HashMap<String, ConditionContainer>();
		if (nodeConditions ==null)
			nodeConditions = new HashMap<String, ConditionContainer>();
	}
	
	public static LinkedList<String> getAllNodeConditions()
	{
		LinkedList<String> res = new LinkedList<String>();
		
		if (nodeConditions!=null)
		{
			String[] s = nodeConditions.keySet().toArray(new String[0]);
			
			res.addAll(Arrays.asList(s));
		}
		return res;
	}
	
	public static LinkedList<String> getAllEdgeConditions()
	{
		LinkedList<String> res = new LinkedList<String>();
		
		if (edgeConditions!=null)
		{
			String[] s = edgeConditions.keySet().toArray(new String[0]);
			
			res.addAll(Arrays.asList(s));
		}
		return res;
	}
	
	public static void applyNodeCondition(String condition) throws Exception
	{
		System.out.println("Entries "+nodeConditions.size());
		System.out.println("Got ||"+condition);
		ConditionContainer c = nodeConditions.get(condition);
		if (c!=null)
		{
			System.out.println("Found correct container");
			filterNode(c.attributeName, c.operation, c.refValue);
		}
		else
		{
			System.out.println("Could not find correct container");
		}
	}
	
	public static void applyEdgeCondition(String condition) throws Exception
	{
		//System.out.println("Entries "+edgeConditions.size());
		System.out.println("Got ||"+condition);
		ConditionContainer c = edgeConditions.get(condition);
		if (c!=null)
		{
			System.out.println("found Container");
			filterEdge(c.attributeName, c.operation, c.refValue);
		}
	}
	
	public static void removeNodeCondition(String condition) throws Exception
	{
		init();
		undoNodeCondition(condition);
		nodeConditions.remove(condition);
	}
	
	public static void removeEdgeCondition(String condition) throws Exception
	{
		init();
		undoEdgeCondition(condition);
		edgeConditions.remove(condition);
	}
	
	public static void undoEdgeCondition (String condition) throws Exception
	{
		ConditionContainer c = edgeConditions.get(condition);
		filterEdgeWithResult(c.attributeName, c.operation, c.refValue, true);
		
		applyAllOtherEdgeConditions(condition);
		
	}
	
	public static void undoNodeCondition (String condition) throws Exception
	{
		ConditionContainer c = nodeConditions.get(condition);
		filterNodeWithResult(c.attributeName, c.operation, c.refValue, true);
		
		applyAllOtherNodeConditions(condition);
	}
	
	private static void applyAllOtherNodeConditions (String condition) throws Exception
	{
		for (Entry<String, ConditionContainer> e : nodeConditions.entrySet())
		{
			if (!e.getKey().equals(condition))
			{
				ConditionContainer c = e.getValue();
				filterNode(c.attributeName, c.operation, c.refValue);
			}
		}
	}
	
	private static void applyAllOtherEdgeConditions (String condition) throws Exception
	{
		for (Entry<String, ConditionContainer> e : edgeConditions.entrySet())
		{
			if (!e.getKey().equals(condition))
			{
				ConditionContainer c = e.getValue();
				filterEdge(c.attributeName, c.operation, c.refValue);
			}
		}
	}
	
	public static String addNodeCondition (String attributeName, AttributeOperations op, Object RefValue)
	{
		init();
		String condition = attributeName+" "+op.getName()+" "+String.valueOf(RefValue);
		ConditionContainer c = new ConditionContainer(attributeName, op, RefValue);
		nodeConditions.put(condition, c);

		
		return condition;
	}
	
	public static String addEdgeCondition (String attributeName, AttributeOperations op, Object RefValue)
	{
		init();
		String condition = attributeName+" "+op.getName()+" "+String.valueOf(RefValue);
		ConditionContainer c = new ConditionContainer(attributeName, op, RefValue);
		edgeConditions.put(condition, c);
		
		return condition;
	}
	
	private static class ConditionContainer{
		public String attributeName;
		public AttributeOperations operation;
		public Object refValue;
		
		public ConditionContainer(String attributeName, AttributeOperations op, Object RefValue)
		{
			this.attributeName = attributeName;
			this.operation = op;
			this.refValue = RefValue;
		}
	}
}
