package org.pssif.main;

import java.util.Iterator;
import java.util.Set;

import org.pssif.consistencyDataStructures.ConsistencyData;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class StartConsistencyCheck {

	/**
	 * These are the subclasses of PSIFFDevArtifacts that are checked for
	 * consistency
	 */
	private final String[] PSIFFDevArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY,
			PSSIFCanonicMetamodelCreator.N_REQUIREMENT,
			PSSIFCanonicMetamodelCreator.N_USE_CASE,
			PSSIFCanonicMetamodelCreator.N_TEST_CASE,
			PSSIFCanonicMetamodelCreator.N_VIEW,
			PSSIFCanonicMetamodelCreator.N_EVENT,
			PSSIFCanonicMetamodelCreator.N_ISSUE,
			PSSIFCanonicMetamodelCreator.N_DECISION,
			PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT };

	/**
	 * These are the subclasses of PSIFFSolArtifacts that are checked for
	 * consistency
	 */
	private final String[] PSIFFSolArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_BLOCK,
			PSSIFCanonicMetamodelCreator.N_FUNCTION,
			PSSIFCanonicMetamodelCreator.N_ACTIVITY,
			PSSIFCanonicMetamodelCreator.N_STATE,
			PSSIFCanonicMetamodelCreator.N_ACTOR,
			PSSIFCanonicMetamodelCreator.N_SERVICE,
			PSSIFCanonicMetamodelCreator.N_SOFTWARE,
			PSSIFCanonicMetamodelCreator.N_HARDWARE,
			PSSIFCanonicMetamodelCreator.N_MECHANIC,
			PSSIFCanonicMetamodelCreator.N_ELECTRONIC,
			PSSIFCanonicMetamodelCreator.N_MODULE };

	Model originalModel, newModel;
	Metamodel metaModel;

	ConsistencyData consistencyData;

	private PSSIFOption<Node> nodesOriginalModel;

	public static void main(Model originalModel, Model newModel,
			Metamodel metaModel) {
		new StartConsistencyCheck(originalModel, newModel, metaModel);
	}

	public StartConsistencyCheck(Model originalModel, Model newModel,
			Metamodel metaModel) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModel = metaModel;

		this.consistencyData = new ConsistencyData();

		this.startTypeAndNodeIteration();
	}

	public void startTypeAndNodeIteration() {
		NodeType rootNodeType = metaModel.getNodeType(
				PSSIFConstants.ROOT_NODE_TYPE_NAME).getOne();

		nodesOriginalModel = rootNodeType.apply(originalModel, true);

		if (nodesOriginalModel.isNone()) {
			// TODO: Alert the user that there are no nodes in the orignal model
			// to merge with the new one
			System.out.println("no nodes in the original model");
		} else {
			if (nodesOriginalModel.isOne()) {
				System.out.println("one node in the original model");
			} else if (nodesOriginalModel.isMany()) {
				System.out.println("many nodes in the original model");
			} else {
				throw new RuntimeException(
						"This should never have happened. Maybe the structure of the root node type was changed");
			}
			typeIteration();
		}
	}

	public void typeIteration() {
		for (int i = 0; i < PSIFFDevArtifactSubClasses.length; i++) {
			iterateNodesOfType(PSIFFDevArtifactSubClasses[i], false);
		}

		for (int i = 0; i < PSIFFSolArtifactSubClasses.length; i++) {
			iterateNodesOfType(PSIFFSolArtifactSubClasses[i], false);
		}
		
		iterateNodesOfType(PSSIFCanonicMetamodelCreator.N_DEV_ARTIFACT, true);
		iterateNodesOfType(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT, true);
		iterateNodesOfType(PSSIFConstants.ROOT_NODE_TYPE_NAME,true) ;
	}

	public void iterateNodesOfType(String type, boolean includeSubtypes) {
		
		int nodeCount = 0;
		
		NodeType actType;
		PSSIFOption<Node> actNodesOriginalModel;
		PSSIFOption<Node> actNodesNewModel;
		
		actType = metaModel.getNodeType(type)
				.getOne();

		actNodesOriginalModel = actType.apply(originalModel, includeSubtypes);

		if (actNodesOriginalModel.isNone()) {
			System.out
					.println("There are no nodes of the type "
							+ actType.getName()
							+ " in the original model. Continuing with the next type.");
		} else {
			if (actNodesOriginalModel.isOne()) {
				System.out
						.println("There is one node of the type "
								+ actType.getName()
								+ " in the original model. Starting the matching for this node.");
				
				Node tempNodeOrigin = actNodesOriginalModel.getOne();
				
				matchNodeWithNewModel(tempNodeOrigin, actType);
				
				/*System.out.println(findName(actType,tempNodeOrigin));
				nodeCount++;*/

			} else {
				System.out
						.println("There are many nodes of the type "
								+ actType.getName()
								+ " in the original model. Starting the matching for these nodes.");
				
				Set<Node> tempNodesOrigin = actNodesOriginalModel.getMany();
				
				Iterator<Node> tempNodeOrigin = tempNodesOrigin.iterator();
				
				while(true){
					
					matchNodeWithNewModel(tempNodeOrigin.next(), actType);
					
					/*System.out.println(findName(actType,tempNodeOrigin.next()));
					nodeCount++;*/
					
					if(!tempNodeOrigin.hasNext()){
						break;
					}
				}
				
			}
		}
		System.out.println("Found " + nodeCount + " unique nodes in the model");
	}
	
	public void matchNodeWithNewModel(Node tempNodeOrigin, NodeType actType){
		//TODO: Check that the nodes haven't been already compared with help of the ConsistencyData object
		
		
	}
	
	/**
	 * Get the name from the Node object
	 * @return the actual name or "Name not available" if the name was not defined
	 * @author Luc
	 */
	private String findName(NodeType actType, Node actNode)
	{
		String name="Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp =actType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		if (tmp.isOne())
		{
			Attribute nodeName = tmp.getOne();
			
			if (nodeName.get(actNode)!=null)
			{
				PSSIFValue value =null;
				if (nodeName.get(actNode).isOne())
				{
					value = nodeName.get(actNode).getOne();
					name = value.asString();
				}
				if (nodeName.get(actNode).isNone())
				{
					name ="Name not available";
				}
			}
			else
				name ="Name not available";
		}
			
		return name;
	}
}
