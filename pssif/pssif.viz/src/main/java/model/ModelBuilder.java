package model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyJunctionNode;
import graph.model.MyJunctionNodeType;
import graph.model.MyJunctionNodeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.model.MyNodeTypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pssif.mainProcesses.CompairsonProcess;
import org.pssif.matchingLogic.ExactMatcher;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;

/**
 * Builds out of a Model and an MetaModel a Model which can be displayed as Graph and Matrix
 * @author Luc
 *
 */
public class ModelBuilder {
	
	private static MyModelContainer activeModel;
	//private static LinkedList<MyModelContainer> activeModels;
	//private static boolean mergerOn=true;
	private static Metamodel metaModel = PSSIFCanonicMetamodelCreator.create();
	private static HashMap<MyPair, LinkedList<MyEdgeType>> possibleMappings;
	
	/**
	 * Add a new Model and MetaModel. The new Model might be merged with another
	 * existing Model
	 * 
	 * @param meta
	 * @param model
	 */
	public static void addModel(Metamodel Pmeta, Model Pmodel) {
		// check if we have to merge the model with an existing
		if (activeModel == null) {
			MyModelContainer newModel = new MyModelContainer(Pmodel, Pmeta);
			activeModel = newModel;
		}
		/**
		 * @author: Andreas
		 */
		else {
			//TODO extract to main class in consistency package (together with the two other methods for popup creation
			if (openConsistencyPopUp()) {
				
				List<MatchMethod> matchMethods = openChooseMatchingMethodsPopup();
				
				CompairsonProcess.main(activeModel.getModel(), Pmodel, Pmeta, matchMethods);
			} else {
				ModelMerger merger = new ModelMerger();
				Model mergedModel = merger.mergeModels(activeModel.getModel(),
						Pmodel, Pmeta);

				MyModelContainer newModel = new MyModelContainer(mergedModel,
						Pmeta);
				activeModel = newModel;
			}

		}
	}

	/**
	 * @author: Andreas
	 * @return a boolean that says whether the user wants to merge the newly
	 *         imported model with the old one
	 */
	private static boolean openConsistencyPopUp() {

		// TODO: Ask user if he wants to just import the model into the
		// workspace or if he wants to merge the original model and the newly
		// imported one
		boolean result = false;
		
		//TODO: Ask user if he wants to merge

		result = true;


		return result;
	}
	
	/**
	 * @author: Andreas
	 * @return the set of matchMethods which shall be applied to the data
	 */
	private static List<MatchMethod> openChooseMatchingMethodsPopup(){
		
		List<MatchMethod> result = new LinkedList<MatchMethod>();
		
		//TODO: Open Dialog here and ask the user which metrics he wants
		
		//TODO Remove after testing
		result.add(new ExactMatcher(MatchingMethods.EXACT_STRING_MATCHING, true, 1.0));
		
		return result;
	}
	
	
	public static void resetModel()
	{
		activeModel = null;
	}

	/**
	 * get all Nodes from the Model
	 * @return List with the Nodes
	 */
	public static LinkedList<MyNode> getAllNodes()
	{
		if (activeModel !=null)
			return activeModel.getAllNodes();
		else
			return new LinkedList<MyNode>();	
	}
	
	/**
	 * get all JunctionNodes from the Model
	 * @return List with the JunctionNodes
	 */
	public static LinkedList<MyJunctionNode> getAllJunctionNodes()
	{
		if (activeModel !=null)
			return activeModel.getAllJunctionNodes();
		else
			return new LinkedList<MyJunctionNode>();	
	}
	
	/**
	 * get all Edges from the Model
	 * @return List with the Edges
	 */
	public static LinkedList<MyEdge> getAllEdges()
	{
		if (activeModel !=null)
			return activeModel.getAllEdges();
		else
			return new LinkedList<MyEdge>();
	}
	

	
	/**
	 * get all Node Types from the Model
	 * @return the NodeTypes object
	 */
	public static MyNodeTypes getNodeTypes() {
		if (activeModel !=null)
			return activeModel.getNodeTypes();
		else
		{
			return new MyNodeTypes(new HashSet<MyNodeType>());
		}
	}
	
	/**
	 * get all JunctionNode Types from the Model
	 * @return the MyJunctionNodeTypes object
	 */
	public static MyJunctionNodeTypes getJunctionNodeTypes() {
		if (activeModel !=null)
			return activeModel.getJunctionNodeTypes();
		else
		{
			return new MyJunctionNodeTypes(new HashSet<MyJunctionNodeType>());
		}
	}
	
	/**
	 * get all Edge Types from the Model
	 * @return the EdgeTypes object
	 */
	public static MyEdgeTypes getEdgeTypes() {
		if (activeModel !=null)
			return activeModel.getEdgeTypes();
		else
		{
			return new MyEdgeTypes(new HashSet<MyEdgeType>());
		}
	}
	
	/**
	 * add an Edge which is added during a collapse operation
	 * @param newEdge the new Edge 
	 */
	public static void addCollapserEdge(MyEdge newEdge)
	{
		if (activeModel !=null)
		{
			newEdge.setCollapseEdge(true);
			activeModel.addEdge(newEdge);
		}
	}
	
