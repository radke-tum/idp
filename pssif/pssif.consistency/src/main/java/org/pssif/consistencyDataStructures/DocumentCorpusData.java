package org.pssif.consistencyDataStructures;

/**
 * Source for method: Paper:
 *        "Improving Requirements Tracing via Information Retrieval"
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pssif.mainProcesses.MatchingProcess;
import org.pssif.mainProcesses.Methods;
import org.pssif.textNormalization.Normalizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 * 
 */
public class DocumentCorpusData {

	private LinkedList<Token> tokens;
	private Model originalModel;
	private Model newModel;
	private Metamodel metaModel;

	/**
	 * this variable counts the total number of documents(here represented by
	 * nodes)
	 */
	private double numberOfDocuments;

	private MatchingProcess matchingProcess;
	private Normalizer normalizer;

	public DocumentCorpusData(Normalizer normalizer,
			MatchingProcess matchingProcess) {

		this.normalizer = normalizer;
		this.matchingProcess = matchingProcess;
		this.tokens = new LinkedList<Token>();

	}

	public void iterateOverAllNodes() {
		// TODO Don't get these fields again here. It's already given to the
		// match method
		Model originalModel = matchingProcess.getOriginalModel();
		Model newModel = matchingProcess.getNewModel();
		Metamodel metaModel = matchingProcess.getMetaModel();

		addNodes(PSSIFConstants.ROOT_NODE_TYPE_NAME, originalModel, metaModel);

		addNodes(PSSIFConstants.ROOT_NODE_TYPE_NAME, newModel, metaModel);
	}

	/**
	 * @param type
	 *            the type of nodes which are looked up in the given model to do
	 *            the compairson
	 * @param model
	 *            TODO
	 * @param metaModel
	 *            TODO
	 * 
	 */
	private void addNodes(String type, Model model, Metamodel metaModel) {

		NodeType actTypeModel;
		PSSIFOption<Node> actNodesModel;

		actTypeModel = metaModel.getNodeType(type).getOne();

		actNodesModel = actTypeModel.apply(model, true);

		if (actNodesModel.isNone()) {
			throw new RuntimeException(
					"Document corpus for IR Methods couldn't be built because there are no nodes in one of the models");
		} else {
			if (actNodesModel.isOne()) {

				Node tempNode = actNodesModel.getOne();

				addTokens(tempNode, actTypeModel);

				numberOfDocuments++;

			} else {

				Set<Node> tempNodes = actNodesModel.getMany();

				Iterator<Node> tempNode = tempNodes.iterator();

				while (tempNode.hasNext()) {

					addTokens(tempNode.next(), actTypeModel);

					numberOfDocuments++;
				}

			}
		}
	}

	private void addTokens(Node tempNode, NodeType actTypeModel) {
		String labelNode = Methods.findName(actTypeModel, tempNode);

		List<Token> tempTokens = normalizer.createNormalizedTokensFromLabel(
				labelNode, true, true, false, true);

		for (Token token : tempTokens) {
			addTokenToData(token);
		}
	}

	private void addTokenToData(Token token) {
		boolean tokenAlreadyStored = false;

		if (tokens.isEmpty()) {
			tokens.add(token);
		} else {

			for (Token savedToken : tokens) {
				if (savedToken.getWord().equals(token.getWord())) {
					tokenAlreadyStored = true;
					savedToken.incrementDocumentCounter();
					break;
				}
			}
			if (!tokenAlreadyStored) {
				token.incrementDocumentCounter();
				tokens.add(token);
			}
		}

	}

	public void computeIDFWeigths() {
		double idf = 0;

		for (Token savedToken : tokens) {
			idf = Methods.logarithmBaseTwo(numberOfDocuments
					/ (savedToken.getDocumentCounter()));
			savedToken.setIdf(idf);
		}
	}

	public List<Token> getFullTokenList(List<Token> tokensOrigin) {
		List<Token> result = new LinkedList<Token>();
		boolean tokenFound = false;
		Token tempOrigin;
		
		for (Token savedToken : tokens) {
			tokenFound = false;
			
			for (int i = 0; i < tokensOrigin.size(); i++) {
				tempOrigin = tokensOrigin.get(i);

				if (tempOrigin.getWord().equals(savedToken.getWord())) {
					tokenFound = true;

					tempOrigin.setIdf(savedToken.getIdf());
					tempOrigin.computeWordWeight();

					result.add(tempOrigin);
					
					break;
				}
				if ((!tokenFound) && (i == (tokensOrigin.size() - 1))) {
					result.add(null);
				}
			}
		}
		return result;
	}

}
