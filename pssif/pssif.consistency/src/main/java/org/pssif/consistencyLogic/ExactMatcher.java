package org.pssif.consistencyLogic;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class ExactMatcher extends MatchMethod {

	public ExactMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, Token[] tokensOrigin,
			Token[] tokensNew) {
		// TODO Auto-generated method stub
		return 0;
	}

}
