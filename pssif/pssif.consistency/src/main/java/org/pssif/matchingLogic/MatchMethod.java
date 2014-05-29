package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         This class represents a general structure of a matching Method. It
 *         supplies the standard attributes for a matching method to say how the
 *         method is called, if it's active and how much is its weight to the
 *         whole similarity score.
 * 
 *         New Matching methods can be easily implemented by extending this
 *         class and implementing an own version of the executeMatching()
 *         method.
 * 
 */
public abstract class MatchMethod {

	/**
	 * @param matchMethod TODO
	 * @param isActive
	 *            bool saying whether the method was activated by the user
	 * @param weigth
	 *            the method weight to the whole similarity score of two nodes
	 */
	public MatchMethod(MatchingMethods matchMethod, boolean isActive, double weigth) {
		super();
		this.matchMethod = matchMethod;
		this.isActive = isActive;
		this.weigth = weigth;
	}

	final MatchingMethods matchMethod;
	private boolean isActive;
	private double weigth;

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
	 * TODO Comment
	 * @param matchMethod
	 * @param isActive
	 * @param weigth
	 * @return
	 */
	public static MatchMethod createMatchMethodObject(MatchingMethods matchMethod, boolean isActive, double weigth){
		//TODO Initialize with null
		MatchMethod newMatchMethod = new ExactMatcher(MatchingMethods.EXACT_STRING_MATCHING, isActive, weigth);
		
		switch(matchMethod){
		case EXACT_STRING_MATCHING:
			newMatchMethod = new ExactMatcher(MatchingMethods.EXACT_STRING_MATCHING, isActive, weigth);
			break;
		case DEPTH_MATCHING:
			//TODO create matchMethod
			break;
		case STRING_EDIT_DISTANCE_MATCHING:
			newMatchMethod = new StringEditDistanceMatcher(MatchingMethods.STRING_EDIT_DISTANCE_MATCHING,isActive, weigth);
			break;
		case HYPHEN_MATCHING:
			//TODO create matchMethod
			break;
		case LINGUISTIC_MATCHING:
			newMatchMethod = new LinguisticMatcher(MatchingMethods.LINGUISTIC_MATCHING, isActive, weigth);
			break;
		case VECTOR_SPACE_MODEL_MATCHING:
			//TODO create matchMethod
			break;
		case LATENT_SEMANTIC_INDEXING_MATCHING:
			//TODO create matchMethod
			break;
		case CONTEXT_MATCHING:
			//TODO create matchMethod
			break;
			default:
				throw new RuntimeException("Couldn't create a correct match method with the given match type!");
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
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @param weigth the weigth to set
	 */
	public void setWeigth(double weigth) {
		this.weigth = weigth;
	}

}
