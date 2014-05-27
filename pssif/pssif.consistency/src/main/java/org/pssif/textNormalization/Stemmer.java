package org.pssif.textNormalization;

import java.util.ArrayList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

public class Stemmer {

	private final GermanStemmingOperator stemmer;
	
	public Stemmer(){
		stemmer = new GermanStemmingOperator();
	}
	
	public List<Token> stemTokens(List<Token> tokens){
		return (stemmer.doWork(tokens));
	}
	
	
	/**
	 * Source: GermanStemming Operator (modified)
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

	/**
	 * A fast german stemmer, based on the algorithm in the report
	 * "A Fast and Simple Stemming Algorithm for German Words" by Joerg
	 * Caumanns (joerg.caumanns@isst.fhg.de).
	 *
	 * 
	 * @author Michael Wurst
	 */
	private class GermanStemmingOperator {

		public List<Token> doWork(List<Token> tokens) {
			GermanStemming stemmer = new GermanStemming();
			List<Token> newSequence = new ArrayList<Token>(tokens.size());
			
			for (Token token: tokens) {
				if (!token.getWord().isEmpty())
					token.setWord(stemmer.stem(token.getWord()));
					newSequence.add(token);
			}
			return newSequence;
			
		}
	}
	
}
