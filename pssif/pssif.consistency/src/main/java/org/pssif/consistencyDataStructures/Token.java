package org.pssif.consistencyDataStructures;

/**
 * @author Andreas
 * 
 *         this class represents a simple token implementation. Each token holds
 *         a word as a String and an according wordWeigth
 * 
 */
public class Token {

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	public Token(String string) {
		this.word = string;
	}
	private String word;
	private double wordWeigth;

}
