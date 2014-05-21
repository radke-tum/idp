package org.pssif.comparedDataStructures;

import org.pssif.consistencyDataStructures.Token;

/**
 * @author Andreas
 * 
 *         This class saves the tokenized and normalized labels of two compared
 *         labels. Additionally it saves the result of several syntactic and
 *         semantic metrics applied to the two token sets.
 * 
 *         Saving this information allows, in further matchings to look up if
 *         similar labels have once been matched and maybe merged so this
 *         information can be useful as more and more models have been matched
 * 
 */
public class ComparedNormalizedTokensPair extends Compared {

	private Token[] tokensOriginalModel, tokensNewModel;

	/**
	 * the result of the matching between two tokens based on their synonym
	 * similarity
	 */
	private double synonymSimilarityMatchResult;

	/**
	 * the result of the calculation of the string edit distance metric between
	 * tokens
	 */
	private double stringEditDistanceResult;

	/**
	 * the result of the compairson of the hyphens of tokens
	 */
	private double hyphenMatchResult;

	/**
	 * the result of the application of the information retrieval technique
	 * vector space model to the two token sets
	 */
	private double vsmMatchResult;

	/**
	 * the result of the application of the information retrieval technique
	 * latent semantic indexing to the two token sets
	 */
	private double lsiMatchResult;

}
