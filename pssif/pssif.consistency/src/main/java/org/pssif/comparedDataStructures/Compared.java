package org.pssif.comparedDataStructures;

/**
 * @author Andreas
 * 
 * 
 *         Classes extendig this class are able to say whether their elements
 *         have been equals or not
 */
public abstract class Compared {
	/**
	 * bool saying if the proposed merge was accepted by the user
	 */
	private boolean equals;

	/**
	 * @return the equals
	 */
	public boolean isEquals() {
		return equals;
	}

	/**
	 * @param equals
	 *            the equals to set
	 */
	public void setEquals(boolean equals) {
		this.equals = equals;
	}
}
