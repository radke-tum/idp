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
 * !!!Source for metric:
 * "Measuring Similarity between Business Process Models.pdf"
 * 
 * This class represents an impelementation of the linguistic Matching. Thereby
 * the similarity of two nodes is calculated exact matches between tokens of
 * their labels or synonym matching betwwen tokens. Thereby the exact matching
 * is weighted higher than a match found with help of a synonym lexicon.
 * 
 * @author Andreas
 * 
 */
public class LinguisticMatcher extends MatchMethod {

	public LinguisticMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
	}

	/**
	 * This variable represents the tokens which both, the token set of the
	 * original Node and the token set of the new Node, have in common
	 */
	private List<Token> intersectionOfTokenLists;

	/**
	 * This variable represents the unique tokens (in compairson to the new node
	 * tokens) of the original node
	 */
	private List<Token> uniqueTokensOrigin;

	/**
	 * This variable represents the unique tokens (in compairson to the original
	 * node tokens) of the new node
	 */
	private List<Token> uniqueTokensNew;

	/**
	 * This method calculates the linguistic similarity. Thereby it searches for
	 * tokens which both token sets have in common and which tokens of the two
	 * token sets are synonyms. If a token is found which both sets have in
	 * common 1 is added to the numerator. If a synonym is found 0.75 is added
	 * to the numerator. Then the numerator is divided through the maximal
	 * length of the two token sets.
	 * 
	 * @return result the result of the linguistic matching
	 */
	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		double result = 0;

		initializeIntersectionAndUniqueOriginTokenList(tokensOrigin, tokensNew);
		initializeUniqueNewTokenList(tokensOrigin, tokensNew);

		double numerator = 1.0 * intersectionOfTokenLists.size() + 0.75
				* countSynonyms();
		double denominator;

		if (tokensOrigin.size() >= tokensNew.size()) {
			denominator = tokensOrigin.size();
		} else {
			denominator = tokensNew.size();
		}

		result = (numerator / denominator);

		if (result > 1) {
			return 1.0;
		} else {
			return result;
		}
	}

	/**
	 * @return the number of Synonyms between two unique token sets
	 */
	private int countSynonyms() {
		int numberOfSynonyms = 0;

		for (Token tokenOrigin : uniqueTokensOrigin) {
			for (Token tokenNew : uniqueTokensNew) {
				if (areSyonyms(tokenOrigin, tokenNew)) {
					numberOfSynonyms++;
				}
			}
		}

		return numberOfSynonyms;
	}

	/**
	 * @param tokenOrigin
	 *            a token from the token set of the original node
	 * @param tokenNew
	 *            a token from the token set of the new node
	 * @return a bool saying whether the two given tokens are synonyms(true
	 *         means they are synonyms, false means they are no synonyms)
	 */
	private boolean areSyonyms(Token tokenOrigin, Token tokenNew) {
		return (wordNetQuery.areSynonyms(tokenOrigin, tokenNew));
	}

	/**
	 * This method initializes the list of tokens which appear uniquely in the
	 * new nodes label
	 * 
	 * @param tokensOrigin
	 *            the tokenset of the original node
	 * @param tokensNew
	 *            the tokenset of the new node
	 * 
	 */
	private void initializeUniqueNewTokenList(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		uniqueTokensNew = new LinkedList<Token>();

		boolean uniqueTokenNew = false;

		for (Token tokenNew : tokensNew) {
			uniqueTokenNew = true;
			for (Token tokenOrigin : tokensOrigin) {
				if (tokenOrigin.getWord().equals(tokenNew.getWord())) {
					uniqueTokenNew = false;
				}
			}
			if (uniqueTokenNew) {
				uniqueTokensNew.add(tokenNew);
			}
		}
	}

	/**
	 * This method initializes the list of tokens which appear uniquely in the
	 * origin nodes label
	 * 
	 * @param tokensOrigin
	 *            the tokenset of the original node
	 * @param tokensNew
	 *            the tokenset of the new node
	 * 
	 * 
	 */
	private void initializeIntersectionAndUniqueOriginTokenList(
			List<Token> tokensOrigin, List<Token> tokensNew) {
		intersectionOfTokenLists = new LinkedList<Token>();
		uniqueTokensOrigin = new LinkedList<Token>();

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
				uniqueTokensOrigin.add(tokenOrigin);
			}
		}
	}
}
