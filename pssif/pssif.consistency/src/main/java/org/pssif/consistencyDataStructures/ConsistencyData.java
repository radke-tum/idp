package org.pssif.consistencyDataStructures;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;
import org.pssif.mainProcesses.Methods;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         This class stores all the information relevant for the consistency
 *         checking process. It Stores the two models that shall be compared and
 *         the according metamodel.
 * 
 *         With this class we know:
 * 
 *         - which IDs already matched (so we don't match them again as we go up
 *         in the class hierachy in the compairson process) - the similarity
 *         results for token & label pairs to be able to look them up in future
 *         compairsons -
 */
public class ConsistencyData {

	public ConsistencyData() {

		IDMapping = new HashSet<String>();
		this.comparedLabelPairs = new LinkedList<ComparedLabelPair>();
		this.comparedTokensPairs = new LinkedList<ComparedNormalizedTokensPair>();
		this.comparedNodePairs = new LinkedList<ComparedNodePair>();
	}

	// TODO Attention! Variable is volatile, will be lost at serialization!
	/**
	 * stores the already compared IDs as the pair (originModelElementID,
	 * newModelElementID)
	 */
	private volatile HashSet<String> IDMapping;

	/**
	 * stores the label pairs which were already matched together with the
	 * similarity metric results
	 */
	private List<ComparedLabelPair> comparedLabelPairs;

	/**
	 * stores the tokens pairs which were already matched together with the
	 * similarity metric results
	 */
	private List<ComparedNormalizedTokensPair> comparedTokensPairs;

	/**
	 * stores compared Nodes with similarity information
	 */
	private volatile List<ComparedNodePair> comparedNodePairs;

	/**
	 * these are the values of the tresholds for the syntactic, semantic and
	 * contextual metrics. Depending on these tresholds and the results of a
	 * match between two nodes the match is proposed to the user if one of the
	 * tresholds is exceeded
	 */
	private static double semanticTreshhold;
	private static double syntacticTreshhold;
	private static double contextTreshhold;

	/**
	 * @return the comparedNodePairs
	 */
	public List<ComparedNodePair> getComparedNodePairs() {
		return comparedNodePairs;
	}

	public static void initThreshholds(double synTreshhold,
			double semTreshhold, double conTreshhold) {
		syntacticTreshhold = synTreshhold;
		semanticTreshhold = semTreshhold;
		contextTreshhold = conTreshhold;
	}

	/**
	 * THis method takes the result of the application of all active metrics
	 * onto two nodes and saves the result into the ID Mapping (so Nodes are
	 * only matched once), the already compared labels & tokens and the compared
	 * Node pairs
	 * 
	 * @param comparedNodePair
	 *            the result of the matching process
	 * @return true if the new compared elements were added to all relevant
	 *         variables. false if something went wrong
	 */
	public boolean putComparedEntry(ComparedNodePair comparedNodePair) {

		boolean success = true;

		success = success
				&& IDMapping.add(Methods.findGlobalID(
						comparedNodePair.getNodeOriginalModel(),
						comparedNodePair.getTypeOriginModel())
						+ Methods.findGlobalID(
								comparedNodePair.getNodeNewModel(),
								comparedNodePair.getTypeNewModel()));
		success = success
				&& comparedLabelPairs
						.add(comparedNodePair.getLabelComparison());
		success = success
				&& comparedTokensPairs.add(comparedNodePair
						.getTokensComparison());
		success = success && comparedNodePairs.add(comparedNodePair);

		return success;
	}

	/**
	 * This method says whether a match between the two given IDs is necessary
	 * 
	 * @param globalIDNodeOrigin
	 *            the global ID of the original node
	 * @param globalIDNodeNew
	 *            the global ID of the new node
	 * @return a bool saying whether the two nodes have been matched already
	 * @true if a match is necessary
	 * @false if they have already been matched
	 */
	public boolean matchNecessary(String globalIDNodeOrigin,
			String globalIDNodeNew) {
		return (!IDMapping.contains(globalIDNodeOrigin + globalIDNodeNew));
	}

	/**
	 * This methhod returns the result of a match in form of a ComparedNodePair
	 * between the two given nodes if they have been matched once
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param tempNodeNew
	 *            the node from the new model
	 * @return the result of a match in form of a ComparedNodePair between the
	 *         two given nodes if they have been matched once
	 */
	public ComparedNodePair getNodeMatch(Node tempNodeOrigin, Node tempNodeNew) {

		for (ComparedNodePair comparedNodePair : comparedNodePairs) {
			if (comparedNodePair.getNodeOriginalModel().equals(tempNodeOrigin)
					&& comparedNodePair.getNodeNewModel().equals(tempNodeNew)) {
				return comparedNodePair;
			}
		}
		return null;
	}

	// TODO Delete?
	/**
	 * This method returns the result of a match between two nodes if they one
	 * of the nodes has already been compared with at least one other node. So
	 * the normalization doesn't have to be computed again.
	 * 
	 * @param tempNode
	 *            the node which is looked up in the stored results
	 * @param tempNodeType
	 *            the type of tempNode
	 * @return a ComparedNodePair which holds inter alia the normalized label of
	 *         the looked up token
	 */
	@Deprecated
	public ComparedNodePair nodeAlreadyCompared(Node tempNode,
			NodeType tempNodeType) {

		String tempNodeID = Methods.findGlobalID(tempNode, tempNodeType);

		String tempSearchedOriginNodeID, tempSearchedNewNodeID;

		for (ComparedNodePair comparedNodePair : comparedNodePairs) {
			tempSearchedOriginNodeID = Methods.findGlobalID(
					comparedNodePair.getNodeOriginalModel(),
					comparedNodePair.getTypeOriginModel());

			tempSearchedNewNodeID = Methods.findGlobalID(
					comparedNodePair.getNodeNewModel(),
					comparedNodePair.getTypeNewModel());

			if (tempSearchedNewNodeID.equals(tempNodeID)
					|| tempSearchedOriginNodeID.equals(tempNodeID)) {
				return comparedNodePair;
			}
		}
		return null;
	}

	//TODO comment
	/**Depending on these tresholds and the results of a
	 * match between two nodes the match is proposed to the user if one of the
	 * tresholds is exceeded
	 */
	public List<ComparedNodePair> getMergeCandidateList() {
		List<ComparedNodePair> mergeCandidates = new LinkedList<>();

		for (ComparedNodePair actPair : comparedNodePairs) {
			if ((actPair.getWeightedSyntacticResult() >= ConsistencyData.syntacticTreshhold)
					|| (actPair.getWeightedSemanticResult() >= ConsistencyData.semanticTreshhold)
					|| (actPair.getWeightedContextResult() >= ConsistencyData.contextTreshhold)) {
				mergeCandidates.add(actPair);
			}
		}

		return mergeCandidates;

	}

	public List<ComparedNodePair> getMergedList() {
		List<ComparedNodePair> mergedList = new LinkedList<>();

		for (ComparedNodePair actPair : comparedNodePairs) {
			if (actPair.isMerged()) {
				mergedList.add(actPair);
			}
		}

		return mergedList;
	}
}
