package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.mainProcesses.Methods;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class ContextMatcher extends MatchMethod {

	private List<MatchMethod> matchMethods;
	private ConsistencyData consistencyData;

	private ComparedNodePair comparedNodePair;

	public ContextMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param matchMethods
	 *            the matchMethods to set
	 */
	public void setMatchMethods(List<MatchMethod> matchMethods) {
		this.matchMethods = matchMethods;
	}

	/**
	 * @param consistencyData
	 *            the consistencyData to set
	 */
	public void setConsistencyData(ConsistencyData consistencyData) {
		this.consistencyData = consistencyData;
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		// PSSIFOption<Edge> incomingEdgesOrigin = tempNodeOrigin.apply(new
		// ReadIncomingNodesOperation(mapping));

		// TODO lookup wirh method nodesAlreadyCompared() if two nodes have
		// already been commpared and use the results from the last match here

		return 0;
	}

	/**
	 *TODO
	 * @param tempNodeOrigin
	 * @param actTypeOriginModel
	 * @param tempNodeNew
	 * @param actTypeNewModel
	 * @return
	 */
	private boolean nodesAlreadyCompared(Node tempNodeOrigin,
			NodeType actTypeOriginModel, Node tempNodeNew,
			NodeType actTypeNewModel) {

		String globalIDNodeOrigin = Methods.findGlobalID(tempNodeOrigin,
				actTypeOriginModel);
		String globalIDNodeNew = Methods.findGlobalID(tempNodeNew,
				actTypeNewModel);

		comparedNodePair = consistencyData.getNodeMatch(globalIDNodeOrigin,
				globalIDNodeNew);

		return(comparedNodePair != null);
	}

}
