package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * 
 * This class represents a general structure of a matching Method. It supplies
 * the standard attributes for a matching method to say how the method is
 * called, if it's active and how much is its weight to the whole similarity
 * score.
 * 
 * New Matching methods can be easily implemented by extending this class and
 * implementing an own version of the executeMatching() method.
 * 
 * @author Andreas
 * 
 */
public abstract class MatchMethod {

	/**
	 * @param matchMethod
	 *            the name of the initialized match method (instance of the enum
	 *            MatchingMethods)
	 * @param isActive
	 *            bool saying whether the method was activated by the user
	 * @param weigth
	 *            the method weight to the whole similarity score of two nodes
	 */
	public MatchMethod(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super();
		this.matchMethod = matchMethod;
		this.isActive = isActive;
		this.weigth = weigth;
	}

	final MatchingMethods matchMethod;
	protected boolean isActive;
	protected double weigth;

	/**
	 * 
	 * An abstract implementation of a matching method. Concrete implementations
	 * are to find in the classes extending this class
	 * 
	 * This methods get's very much information about the currently matched two
	 * nodes. Though not all matching methods require that much data, some
	 * methods need quite a lot.
	 * 
	 * @return the result of the applied metric
	 */
	public abstract double executeMatching(Node tempNodeOrigin,
			Node tempNodeNew, Model originalModel, Model newModel,
			Metamodel metaModel, NodeType actTypeOriginModel,
			NodeType actTypeNewModel, String labelOrigin, String labelNew,
			List<Token> tokensOrigin, List<Token> tokensNew);

	/**
	 * This method creates a MatchMethod of the given type and with the given
	 * attributes
	 * 
	 * @param matchMethod
	 *            The type of match method which shall be created
	 * @param isActive
	 *            detemrining whether the created method shall be active
	 * @param weight
	 *            thr weight of the created method
	 * @return the createtd method
	 * 
	 */
	public static MatchMethod createMatchMethodObject(
			MatchingMethods matchMethod, boolean isActive, double weight) {
		MatchMethod newMatchMethod = null;

		switch (matchMethod) {
		case EXACT_STRING_MATCHING:
			newMatchMethod = new ExactMatcher(matchMethod, isActive, weight);
			break;
		case DEPTH_MATCHING:
			newMatchMethod = new DepthMatcher(matchMethod, isActive, weight);
			break;
		case STRING_EDIT_DISTANCE_MATCHING:
			newMatchMethod = new StringEditDistanceMatcher(matchMethod,
					isActive, weight);
			break;
		case HYPHEN_MATCHING:
			newMatchMethod = new HyphenMatcher(matchMethod, isActive, weight);
			break;
		case LINGUISTIC_MATCHING:
			newMatchMethod = new LinguisticMatcher(matchMethod, isActive,
					weight);
			break;
		case VECTOR_SPACE_MODEL_MATCHING:
			newMatchMethod = new VsmMatcher(matchMethod, isActive, weight);
			break;
		case LATENT_SEMANTIC_INDEXING_MATCHING:
			newMatchMethod = new LsiMatcher(matchMethod, isActive, weight);
			break;
		case ATTRIBUTE_MATCHING:
			newMatchMethod = new AttributeMatcher(matchMethod, isActive, weight);
			break;
		case CONTEXT_MATCHING:
			newMatchMethod = new ContextMatcher(matchMethod, isActive, weight);
			break;
		default:
			throw new RuntimeException(
					"Couldn't create a correct match method with the given match type!");
		}

		return newMatchMethod;
	}

	/**
	 * @return the matchMethod
	 */
	public MatchingMethods getMatchMethod() {
		return matchMethod;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return the weigth
	 */
	public double getWeigth() {
		return weigth;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @param weigth
	 *            the weigth to set
	 */
	public void setWeigth(double weigth) {
		this.weigth = weigth;
	}

}
