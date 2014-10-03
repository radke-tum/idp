package de.tum.pssif.core.metamodel.external;

/**
 * Represent a tupel of two strings
 * @author Alex
 *
 */
public class Tupel {

	private String first;
	private String second;

	/**
	 * Constructor for a tupel
	 * @param first First component of the tupel
	 * @param second Second component of the tupel
	 */
	public Tupel(String first, String second) {
		this.setFirst(first);
		this.setSecond(second);
	}

	/**
	 * Get the first part of the tupel
	 * @return First part of the tupel
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * Set the first part of the tupel
	 * @param first New first part of the tupel
	 */
	public void setFirst(String first) {
		this.first = first;
	}

	/**
	 * Get the second part of the tupel
	 * @return Second part of the tupel
	 */
	public String getSecond() {
		return second;
	}

	/**
	 * Set the second part of the tupel
	 * @param second New second part of the tupel
	 */
	public void setSecond(String second) {
		this.second = second;
	}
	
	/**
	 * Check if a string is part of the tupel
	 * @param searchString The string to check for
	 * @return Result of the check, true if string is contained
	 */
	public boolean contains(String searchString) {
		return first.equals(searchString) || second.equals(searchString);
	}
	
	/**
	 * As a string formatted tupel
	 */
	public String toString() {
		return "[" + first + ", " + second + "]";
	}
}
