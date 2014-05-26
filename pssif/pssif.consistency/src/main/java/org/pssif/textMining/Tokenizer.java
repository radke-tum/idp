package org.pssif.textMining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

public class Tokenizer {

	/**
	 * Modifzierter Code von RapidMiner! StringTokenizerOperator.java (aus
	 * ordner: Text Processing(Tokenizing, Stemming, Stopwords)
	 */

	/*
	 * RapidMiner Text Processing Extension
	 * 
	 * Copyright (C) 2001-2013 by Rapid-I and the contributors
	 * 
	 * Complete list of developers available at our web site:
	 * 
	 * http://rapid-i.com
	 * 
	 * This program is free software: you can redistribute it and/or modify it
	 * under the terms of the GNU Affero General Public License as published by
	 * the Free Software Foundation, either version 3 of the License, or (at
	 * your option) any later version.
	 * 
	 * This program is distributed in the hope that it will be useful, but
	 * WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero
	 * General Public License for more details.
	 * 
	 * You should have received a copy of the GNU Affero General Public License
	 * along with this program. If not, see http://www.gnu.org/licenses/.
	 */
	public static List<Token> findTokens(String label) {
		// TODO initialize and sort only once for performance reasons
		char[] splitCharacters = { '.', ';', ',', ' ' };
		Arrays.sort(splitCharacters);

		List<Token> newSequence = new ArrayList<Token>();

		char[] tokenChars = label.toCharArray();
		int start = 0;
		for (int i = 0; i < tokenChars.length; i++) {
			if (isSplitPoint(tokenChars[i], splitCharacters)) {
				if (i - start > 0) {
					newSequence.add(new Token(new String(tokenChars, start, i
							- start)));
				}
				start = i + 1;
			}
		}
		if (tokenChars.length - start > 0)
			newSequence.add(new Token(new String(tokenChars, start,
					tokenChars.length - start)));

		return newSequence;

	}

	/*
	 * RapidMiner Text Processing Extension
	 * 
	 * Copyright (C) 2001-2013 by Rapid-I and the contributors
	 * 
	 * Complete list of developers available at our web site:
	 * 
	 * http://rapid-i.com
	 * 
	 * This program is free software: you can redistribute it and/or modify it
	 * under the terms of the GNU Affero General Public License as published by
	 * the Free Software Foundation, either version 3 of the License, or (at
	 * your option) any later version.
	 * 
	 * This program is distributed in the hope that it will be useful, but
	 * WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero
	 * General Public License for more details.
	 * 
	 * You should have received a copy of the GNU Affero General Public License
	 * along with this program. If not, see http://www.gnu.org/licenses/.
	 */
	private static boolean isSplitPoint(char character, char[] splitCharacters) {
		return Arrays.binarySearch(splitCharacters, character) >= 0;
	}

}
