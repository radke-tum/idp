package org.pssif.consistencyLogic;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public abstract class MatchMethod {

	/**
	 * @param matchMethod
	 * @param isActive
	 * @param weigth
	 */
	public MatchMethod(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super();
		this.matchMethod = matchMethod;
		this.isActive = isActive;
		this.weigth = weigth;
	}

	final MatchingMethods matchMethod;
	private boolean isActive;
	private double weigth;

	/**
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param tempNodeNew
	 *            the node form the new model
	 * @param originalModel
	 *            the first imported model
	 * @param newModel
	 *            the recent imported model
	 * @param metaModel
	 *            the accroding metamodel for the two models
	 * @param actTypeOriginModel
	 *            the actual type of the originalNode
	 * @param actTypeNewModel
	 *            the actual type of the newNode
	 * @param labelOrigin
	 *            TODO
	 * @param labelNew
	 *            TODO
	 * @param tokensOrigin
	 *            TODO
	 * @param tokensNew
	 *            TODO
	 * @return the result of the applied metric
	 */
	public abstract double executeMatching(Node tempNodeOrigin,
			Node tempNodeNew, Model originalModel, Model newModel,
			Metamodel metaModel, NodeType actTypeOriginModel,
			NodeType actTypeNewModel, String labelOrigin, String labelNew,
			Token[] tokensOrigin, Token[] tokensNew);

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

}
