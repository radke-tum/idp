package comparedDataStructures;

import org.pssif.consistencyDataStructures.Token;

public class ComparedNormalizedTokensPair extends Compared {

	private Token[] tokensOriginalModel, tokensNewModel;
	
	/*
	 * the result of the matching between two tokens based on their synonym
	 * similarity
	 */
	private double synonymSimilarityMatchResult;

	/*
	 * the result of the calculation of the string edit distance metric between
	 * tokens
	 */
	private double stringEditDistanceResult;
	
	/*
	 * the result of the compairson of the hyphens of strings/tokens
	 */
	private double hyphenMatchResult;
	
	private double vsmMatchResult;
	
	private double lsiMatchResult;

}
