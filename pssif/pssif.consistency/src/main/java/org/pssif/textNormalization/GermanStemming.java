/**
 * modified by Andreas Genz
 * Source: GermanStemming
 * from folder: Text Processing (Tokenizing, Stemming, StopWords)
 */

/*
 *  RapidMiner Text Processing Extension
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.pssif.textNormalization;

/**
This file is part of PSSIF Consistency. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

PSSIF Consistency is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

PSSIF Consistency is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with PSSIF Consistency.  If not, see <http://www.gnu.org/licenses/>.

Feel free to contact me via eMail: genz@in.tum.de
*/

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A stemmer for German words. The algorithm is based on the report
 * "A Fast and Simple Stemming Algorithm for German Words" by Jörg Caumanns
 * (joerg.caumanns@isst.fhg.de).
 * 
 * @author Gerhard Schwarz
 */
public class GermanStemming {
	/**
	 * Buffer for the terms while stemming them.
	 */
	private StringBuffer sb = new StringBuffer();

	/**
	 * Amount of characters that are removed with substitute() while stemming.
	 */
	private int substCount = 0;

	/**
	 * Stemms the given term to an unique discriminator.
	 * 
	 * @param term
	 *            The term that should be stemmed.
	 * @return Discriminator for term
	 */
	public String stem(String term) {
		// Use lowercase for medium stemming.
		term = term.toLowerCase();
		if (!isStemmable(term))
			return term;
		// Reset the StringBuffer.
		sb.delete(0, sb.length());
		sb.insert(0, term);
		// Stemming starts here...
		substitute(sb);
		strip(sb);
		optimize(sb);
		resubstitute(sb);
		removeParticleDenotion(sb);
		return sb.toString();
	}

	/**
	 * Checks if a term could be stemmed.
	 * 
	 * @return true if, and only if, the given term consists in letters.
	 */
	private boolean isStemmable(String term) {
		for (int c = 0; c < term.length(); c++) {
			if (!Character.isLetter(term.charAt(c)))
				return false;
		}
		return true;
	}

