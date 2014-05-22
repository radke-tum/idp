package org.pssif.consistencyDataStructures;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;

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

	/**
	 * @param iDMapping
	 * @param comparedLabelPairs
	 * @param comparedTokensPairs
	 * @param comparedNodePairs
	 */
	public ConsistencyData() {

		IDMapping = new HashSet<String>();
		this.comparedLabelPairs = new LinkedList<ComparedLabelPair>();
		this.comparedTokensPairs = new LinkedList<ComparedNormalizedTokensPair>();
		this.comparedNodePairs = new LinkedList<ComparedNodePair>();
	}

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
	 * @return true if the new compared elements were added to all relevant
	 *         variables
	 * @return false if something went wrong
	 */
	public boolean putComparedEntry(Node tempNodeOrigin, Node tempNodeNew,
			ComparedNodePair comparedNodePair) {

		boolean success = true;

		success = success
				&& IDMapping.add(tempNodeOrigin.getId() + tempNodeNew.getId());
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
	 * @param idNodeOrigin the id of the original node
	 * @param idNodeNew the id of the new node
	 * @return a bool saying whether the two nodes have been matched already
	 * @true if a match is necessary
	 * @false if they have already been matched
	 */
	public boolean matchNecessary(String idNodeOrigin, String idNodeNew) {
		return(!IDMapping.contains(idNodeOrigin + idNodeNew));
	}
}
