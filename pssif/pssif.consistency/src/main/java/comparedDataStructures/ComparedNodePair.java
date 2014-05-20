package comparedDataStructures;

import graph.model.MyNode;


public class ComparedNodePair extends Compared {

	private MyNode nodeOriginalModel, nodeNewModel;
	
	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;
	
	/**
	 * the result of the matching between the surrounding elements between two elements
	 */
	private double contextMatchResult;
	
	/**
	 * the result of the matching between two elements based on their depth in the modelgraph
	 */
	private double depthMatchResult;
}
