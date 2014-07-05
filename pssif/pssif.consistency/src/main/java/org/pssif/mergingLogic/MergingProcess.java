package org.pssif.mergingLogic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.mainProcesses.Methods;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;
import org.pssif.mergedDataStructures.MergedNodePair;
import org.pssif.textNormalization.Normalizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class MergingProcess {

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

	private ConsistencyData consistencyData;

	private Model originalModel, newModel;
	private Metamodel metaModelOriginal, metaModelNew;

	private Normalizer normalizer;

	private List<MatchMethod> matchMethods;

	private MatchMethod exactMatcher;
	private MatchMethod attributeMatcher;

	/**
	 * @param consistencyData
	 * @param originalModel
	 * @param newModel
	 * @param metaModelOriginal
	 * @param metaModelNew
	 */
	public MergingProcess(ConsistencyData consistencyData, Model originalModel,
			Model newModel, Metamodel metaModelOriginal, Metamodel metaModelNew) {
		super();
		this.consistencyData = consistencyData;
		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModelOriginal = metaModelOriginal;
		this.metaModelNew = metaModelNew;

		initializeMatchMethods();

		this.normalizer = Normalizer.initialize(matchMethods);

		mergeModels();
	}

	/**
	 * TODO
	 */
	private void initializeMatchMethods() {
		matchMethods = new LinkedList<MatchMethod>();

		exactMatcher = MatchMethod.createMatchMethodObject(
				MatchingMethods.EXACT_STRING_MATCHING, true, 1.0);

		matchMethods.add(exactMatcher);

		attributeMatcher = MatchMethod.createMatchMethodObject(
				MatchingMethods.ATTRIBUTE_MATCHING, true, 1.0);

		matchMethods.add(attributeMatcher);
	}

	private void mergeModels() {
		startTypeAndNodeIteration();
	}

	/**
	 * this method checks if the original Model contains at least one node and
	 * then calls the typeIteration Method. If the original model is empty no
	 * merge can be conducted so no further methods are called for compairson
	 */
	public void startTypeAndNodeIteration() {
		NodeType rootNodeType = metaModelOriginal.getNodeType(
				PSSIFConstants.ROOT_NODE_TYPE_NAME).getOne();

		PSSIFOption<Node> nodesOriginalModel = rootNodeType.apply(
				originalModel, true);

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

	/**
	 * this method iterates over every node type of the pssif metamodel and
	 * calls the method iterateNodesOfType() with the current type. This ensures
	 * that all nodes from the original model are compared with allfrom the
	 * other model.
	 * 
	 * It starts with the children of the Type DevelopmentArtifact and then
	 * continues with the children of the Type Solution Artifact. After that the
	 * Development and Solution Artifact Type and finally the Node Type are
	 * iterated.
	 */
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

	/**
	 * this method gets all nodes from the original model with the given type.
	 * If there is no node with the type in the model the method is finished. If
	 * there is one node with the type in the model the node is given to the
	 * method iterateOverTypesOfNewModel(). If there are many nodes each node is
	 * given seperately to the method iterateOverTypesOfNewModel().
	 * 
	 * @param type
	 *            the type of nodes which are looked up in the original model to
	 *            do the compairson
	 * @param includeSubtypes
	 *            a bool specifying whether nodes with a subtype of type shall
	 *            too be part of the compairson process
	 * 
	 * 
	 * 
	 */
	public void iterateNodesOfType(String type, boolean includeSubtypes) {

		NodeType actTypeOriginModel;
		PSSIFOption<Node> actNodesOriginalModel;

		actTypeOriginModel = metaModelOriginal.getNodeType(type).getOne();

		actNodesOriginalModel = actTypeOriginModel.apply(originalModel,
				includeSubtypes);

		if (actNodesOriginalModel.isNone()) {
		} else {
			if (actNodesOriginalModel.isOne()) {

				Node tempNodeOrigin = actNodesOriginalModel.getOne();

				iterateOverNodesOfNewModel(tempNodeOrigin, actTypeOriginModel);

			} else {

				Set<Node> tempNodesOrigin = actNodesOriginalModel.getMany();

				Iterator<Node> tempNodeOrigin = tempNodesOrigin.iterator();

				while (tempNodeOrigin.hasNext()) {

					iterateOverNodesOfNewModel(tempNodeOrigin.next(),
							actTypeOriginModel);

				}
			}
		}
	}

	public void iterateOverNodesOfNewModel(Node tempNodeOrigin,
			NodeType actTypeOriginModel) {
		/**
		 * the actual type with which nodes to compare are searched in the new
		 * model
		 */
		NodeType actTypeNewModel = actTypeOriginModel;

		PSSIFOption<Node> actNodesNewModel = actTypeNewModel.apply(newModel,
				false);

		matchNodeWithNodesOfActTypeOfNewModel(tempNodeOrigin, actNodesNewModel,
				actTypeOriginModel, actTypeNewModel);

	}

	public void matchNodeWithNodesOfActTypeOfNewModel(Node tempNodeOrigin,
			PSSIFOption<Node> actNodesNewModel, NodeType actTypeOriginModel,
			NodeType actTypeNewModel) {
		if (actNodesNewModel.isNone()) {
			System.out
					.println("There is no node in the new model of the type "
							+ actTypeNewModel.getName()
							+ " to match. Continuing with next node from the original model.");
		} else {
			if (actNodesNewModel.isOne()) {

				matchNodeWithNode(tempNodeOrigin, actNodesNewModel.getOne(),
						actTypeOriginModel, actTypeNewModel);

			} else {
				Set<Node> tempNodesNew = actNodesNewModel.getMany();

				Iterator<Node> tempNodeNew = tempNodesNew.iterator();

				while (tempNodeNew.hasNext()) {

					matchNodeWithNode(tempNodeOrigin, tempNodeNew.next(),
							actTypeOriginModel, actTypeNewModel);
				}

			}
		}
	}

	public void matchNodeWithNode(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {

		String globalIDNodeOrigin = Methods.findGlobalID(tempNodeOrigin,
				actTypeOriginModel);
		String globalIDNodeNew = Methods.findGlobalID(tempNodeNew,
				actTypeNewModel);

		if (consistencyData.matchNecessary(globalIDNodeOrigin, globalIDNodeNew)) {
			matchNodes(tempNodeOrigin, tempNodeNew, actTypeOriginModel,
					actTypeNewModel);
		} else {
			System.out.println("These two nodes have already been compared.");
		}

	}

	private void matchNodes(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {

		String labelOrigin = Methods.findName(actTypeOriginModel,
				tempNodeOrigin);
		String labelNew = Methods.findName(actTypeNewModel, tempNodeNew);

		String labelOriginNormalized = normalizer.normalizeLabel(labelOrigin);
		String labelNewNormalized = normalizer.normalizeLabel(labelNew);

		boolean traceLink = false;
		boolean merge = false;

		double exactMatchResult = exactMatcher.executeMatching(tempNodeOrigin,
				tempNodeNew, originalModel, newModel, metaModelOriginal,
				metaModelNew, actTypeOriginModel, actTypeNewModel,
				labelOriginNormalized, labelNewNormalized, null, null);

		double attributeMatchResult = attributeMatcher.executeMatching(
				tempNodeOrigin, tempNodeNew, originalModel, newModel,
				metaModelOriginal, metaModelNew, actTypeOriginModel,
				actTypeNewModel, labelOriginNormalized, labelNewNormalized,
				null, null);

		if (exactMatchResult > 0.0) {
			if (attributeMatchResult >= 1.0) {
				merge = true;
			} else {
				if (attributeMatchResult >= 0.25) {
					traceLink = true;
				}
			}
		} else {
			// no match found between the two nodes
		}

		ComparedLabelPair comparedLabelPair = new ComparedLabelPair(
				labelOrigin, labelNew, labelOriginNormalized,
				labelNewNormalized);

		comparedLabelPair.setExactMatchResult(exactMatchResult);

		MergedNodePair mergedNodePair = new MergedNodePair(tempNodeOrigin,
				tempNodeNew, actTypeOriginModel, actTypeNewModel,
				comparedLabelPair, traceLink, merge);

		consistencyData.putMergedEntry(mergedNodePair);
	}
}