	/**
	 * suffix stripping (stemming) on the current term. The stripping is reduced
	 * to the seven "base" suffixes "e", "s", "n", "t", "em", "er" and * "nd",
	 * from which all regular suffixes are build of. The simplification causes
	 * some overstemming, and way more irregular stems, but still provides
	 * unique. discriminators in the most of those cases. The algorithm is
	 * context free, except of the length restrictions.
	 */
	private void strip(StringBuffer buffer) {
		boolean doMore = true;
		while (doMore && buffer.length() > 3) {
			if ((buffer.length() + substCount > 5)
					&& buffer.substring(buffer.length() - 2, buffer.length())
							.equals("nd")) {
				buffer.delete(buffer.length() - 2, buffer.length());
			} else if ((buffer.length() + substCount > 4)
					&& buffer.substring(buffer.length() - 2, buffer.length())
							.equals("em")) {
				buffer.delete(buffer.length() - 2, buffer.length());
			} else if ((buffer.length() + substCount > 4)
					&& buffer.substring(buffer.length() - 2, buffer.length())
							.equals("er")) {
				buffer.delete(buffer.length() - 2, buffer.length());
			} else if (buffer.charAt(buffer.length() - 1) == 'e') {
				buffer.deleteCharAt(buffer.length() - 1);
			} else if (buffer.charAt(buffer.length() - 1) == 's') {
				buffer.deleteCharAt(buffer.length() - 1);
			} else if (buffer.charAt(buffer.length() - 1) == 'n') {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			// "t" occurs only as suffix of verbs.
			else if (buffer.charAt(buffer.length() - 1) == 't') {
				buffer.deleteCharAt(buffer.length() - 1);
			} else {
				doMore = false;
			}
		}
	}

	/**
	 * Does some optimizations on the term. This optimisations are contextual.
	 */
	private void optimize(StringBuffer buffer) {
		// Additional step for female plurals of professions and inhabitants.
		if (buffer.length() > 5
				&& buffer.substring(buffer.length() - 5, buffer.length())
						.equals("erin*")) {
			buffer.deleteCharAt(buffer.length() - 1);
			strip(buffer);
		}
		// Additional step for irregular plural nouns like "Matrizen -> Matrix".
		if (buffer.charAt(buffer.length() - 1) == ('z')) {
			buffer.setCharAt(buffer.length() - 1, 'x');
		}
	}

	/**
	 * Removes a particle denotion ("ge") from a term.
	 */
	private void removeParticleDenotion(StringBuffer buffer) {
		if (buffer.length() > 4) {
			for (int c = 0; c < buffer.length() - 3; c++) {
				if (buffer.substring(c, c + 4).equals("gege")) {
					buffer.delete(c, c + 2);
					return;
				}
			}
		}
	}

	/**
	 * Do some substitutions for the term to reduce overstemming:
	 * 
	 * - Substitute Umlauts with their corresponding vowel: äöü -> aou, "ß" is
	 * substituted by "ss" - Substitute a second char of a pair of equal
	 * characters with an asterisk: ?? -> ?* - Substitute some common character
	 * combinations with a token: sch/ch/ei/ie/ig/st -> $/§/%/&/#/!
	 */
	private void substitute(StringBuffer buffer) {
		substCount = 0;
		for (int c = 0; c < buffer.length(); c++) {
			// Replace the second char of a pair of the equal characters with an
			// asterisk
			if (c > 0 && buffer.charAt(c) == buffer.charAt(c - 1)) {
				buffer.setCharAt(c, '*');
			}
			// Substitute Umlauts.
			else if (buffer.charAt(c) == 'ä') {
				buffer.setCharAt(c, 'a');
			} else if (buffer.charAt(c) == 'ö') {
				buffer.setCharAt(c, 'o');
			} else if (buffer.charAt(c) == 'ü') {
				buffer.setCharAt(c, 'u');
			}
			// Take care that at least one character is left left side from the
			// current one
			if (c < buffer.length() - 1) {
				if (buffer.charAt(c) == 'ß') {
					buffer.setCharAt(c, 's');
					buffer.insert(c + 1, 's');
					substCount++;
				}
				// Masking several common character combinations with an token
				else if ((c < buffer.length() - 2) && buffer.charAt(c) == 's'
						&& buffer.charAt(c + 1) == 'c'
						&& buffer.charAt(c + 2) == 'h') {
					buffer.setCharAt(c, '$');
					buffer.delete(c + 1, c + 3);
					substCount = +2;
				} else if (buffer.charAt(c) == 'c'
						&& buffer.charAt(c + 1) == 'h') {
					buffer.setCharAt(c, '§');
					buffer.deleteCharAt(c + 1);
					substCount++;
				} else if (buffer.charAt(c) == 'e'
						&& buffer.charAt(c + 1) == 'i') {
					buffer.setCharAt(c, '%');
					buffer.deleteCharAt(c + 1);
					substCount++;
				} else if (buffer.charAt(c) == 'i'
						&& buffer.charAt(c + 1) == 'e') {
					buffer.setCharAt(c, '&');
					buffer.deleteCharAt(c + 1);
					substCount++;
				} else if (buffer.charAt(c) == 'i'
						&& buffer.charAt(c + 1) == 'g') {
					buffer.setCharAt(c, '#');
					buffer.deleteCharAt(c + 1);
					substCount++;
				} else if (buffer.charAt(c) == 's'
						&& buffer.charAt(c + 1) == 't') {
					buffer.setCharAt(c, '!');
					buffer.deleteCharAt(c + 1);
					substCount++;
				}
			}
		}
	}

	/**
	 * Undoes the changes made by substitute(). That are character pairs and
	 * character combinations. Umlauts will remain as their corresponding vowel,
	 * as "ß" remains as "ss".
	 */
	private void resubstitute(StringBuffer buffer) {
		for (int c = 0; c < buffer.length(); c++) {
			if (buffer.charAt(c) == '*') {
				char x = buffer.charAt(c - 1);
				buffer.setCharAt(c, x);
			} else if (buffer.charAt(c) == '$') {
				buffer.setCharAt(c, 's');
				buffer.insert(c + 1, new char[] { 'c', 'h' }, 0, 2);
			} else if (buffer.charAt(c) == '§') {
				buffer.setCharAt(c, 'c');
				buffer.insert(c + 1, 'h');
			} else if (buffer.charAt(c) == '%') {
				buffer.setCharAt(c, 'e');
				buffer.insert(c + 1, 'i');
			} else if (buffer.charAt(c) == '&') {
				buffer.setCharAt(c, 'i');
				buffer.insert(c + 1, 'e');
			} else if (buffer.charAt(c) == '#') {
				buffer.setCharAt(c, 'i');
				buffer.insert(c + 1, 'g');
			} else if (buffer.charAt(c) == '!') {
				buffer.setCharAt(c, 's');
				buffer.insert(c + 1, 't');
			}
		}
	}
}
