package org.pssif.mainProcesses;

import java.util.Iterator;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.textNormalization.Normalizer;
import org.pssif.textNormalization.Tokenizer;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas
 * 
 *         This class is responsible for conducting the whole matching process.
 *         It takes two nodes and applies all active matching methods to them.
 *         Afterwards it saves the idpair of the two nodes so they won't be
 *         compared again.
 */
public class MatchingProcess {

	/**
	 * @param originalModel
	 *            the model which was first imported
	 * @param newModel
	 *            the model which was recently imported
	 * @param metaModel
	 *            the metamodel according to the two models
	 * @param consistencyData
	 *            the data of the matching process will be stored here and can
	 *            be accesed both by the Comparsion and the matching process
	 * @param matchMethods
	 *            these are the matching metrics for the current matching
	 *            operation
	 */
	public MatchingProcess(Model originalModel, Model newModel,
			Metamodel metaModel, ConsistencyData consistencyData,
			List<MatchMethod> matchMethods) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModel = metaModel;
		this.consistencyData = consistencyData;
		this.matchMethods = matchMethods;

		this.normalizer = Normalizer.initialize(matchMethods);
	}

	private Model originalModel, newModel;
	private Metamodel metaModel;

	private ConsistencyData consistencyData;

	private List<MatchMethod> matchMethods;
	
	private Normalizer normalizer;

	private ComparedLabelPair comparedLabelPair = null;
	private ComparedNormalizedTokensPair comparedNormalizedTokensPair = null;
	private ComparedNodePair comparedNodePair = null;

	/**
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeOriginModel
	 *            the type of the tempNodeOrigin
	 * @param actTypeNewModel
	 *            the type of the tempNodeNew
	 * @throws RuntimeException
	 *             If something at the saving goes wrong an exception is thrown.
	 *             Else nothing besides the saving happens.
	 * 
	 *             This method guides the whole matching process. It initializes
	 *             the variables where the consistencyData will be stored later.
	 *             Then it normalizes and/or tokenizes the labels if necessary.
	 *             Then it applies the active matching methods to the nodes and
	 *             saves the results
	 */
	public void startMatchingProcess(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {

		double currentMetricResult = 0;

		/**
		 * initializing the consistency Data variables here
		 */
		comparedLabelPair = null;
		comparedNormalizedTokensPair = null;
		comparedNodePair = new ComparedNodePair();

		/**
		 * here the strings of the old and the new node are read from the model.
		 */
		String labelOrigin = Methods.findName(actTypeOriginModel, tempNodeOrigin);
		String labelNew = Methods.findName(actTypeNewModel, tempNodeNew);

		//TODO Remove after testing
		
		boolean normalizeCases = true;
		boolean filterStopwords = true;
		boolean splitWords = true;
		boolean stemWords = true;

		
		//TODO until here
		
		List<Token> tokensOrigin = normalizer.createNormalizedTokensFromLabel(labelOrigin, normalizeCases, filterStopwords, splitWords, stemWords);
		List<Token> tokensNew = normalizer.createNormalizedTokensFromLabel(labelNew, normalizeCases, filterStopwords, splitWords, stemWords);
		
		labelOrigin = normalizer.normalizeLabel(labelOrigin);
		labelNew = normalizer.normalizeLabel(labelNew);
		
		System.out.println("The normalized label of the original Node: " + labelOrigin);
		System.out.println("The normalized label of the new Node: " + labelNew);

		
		createComparedNormalizedTokensPair(tokensOrigin, tokensNew);

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		/**
		 * Applying every match Method to the two nodes here
		 */
		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			currentMetricResult = 0;

			if (currentMethod.isActive()) {
				currentMetricResult = currentMethod.executeMatching(
						tempNodeOrigin, tempNodeNew, originalModel, newModel,
						metaModel, actTypeOriginModel, actTypeNewModel,
						labelOrigin, labelNew, tokensOrigin, tokensNew);
			}

			saveMatchMethodResult(currentMethod, currentMetricResult,
					labelOrigin, labelNew);
		}

		saveComparedNodePaier(tempNodeOrigin, tempNodeNew, actTypeOriginModel,
				actTypeNewModel);

		/**
		 * Saves the two compared nodes, the ids and the matching results in the
		 * consistencyData object. If something goes wrong an exception is
		 * thrown. Else nothing besides the saving happens.
		 */
		if (!(consistencyData.putComparedEntry(comparedNodePair))) {
			throw new RuntimeException(
					"Something went wrong with the saving of the recently matched elements.");
		}
		
		printResults(labelOrigin, labelNew);
	}

	/**
	 * @param labelOrigin
	 * @param labelNew
	 */
	private void printResults(String labelOrigin, String labelNew) {
		System.out.println("The node(origin): " + labelOrigin
				+ " and the node(new) " + labelNew
				+ " have the following similarieties:");
		System.out.println("Syntactic similarity: "
				+ getWeightedSyntacticSimilarity());
		System.out.println("Semantic similarity: "
				+ getWeightedSemanticSimilarity());
		System.out.println("Contextual Similarity: "
				+ getWeightedContextSimilarity());
	}

	/**TODO
	 * @param tempNodeOrigin
	 * @param tempNodeNew
	 * @param actTypeOriginModel
	 * @param actTypeNewModel
	 */
	private void saveComparedNodePaier(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {
		comparedNodePair.setLabelComparison(comparedLabelPair);
		comparedNodePair.setTokensComparison(comparedNormalizedTokensPair);
		comparedNodePair.setNodeTypes(actTypeOriginModel,actTypeNewModel);
		comparedNodePair.setNodeOriginalModel(tempNodeOrigin);
		comparedNodePair.setNodeNewModel(tempNodeNew);
	}

	/**
	 * @param currentMethod
	 *            the matchMethod which was currently applied
	 * @param currentMetricResult
	 *            the result of the currently applied matchMethod
	 * @param labelOrigin
	 * @param labelNew
	 * 
	 *            This method is supposed to save the results of the last match
	 *            operation. Depending on the matchMethod type it's saved in a
	 *            different matchData container.
	 */
	public void saveMatchMethodResult(MatchMethod currentMethod,
			double currentMetricResult, String labelOrigin, String labelNew) {
		
		switch (currentMethod.getMatchMethod()) {
		case EXACT_STRING_MATCHING:
			if (comparedLabelPair == null) {
				comparedLabelPair = new ComparedLabelPair(labelOrigin,
						labelNew, currentMetricResult);
			}
			break;
		case DEPTH_MATCHING:
			comparedNodePair.setDepthMatchResult(currentMetricResult);
			break;
		case STRING_EDIT_DISTANCE_MATCHING:
			comparedNormalizedTokensPair
					.setStringEditDistanceResult(currentMetricResult);
			break;
		case HYPHEN_MATCHING:
			comparedNormalizedTokensPair
					.setHyphenMatchResult(currentMetricResult);
			break;
		case LINGUISTIC_MATCHING:
			comparedNormalizedTokensPair
					.setLinguisticMatchResult(currentMetricResult);
			break;
		case VECTOR_SPACE_MODEL_MATCHING:
			comparedNormalizedTokensPair.setVsmMatchResult(currentMetricResult);
			break;
		case LATENT_SEMANTIC_INDEXING_MATCHING:
			comparedNormalizedTokensPair.setLsiMatchResult(currentMetricResult);
			break;
		case CONTEXT_MATCHING:
			comparedNodePair.setContextMatchResult(currentMetricResult);
			break;
		}
	}

	/**
	 * @param tokensOrigin
	 * @param tokensNew
	 * 
	 *            This method creates an object for the field
	 *            "comparedNormalizedTokensPair" if it's not yet created. This
	 *            ensures that every different token dependent metric is saved
	 *            into the same matchData container.
	 */
	private void createComparedNormalizedTokensPair(List<Token> tokensOrigin,
			List<Token> tokensNew) {
		if (comparedNormalizedTokensPair == null) {
			comparedNormalizedTokensPair = new ComparedNormalizedTokensPair(
					tokensOrigin, tokensNew);
		}
	}

	/**
	 * @return The weighted combination of all results of the syntactic match
	 *         methods.
	 */
	public double getWeightedSyntacticSimilarity() {
		double result = 0;

		double exactMatch = comparedLabelPair.getExactMatchResult();
		double depthMatch = comparedNodePair.getDepthMatchResult();
		double stringEditDistanceMatch = comparedNormalizedTokensPair
				.getStringEditDistanceResult();
		double hyphenMatch = comparedNormalizedTokensPair
				.getHyphenMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case EXACT_STRING_MATCHING:
				result += currentMethod.getWeigth() * exactMatch;
				break;
			case DEPTH_MATCHING:
				result += currentMethod.getWeigth() * depthMatch;
				break;
			case STRING_EDIT_DISTANCE_MATCHING:
				result += currentMethod.getWeigth() * stringEditDistanceMatch;
				break;
			case HYPHEN_MATCHING:
				result += currentMethod.getWeigth() * hyphenMatch;
				break;
			default:
				;
			}
		}
		return result;
	}

	/**
	 * @return The weighted combination of all results of the semantic match
	 *         methods.
	 */
	public double getWeightedSemanticSimilarity() {
		double result = 0;

		double linguisticMatch = comparedNormalizedTokensPair
				.getLinguisticMatchResult();
		double vsmMatch = comparedNormalizedTokensPair.getVsmMatchResult();
		double lsiMatch = comparedNormalizedTokensPair.getLsiMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case LINGUISTIC_MATCHING:
				result += currentMethod.getWeigth() * linguisticMatch;
				break;
			case VECTOR_SPACE_MODEL_MATCHING:
				result += currentMethod.getWeigth() * vsmMatch;
				break;
			case LATENT_SEMANTIC_INDEXING_MATCHING:
				result += currentMethod.getWeigth() * lsiMatch;
				break;
			default:
				;
			}
		}

		return result;
	}

	/**
	 * @return The weighted combination of all results of the context match
	 *         methods.
	 */
	public double getWeightedContextSimilarity() {
		double result = 0;

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case CONTEXT_MATCHING:
				result += currentMethod.getWeigth()
						* comparedNodePair.getContextMatchResult();
				break;
			default:
				;
			}
		}

		return result;
	}
}