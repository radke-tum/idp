package org.pssif.matchingLogic;

import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import wordNet.wordNetQuery;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         !!!Source for metric:
 *         "Measuring Similarity between Business Process Models.pdf"
 * 
 */
public class LinguisticMatcher extends MatchMethod {

	public LinguisticMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(MatchingMethods.LINGUISTIC_MATCHING, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	private List<Token> intersectionOfTokenLists;
	private List<Token> uniqueTokensOriginList;
	private List<Token> uniqueTokensNewList;

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		double result = 0;

		initializeIntersection(tokensOrigin, tokensNew);
		initializeUniqueNewTokenList(tokensOrigin, tokensNew);

		double numerator = 1.0 * intersectionOfTokenLists.size() + 0.75
				* countSynonyms();
		double denominator;
		
		if (tokensOrigin.size() >= tokensNew.size()) {
			denominator = tokensOrigin.size();
		} else {
			denominator = tokensNew.size();
		}

		result = (numerator/denominator);
		
		return result;
	}

	private int countSynonyms() {
		int numberOfSynonyms = 0;

		for (Token tokenOrigin : uniqueTokensOriginList) {
			for (Token tokenNew : uniqueTokensNewList) {
				if (areSyonyms(tokenOrigin, tokenNew)) {
					numberOfSynonyms++;
				}
			}
		}

		return numberOfSynonyms;
	}

	private boolean areSyonyms(Token tokenOrigin, Token tokenNew) {
		return (wordNetQuery.areSynonyms(tokenOrigin, tokenNew));
	}

	private void initializeUniqueNewTokenList(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		uniqueTokensNewList = new LinkedList<Token>();

		boolean uniqueTokenNew = false;

		for (Token tokenNew : tokensNew) {
			uniqueTokenNew = true;
			for (Token tokenOrigin : tokensOrigin) {
				if (tokenOrigin.getWord().equals(tokenNew.getWord())) {
					uniqueTokenNew = false;
				}
			}
			if (uniqueTokenNew) {
				uniqueTokensNewList.add(tokenNew);
			}
		}
	}

	private void initializeIntersection(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		intersectionOfTokenLists = new LinkedList<Token>();
		uniqueTokensOriginList = new LinkedList<Token>();

		boolean uniqueTokenOrigin = false;

		for (Token tokenOrigin : tokensOrigin) {
			uniqueTokenOrigin = true;
			for (Token tokenNew : tokensNew) {
				if (tokenOrigin.getWord().equals(tokenNew.getWord())) {
					uniqueTokenOrigin = false;
					if (!(intersectionOfTokenLists.contains(tokenOrigin))) {
						intersectionOfTokenLists.add(tokenOrigin);
					}
				}
			}
			if (uniqueTokenOrigin) {
				uniqueTokensOriginList.add(tokenOrigin);
			}
		}
	}
}
