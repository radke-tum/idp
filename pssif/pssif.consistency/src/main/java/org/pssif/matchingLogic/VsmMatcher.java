package org.pssif.matchingLogic;

/**
 *  Source for method: Paper:
 *         "Improving Requirements Tracing via Information Retrieval"
 */

import java.util.List;

import org.pssif.consistencyDataStructures.DocumentCorpusData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.mainProcesses.MatchingProcess;

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
		
		setIDFWeigths(tokensOrigin);
		setIDFWeigths(tokensNew);

		result = calculateCosine(tokensOrigin, tokensNew);

		return result;
	}


	//TODO eventually add thesaurus match here as seen in paper
	private double calculateCosine(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		double cosine = 1;
		
		double denominator = 0;
		double numerator = 0;
		//TODO Calculate cosine similarity here
	//	for()
		
		cosine = (numerator / denominator);
		
		return cosine;
	}

	public void initializeDocumentCorpus(MatchingProcess matchingProcess) {

		this.matchingProcess = matchingProcess;
		corpusData = new DocumentCorpusData(matchingProcess);
		corpusData.iterateOverAllNodes();

	}
	
	private void setIDFWeigths(List<Token> tokensOrigin) {
		corpusData.setIDFWeigths(tokensOrigin);
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
