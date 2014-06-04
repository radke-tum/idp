package org.pssif.comparedDataStructures;

/**
 * @author Andreas
 * 
 *         this class saves the labels (unnormalized except whitespace removal)
 *         of two compared labels and the according match result of these
 *         labels. Saving this information allows, in further matchings to look
 *         up if similar labels have once been matched (and maybe merged). So this
 *         information can be useful as more and more models have been matched.
 * 
 */
public class ComparedLabelPair extends Compared {

	/**
	 * @param labelOrigin
	 * @param labelNew
	 */
	public ComparedLabelPair(String labelOrigin, String labelNew) {
		super();
		this.labelOrigin = labelOrigin;
		this.labelNew = labelNew;
	}

	/**
	 * two Strings representing Labels of Nodes (whitespace removed) to be able
	 * to lookup if two labels have been already matched once
	 */
	private String labelOrigin, labelNew;

	/**
	 * the result of a direct string/token comparison
	 */
	private double exactMatchResult;

	/**
	 * @return the exactMatchResult
	 */
	public double getExactMatchResult() {
		return exactMatchResult;
	}

	/**
	 * @return the labelOrigin
	 */
	public String getLabelOrigin() {
		return labelOrigin;
	}

	/**
	 * @return the labelNew
	 */
	public String getLabelNew() {
		return labelNew;
	}

	/**
	 * @param exactMatchResult the exactMatchResult to set
	 */
	public void setExactMatchResult(double exactMatchResult) {
		this.exactMatchResult = exactMatchResult;
	}

}
