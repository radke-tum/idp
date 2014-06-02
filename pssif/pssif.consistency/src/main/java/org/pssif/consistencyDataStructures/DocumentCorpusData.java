package org.pssif.consistencyDataStructures;

/**
 * Source for method: Paper:
 *        "Improving Requirements Tracing via Information Retrieval"
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pssif.mainProcesses.MatchingProcess;
import org.pssif.mainProcesses.Methods;

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

	private HashSet<Token> tokens;

	/**
	 * this variable counts the total number of documents(here represented by
	 * nodes)
	 */
	private double numberOfDocuments;

	private MatchingProcess matchingProcess;

	public DocumentCorpusData(MatchingProcess matchingProcess) {

		this.matchingProcess = matchingProcess;

	}

	public void iterateOverAllNodes() {
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
					"Document corpus couldn't be built because there are no nodes in one of the models");
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
		
		List<Token> tempTokens = matchingProcess.getNormalizer().createNormalizedTokensFromLabel(labelNode, true, true, false, true);
		
		for(Token token : tempTokens){
			addTokenToData(token);
		}
	}
	
	private void addTokenToData(Token token) {
		boolean tokenAlreadyStored = false;

		for (Token actToken : tokens) {
			if (actToken.getWord().equals(token.getWord())) {
				tokenAlreadyStored = true;
				actToken.incrementDocumentCounter();
			}
		}
		if (!tokenAlreadyStored) {
			token.incrementDocumentCounter();
			tokens.add(token);
		}

	}

	public void computeIDFWeigths() {
		double idf = 0;
		
		for(Token actToken : tokens){
			idf = Methods.logarithmBaseTwo(numberOfDocuments/(actToken.getDocumentCounter()));
			actToken.setIdf(idf);
		}
	}

	public void setIDFWeigths(List<Token> tokensOrigin) {
		for(Token actToken : tokensOrigin){
			for(Token savedToken : tokens){
				if(actToken.getWord().equals(savedToken.getWord())){
					actToken.setTf(savedToken.getTf());
					break;
				}
			}
		}
	}
}
