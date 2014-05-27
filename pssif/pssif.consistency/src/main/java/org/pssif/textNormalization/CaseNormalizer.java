package org.pssif.textNormalization;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

public class CaseNormalizer {

	public List<Token> convertTokensToLowerCase(List<Token> tokens) {

		String temp;

		for (Token token : tokens) {

			temp = token.getWord();

			/**
			 * check if the tokens word doesnt consist of upper case letters
			 * only (if it only has uppercase letters it is assumed to be an
			 * abberevation)
			 */
			if (!(temp.toUpperCase() == temp)) {
				temp = temp.toLowerCase();
				token.setWord(temp);
			}
		}

		return tokens;
	}

}
