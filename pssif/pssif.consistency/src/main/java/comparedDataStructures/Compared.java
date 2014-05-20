package comparedDataStructures;

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
	private boolean wasMerged;

	/**
	 * @return the wasMerged
	 */
	public boolean isWasMerged() {
		return wasMerged;
	}

	/**
	 * @param wasMerged
	 *            the wasMerged to set
	 */
	public void setWasMerged(boolean wasMerged) {
		this.wasMerged = wasMerged;
	}
}
