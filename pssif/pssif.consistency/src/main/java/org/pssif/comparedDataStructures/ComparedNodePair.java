package org.pssif.comparedDataStructures;

import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         this class stores two nodes. one from the first imported model and
 *         the other one from the recent imported model. In addition to this
 *         this class stores the match result of the label compairson of the two
 *         nodes, the tokenization of the labels (plus the according metric
 *         results) and the result of two node metrics (depth matching and
 *         contextual similarity)
 * 
 */
public class ComparedNodePair extends Compared {

	private Node nodeOriginalModel, nodeNewModel;

	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;

	/**
	 * the result of the matching between two elements based on their the
	 * surrounding elements
	 */
	private double contextMatchResult;

	/**
	 * the result of the matching between two elements based on their depth in
	 * the modelgraph
	 */
	private double depthMatchResult;
}
