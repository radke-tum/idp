package org.pssif.textNormalization;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.danielnaber.jwordsplitter.AbstractWordSplitter;
import de.danielnaber.jwordsplitter.GermanWordSplitter;

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

	public List<Token> splitTokens(List<Token> tokens) {
		List<Token> newSequence = new LinkedList<Token>();
		List<String> splitResult;

		for (Token token : tokens) {

			splitResult = splitter.splitWord(token.getWord());

			if (!splitResult.isEmpty()) {
				for(String str : splitResult){
					newSequence.add(new Token(str));
				}
			} else {
				newSequence.add(token);
			}
		}

		return newSequence;
	}

}
