package org.pssif.matchingLogic;

/**
 *  Source for method: Paper:
 *         "Improving Requirements Tracing via Information Retrieval"
 */

import java.util.List;

import org.pssif.consistencyDataStructures.DocumentCorpusData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.mainProcesses.MatchingProcess;
import org.pssif.textNormalization.Normalizer;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 * 
 */
public class VsmMatcher extends MatchMethod {

	private DocumentCorpusData corpusData;
	private MatchingProcess matchingProcess;
	private Normalizer normalizer;

	public VsmMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {

		double result = 0;

		computeTFWeigths(tokensOrigin);
		computeTFWeigths(tokensNew);

		result = calculateCosineSimilarity(
				corpusData.getFullTokenList(tokensOrigin),
				corpusData.getFullTokenList(tokensNew));

		return result;
	}

	// TODO eventually add thesaurus match here as seen in paper
	private double calculateCosineSimilarity(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		double cosine = 1;

		double numerator = 0;
		double denominator = 1;
		double tempDenominatorOrigin = 0, tempDenominatorNew = 0;

		Token tempOrigin, tempNew;

		for (int i = 0; i < tokensOrigin.size(); i++) {
			tempOrigin = tokensOrigin.get(i);
			tempNew = tokensNew.get(i);

			if ((tempOrigin != null) && (tempNew != null)) {
				numerator += tempOrigin.getWordWeigth()
						* tempNew.getWordWeigth();
			}

			if (tempOrigin != null) {
				tempDenominatorOrigin += Math.pow(tokensOrigin.get(i)
						.getWordWeigth(), 2);
			}
			if (tempNew != null) {
				tempDenominatorNew += Math.pow(
						tokensNew.get(i).getWordWeigth(), 2);
			}
		}

		denominator = Math.sqrt(tempDenominatorOrigin * tempDenominatorNew);

		cosine = (numerator / denominator);

		return cosine;
	}

	public void initializeDocumentCorpus(Normalizer normalizer,
			MatchingProcess matchingProcess) {

		this.normalizer = normalizer;
		this.matchingProcess = matchingProcess;

		corpusData = new DocumentCorpusData(normalizer, matchingProcess);
		corpusData.iterateOverAllNodes();

	}

	public void computeIDFWeigths() {
		corpusData.computeIDFWeigths();
	}

	private void computeTFWeigths(List<Token> tokensOrigin) {

		double tokenCounter = 0;

		for (Token actToken : tokensOrigin) {
			if (actToken.getTf() == 0) {
				for (Token actToken2 : tokensOrigin) {
					if (actToken.getWord().equals(actToken2.getWord())) {
						tokenCounter++;
					}
				}
				actToken.setTf(tokenCounter);
			}
		}
	}

}