	/**
	 * remove an Edge which was added during a collapse operation
	 * @param edge the edge in question
	 */
	public static void removeCollapserEdge(MyEdge edge)
	{
		if (activeModel !=null)
		{
			activeModel.removeCollapserEdge(edge);
		}
	}
	
	public static void printVisibleStuff ()
	{
		System.out.println("------visible Nodes----------");
		for (MyNode n : getAllNodes())
		{
			if (n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
		System.out.println("------invisible Nodes----------");
		for (MyNode n : getAllNodes())
		{
			if (!n.isVisible())
				System.out.println(n.getRealName());
		}
		System.out.println("--------------------------");
	}
	
	/**
	 * Add a new Node which was created through the Gui
	 * @param nodeName The name of the new Node
	 * @param type The type of the new Node
	 */
	public static void addNewNodeFromGUI (String nodeName, MyNodeType type)
	{
		if (activeModel !=null)
		{
			activeModel.addNewNodeFromGUI(nodeName, type);
		}
	}
	
	/**
	 * Add a new Edge which was created through the Gui
	 * @param source The start Node of the Edge
	 * @param destination The destination Node of the Edge
	 * @param edgetype The type of the Edge
	 * @param directed should the new edge be directed
	 * @return true if the add operation was successful, false otherwise
	 */
	public static boolean addNewEdgeGUI(MyNode source, MyNode destination, MyEdgeType edgetype, boolean directed)
	{
		if (activeModel !=null)
		{
			return activeModel.addNewEdgeGUI(source, destination, edgetype, directed);
		}
		else
			return false;
	}
	
	public static Metamodel getMetamodel()
	{
		return metaModel;
	}
	
	public static Model getModel()
	{
		if (activeModel !=null)
		{
			return activeModel.getModel();
		}
		else
			return null;
	}
	
	
	private static void calcPossibleEdges()
	{
		//System.out.println("Call");
		if (possibleMappings== null)
		{
			possibleMappings = new HashMap<ModelBuilder.MyPair, LinkedList<MyEdgeType>>();
		}
		
		for (NodeType start :getMetamodel().getNodeTypes())
		{
			for (NodeType end :getMetamodel().getNodeTypes())
			{
				for (EdgeType et: getMetamodel().getEdgeTypes())
				{
					PSSIFOption<ConnectionMapping> tmp = et.getMapping(start, end);
					
					
					/*if (et.getName().equals(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS) && tmp!=null)
					{
						if ((tmp.isOne() || tmp.isMany()) && (end.getName().equals("Activity") || start.getName().equals("Activity")))
						{
							System.out.println(start.getName()+" to "+end.getName());
							//System.out.println("None "+tmp.isNone());
							System.out.println("One "+tmp.isOne());
							System.out.println("Many "+tmp.isMany());
							System.out.println("-----------------------------");
						}
					}*/
					if (tmp!=null && tmp.isOne())
					{
						//ConnectionMapping mapping = tmp.getOne();
						MyPair p = MyPair.getInsance(start,end);
						LinkedList<MyEdgeType> data = possibleMappings.get(p);
						
						if (data ==null)
						{
							data = new LinkedList<MyEdgeType>();
						}
						MyEdgeType value = getEdgeTypes().getValue(et.getName());
						data.add(value);
						
						/*if (value.getName().equals(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION) )
						{
							System.out.println(start.getName() +"--"+ end.getName());
						}
						
						if (value.getParentType()!=null)
						{
							if (value.getParentType().getName().equals(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION))
								System.out.println(start.getName() +"--"+ end.getName());
						}*/
						possibleMappings.put(p, data);
					}
					
				}
			}
		}
	}
	
	public static LinkedList<MyEdgeType> getPossibleEdges(NodeType start, NodeType end) 
	{
		if (possibleMappings== null)
			calcPossibleEdges();
		
		LinkedList<MyEdgeType> res = possibleMappings.get(MyPair.getInsance(start, end));
		
		if (res ==null)
		{
			res = new LinkedList<MyEdgeType>();
		}
		
		return res;
	}
	
	private static class MyPair
	{
		private NodeType start;
		private NodeType end;
		
		private static LinkedList<MyPair> values;
		
		private MyPair (NodeType start, NodeType end)
		{
			this.start = start;
			this.end = end;
		}
		
		public static MyPair getInsance(NodeType start, NodeType end)
		{
			if (values ==null)
				values = new LinkedList<ModelBuilder.MyPair>();
			
			MyPair mp = existsAlready(start, end);
			if (mp!=null)
				return mp;
			else
			{
				mp = new MyPair(start, end);
				values.add(mp);
				return mp;
			}
		}
		
		private static MyPair existsAlready (NodeType start, NodeType end)
		{
			for (MyPair v: values)
			{
				if (v.start.getName().equals(start.getName()) && v.end.getName().equals(end.getName()))
					return v;
			}
			
			return null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((end == null) ? 0 : end.getName().hashCode());
			result = prime * result + ((start == null) ? 0 : start.getName().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyPair other = (MyPair) obj;
			if (end == null) {
				if (other.end != null)
					return false;
			} 
			else if (!end.equals(other.end))
				return false;
			if (start == null) {
				if (other.start != null)
					return false;
			} else if (!start.equals(other.start))
				return false;
			return true;
		}
	}
}
