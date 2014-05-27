package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;
import org.pssif.levenstheinDistance.Levenshtein;
import org.pssif.mainProcesses.Methods;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         This class represents a matcher based on the levenshtein distance. It
 *         calculates the weighted string edit distance and returns a similarity
 *         value in the interval [0,1]. The result is 1 if two Strings are equal
 *         and near zero if they are very unsimilar.
 */
public class StringEditDistanceMatcher extends MatchMethod {

	private final Levenshtein levenshtein = new Levenshtein();

	public StringEditDistanceMatcher(boolean isActive, double weigth) {
		super(MatchingMethods.STRING_EDIT_DISTANCE_MATCHING, isActive, weigth);
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		double result = 0;

		result = levenshtein.getSimilarity(
				Methods.getStringFromTokens(tokensOrigin),
				Methods.getStringFromTokens(tokensNew));

		return result;
	}

}
