package comparedDataStructures;


public class ComparedElementPair<T> extends Compared {

	private T elementOriginalModel, elementNewModel;
	
	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;
	
	/*
	 * the result of the matching between the surrounding elements between two elements
	 */
	private double contextMatchResult;
	
	/*
	 * the result of the matching between two elements based on their depth in the modelgraph
	 */
	private double depthMatchResult;
}
