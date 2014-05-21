package org.pssif.comparedDataStructures;

/**
 * @author Andreas
 * 
 * 
 *         Classes extendig this class are able to say whether their elements
 *         have been merged or not
 */
public abstract class Compared {

	/**
	 * bool saying if the proposed merge was accepted by the user
	 */
	private boolean merged;

	/**
	 * @return the merged
	 */
	public boolean isMerged() {
		return merged;
	}

	/**
	 * @param merged
	 *            the merged to set
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}
}
