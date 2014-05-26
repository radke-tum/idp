package org.pssif.comparedDataStructures;

import java.util.List;

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

	/**
	 * @param tokensOrigin
	 * @param tokensNew
	 */
	public ComparedNormalizedTokensPair(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		super();
		this.tokensOrigin = tokensOrigin;
		this.tokensNew = tokensNew;
	}

	private List<Token> tokensOrigin;

	private List<Token> tokensNew;

	/**
	 * the result of the matching between two tokens based on their synonym
	 * similarity
	 */
	private double linguisticMatchResult;

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

	/**
	 * @return the linguisticMatchResult
	 */
	public double getLinguisticMatchResult() {
		return linguisticMatchResult;
	}

	/**
	 * @return the stringEditDistanceResult
	 */
	public double getStringEditDistanceResult() {
		return stringEditDistanceResult;
	}

	/**
	 * @return the hyphenMatchResult
	 */
	public double getHyphenMatchResult() {
		return hyphenMatchResult;
	}

	/**
	 * @return the vsmMatchResult
	 */
	public double getVsmMatchResult() {
		return vsmMatchResult;
	}

	/**
	 * @return the lsiMatchResult
	 */
	public double getLsiMatchResult() {
		return lsiMatchResult;
	}
	
	/**
	 * @param linguisticMatchResult the linguisticMatchResult to set
	 */
	public void setLinguisticMatchResult(double linguisticMatchResult) {
		this.linguisticMatchResult = linguisticMatchResult;
	}

	/**
	 * @param stringEditDistanceResult the stringEditDistanceResult to set
	 */
	public void setStringEditDistanceResult(double stringEditDistanceResult) {
		this.stringEditDistanceResult = stringEditDistanceResult;
	}

	/**
	 * @param hyphenMatchResult the hyphenMatchResult to set
	 */
	public void setHyphenMatchResult(double hyphenMatchResult) {
		this.hyphenMatchResult = hyphenMatchResult;
	}

	/**
	 * @param vsmMatchResult the vsmMatchResult to set
	 */
	public void setVsmMatchResult(double vsmMatchResult) {
		this.vsmMatchResult = vsmMatchResult;
	}

	/**
	 * @param lsiMatchResult the lsiMatchResult to set
	 */
	public void setLsiMatchResult(double lsiMatchResult) {
		this.lsiMatchResult = lsiMatchResult;
	}

}
