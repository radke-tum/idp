package org.pssif.consistencyDataStructures;

import java.util.HashMap;
import java.util.HashSet;

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
 *         in the class hierachy in the compairson process)
 *         - the similarity
 *         results for token & label pairs to be able to look them up in future compairsons
 *         - 
 */
public class ConsistencyData {



	/**
	 * stores the already compared IDs as the pair (originModelElementID,
	 * newModelElementID)
	 */
	private HashMap<String, String> IDMapping;

	/**
	 * stores the label pairs which were already matched together with the
	 * similarity metric results
	 */
	private HashSet<ComparedLabelPair> comparedLabelPairs;

	/**
	 * stores the tokens pairs which were already matched together with the
	 * similarity metric results
	 */
	private HashSet<ComparedNormalizedTokensPair> comparedTokensPair;

	/**
	 * stores compared Nodes with similarity information
	 */
	private HashSet<ComparedNodePair> comparedNodePairs;

	/**
	 * @return true if the new compared elements were added to all relevant
	 *         variables
	 * @return false if something went wrong
	 */
	public boolean putComparedEntry(Node a, Node b, ComparedNormalizedTokensPair c, ComparedLabelPair d, ComparedNodePair e) {
		return false;
	}

}
