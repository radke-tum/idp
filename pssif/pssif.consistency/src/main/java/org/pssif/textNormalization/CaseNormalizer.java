package org.pssif.textNormalization;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

/**
This project is part of the Product-Service System integration framework. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Feel free to contact me via eMail: genz@in.tum.de
*/

public class CaseNormalizer {

	public List<Token> convertTokensToLowerCase(List<Token> tokens) {

		String temp;

		for (Token token : tokens) {

			temp = token.getWord();

			/**
			 * check if the tokens word doesnt consist of upper case letters
			 * only (if it only has uppercase letters it is assumed to be an
			 * abberevation and remains unnormalized)
			 */
			if (!(temp.toUpperCase() == temp)) {
				temp = temp.toLowerCase();
				token.setWord(temp);
			}
		}

		return tokens;
	}

}
