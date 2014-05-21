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
						"This can never happen. Maybe the structure of the root node type was changed");
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

		iterateNodesOfType(PSSIFCanonicMetamodelCreator.N_DEV_ARTIFACT, false);
		iterateNodesOfType(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT, false);
		iterateNodesOfType(PSSIFConstants.ROOT_NODE_TYPE_NAME, false);
	}

	public void iterateNodesOfType(String type, boolean includeSubtypes) {

		int nodeCount = 0;

		NodeType actTypeOriginModel;
		PSSIFOption<Node> actNodesOriginalModel;

		actTypeOriginModel = metaModel.getNodeType(type).getOne();

		actNodesOriginalModel = actTypeOriginModel.apply(originalModel,
				includeSubtypes);

		if (actNodesOriginalModel.isNone()) {
			// System.out.println("There are no nodes of the type "
			// + actType.getName()
			// + " in the original model. Continuing with the next type.");
		} else {
			if (actNodesOriginalModel.isOne()) {
				// System.out
				// .println("There is one node of the type "
				// + actType.getName()
				// +
				// " in the original model. Starting the matching for this node.");

				Node tempNodeOrigin = actNodesOriginalModel.getOne();

				iterateOverTypesOfNewModel(tempNodeOrigin, actTypeOriginModel);

				/*
				 * System.out.println(findName(actType,tempNodeOrigin));
				 * nodeCount++;
				 */

			} else {
				// System.out
				// .println("There are many nodes of the type "
				// + actType.getName()
				// +
				// " in the original model. Starting the matching for these nodes.");

				Set<Node> tempNodesOrigin = actNodesOriginalModel.getMany();

				Iterator<Node> tempNodeOrigin = tempNodesOrigin.iterator();

				while (true) {

					iterateOverTypesOfNewModel(tempNodeOrigin.next(),
							actTypeOriginModel);

					/*
					 * System.out.println(findName(actType,tempNodeOrigin.next())
					 * ); nodeCount++;
					 */

					if (!tempNodeOrigin.hasNext()) {
						break;
					}
				}

			}
		}
		// System.out.println("Found " + nodeCount +
		// " unique nodes in the model");
	}

	public void iterateOverTypesOfNewModel(Node tempNodeOrigin, NodeType type) {
		// TODO: Check that the nodes haven't been already compared with help of
		// the ConsistencyData object
		NodeType actTypeNewModel = type;

		boolean firstIteration = true;

		// QuickFIX!!! (problem is: If the program shall compare a node of type
		// node with the other model
		// it's currently not matched with any other node because the subtypes
		// are not included in the first iteration of the matching
		if (type.getName() == PSSIFConstants.ROOT_NODE_TYPE_NAME) {
			firstIteration = false;
		}

		while (true) {

			PSSIFOption<Node> actNodesNewModel = actTypeNewModel.apply(
					newModel, !(firstIteration));

			matchNodeWithNodesOfActTypeOfNewModel(tempNodeOrigin,
					actNodesNewModel, actTypeNewModel);

			if (actTypeNewModel.getGeneral().isNone()) {
				break;
			} else {
				actTypeNewModel = actTypeNewModel.getGeneral().getOne();
			}

			firstIteration = false;
		}
	}

	public void matchNodeWithNodesOfActTypeOfNewModel(Node tempNodeOrigin,
			PSSIFOption<Node> actNodesNewModel, NodeType actTypeNewModel) {
		if (actNodesNewModel.isNone()) {
			// System.out
			// .println("There is no node in the new model of this type to match. Continuing with next node type from new model.");
		} else {
			if (actNodesNewModel.isOne()) {

				matchNodeWithNode(tempNodeOrigin, actNodesNewModel.getOne(),
						actTypeNewModel);

			} else {
				Set<Node> tempNodesNew = actNodesNewModel.getMany();

				Iterator<Node> tempNodeNew = tempNodesNew.iterator();

				while (true) {

					matchNodeWithNode(tempNodeOrigin, tempNodeNew.next(),
							actTypeNewModel);

					if (!tempNodeNew.hasNext()) {
						break;
					}
				}

			}
		}
	}

	private void matchNodeWithNode(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeNewModel) {
		System.out.println("Comparing original: "
				+ findName(actTypeNewModel, tempNodeOrigin)
				+ " with new node: " + findName(actTypeNewModel, tempNodeNew)
				+ " of type " + actTypeNewModel.getName());
		// TODO: Apply Matching for the two given nodes!

	}

	/**
	 * Get the name from the Node object
	 * 
	 * @return the actual name or "Name not available" if the name was not
	 *         defined
	 * @author Luc
	 */
	private String findName(NodeType actType, Node actNode) {
		String name = "Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp = actType
				.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		if (tmp.isOne()) {
			Attribute nodeName = tmp.getOne();

			if (nodeName.get(actNode) != null) {
				PSSIFValue value = null;
				if (nodeName.get(actNode).isOne()) {
					value = nodeName.get(actNode).getOne();
					name = value.asString();
				}
				if (nodeName.get(actNode).isNone()) {
					name = "Name not available";
				}
			} else
				name = "Name not available";
		}

		return name;
	}
}
