package org.pssif.textNormalization;

import org.pssif.consistencyDataStructures.Token;

public class CaseNormalizer {

	public static Token toLowerCase(Token token) {
		String temp = token.getWord();

		/**
		 * check if the tokens word doesnt consist of upper case letters only
		 * (if it only has uppercase letters it is often an abberevation)
		 */
		if (!(temp.toUpperCase() == temp)){
			temp.toLowerCase();
		}

			token.setWord(temp);
		return token;
	}

}
