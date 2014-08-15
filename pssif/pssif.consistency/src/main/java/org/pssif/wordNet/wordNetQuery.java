package org.pssif.wordNet;

import org.pssif.consistencyDataStructures.Token;

public class wordNetQuery {

	// TODO establish connection with wordNet database here and check if the
	// two given tokens are synonyms;
	public static boolean areSynonyms(Token tokenOrigin, Token tokenNew) {
		
		System.out.println("Error: The WordNet database couln't be found!");
		
		return false;		
	}
}
