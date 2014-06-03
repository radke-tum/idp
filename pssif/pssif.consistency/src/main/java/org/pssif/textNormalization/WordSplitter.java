package org.pssif.textNormalization;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.danielnaber.jwordsplitter.AbstractWordSplitter;
import de.danielnaber.jwordsplitter.GermanWordSplitter;

/**
 * @author Andreas
 * 
 *         This class splits a given token (if it's a word compund), if
 *         possible, into smaller tokens. Therefore the jwordsplitter is used.
 */
public class WordSplitter {

	private AbstractWordSplitter splitter;

	public WordSplitter() {
		try {
			this.splitter = new GermanWordSplitter(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		splitter.setStrictMode(true);
	}

	/**
	 * This method takes a list of tokens and iterates over it. For every
	 * token it looks up whether the token is a compound and if yes it is split
	 * into 2 or more tokens. These are then added to the result. If a token
	 * can't be split it's added unchanged to the result.
	 * 
	 * @param tokens
	 *            the token list where splittable tokens are splitted
	 * @return the list of splitted and not splittable tokens
	 * 
	 */
	public List<Token> splitTokens(List<Token> tokens) {
		List<Token> newSequence = new LinkedList<Token>();
		List<String> splitResult;

		for (Token token : tokens) {

			splitResult = splitter.splitWord(token.getWord());

			if (!splitResult.isEmpty()) {
				for (String str : splitResult) {
					newSequence.add(new Token(str));
				}
			} else {
				newSequence.add(token);
			}
		}

		return newSequence;
	}

}
