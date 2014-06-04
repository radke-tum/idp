package org.pssif.mainProcesses;

import java.util.Iterator;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;
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

		this.normalizer = Normalizer.initialize(matchMethods, this);
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
	 * @return the originalModel
	 */
	public Model getOriginalModel() {
		return originalModel;
	}

	/**
	 * @return the newModel
	 */
	public Model getNewModel() {
		return newModel;
	}

	/**
	 * @return the metaModel
	 */
	public Metamodel getMetaModel() {
		return metaModel;
	}

	/**
	 * @return the normalizer
	 */
	public Normalizer getNormalizer() {
		return normalizer;
	}

	/**
	 * This method guides the whole matching process. It initializes the
	 * variables where the consistencyData will be stored later. Then it
	 * triggers the normalization and/or tokenization of the labels if
	 * necessary. Then it applies the active matching methods to the nodes and
	 * saves the results
	 * 
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
		 * here the strings of the old and the new node are read from the model
		 * and normalized.
		 */
		String labelOriginNode = Methods.findName(actTypeOriginModel,
				tempNodeOrigin);
		String labelNewNode = Methods.findName(actTypeNewModel, tempNodeNew);

		String labelOriginNodeNormalized = normalizer
				.normalizeLabel(labelOriginNode);
		String labelNewNodeNormalized = normalizer.normalizeLabel(labelNewNode);

		System.out.println("The normalized label of the original Node: "
				+ labelOriginNodeNormalized);
		System.out.println("The normalized label of the new Node: "
				+ labelNewNodeNormalized);

		/**
		 * variables to store the different normalized tokens
		 */
		List<Token> tokensOriginNodeNormalized = null;
		List<Token> tokensNewNodeNormalized = null;
		List<Token> tokensOriginNodeNormalizedCompundedUnstemmed = null;
		List<Token> tokensNewNodeNormalizedCompundedUnstemmed = null;

		/**
		 * creating objects for the saving of the matching results here
		 */
		comparedLabelPair = new ComparedLabelPair(labelOriginNodeNormalized,
				labelNewNodeNormalized);
		comparedNormalizedTokensPair = new ComparedNormalizedTokensPair();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		/**
		 * Here the tokenized forms are created if necessary. Thereby it's
		 * important to note that the syntactic metrics require a normalization
		 * without compund splitting and without stemming. Whereas the semantic
		 * metrics require full normalization.
		 * 
		 * Then every active match Method is applied to the two nodes here.
		 */
		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			currentMetricResult = 0;

			/**
			 * Depending on the current match method different token sets of the
			 * labels have to be given to the match method. If the method is
			 * StringEditDistance or Hyphen we don't use token normalization
			 * with compoundsplitting or stemming. Thatfore we have to separate
			 * these two cases.
			 */
			if (currentMethod.isActive()) {
				if ((currentMethod.getMatchMethod() == MatchingMethods.STRING_EDIT_DISTANCE_MATCHING)
						|| (currentMethod.getMatchMethod() == MatchingMethods.HYPHEN_MATCHING)) {
					if ((tokensOriginNodeNormalizedCompundedUnstemmed == null)
							|| (tokensNewNodeNormalizedCompundedUnstemmed == null)) {
						tokensOriginNodeNormalizedCompundedUnstemmed = normalizer
								.createNormalizedTokensFromLabel(
										labelOriginNode, true, true, false,
										false);
						tokensNewNodeNormalizedCompundedUnstemmed = normalizer
								.createNormalizedTokensFromLabel(labelNewNode,
										true, true, false, false);

						comparedNormalizedTokensPair
								.setTokensOriginNodeNormalizedCompundedUnstemmed(tokensOriginNodeNormalizedCompundedUnstemmed);
						comparedNormalizedTokensPair
								.setTokensNewNodeNormalizedCompundedUnstemmed(tokensNewNodeNormalizedCompundedUnstemmed);
					}
					currentMetricResult = currentMethod.executeMatching(
							tempNodeOrigin, tempNodeNew, originalModel,
							newModel, metaModel, actTypeOriginModel,
							actTypeNewModel, labelOriginNodeNormalized,
							labelNewNodeNormalized,
							tokensOriginNodeNormalizedCompundedUnstemmed,
							tokensNewNodeNormalizedCompundedUnstemmed);
				} else {
					if ((tokensOriginNodeNormalized == null)
							|| (tokensNewNodeNormalized == null)) {
						tokensOriginNodeNormalized = normalizer
								.createNormalizedTokensFromLabel(
										labelOriginNode, true, true, true, true);
						tokensNewNodeNormalized = normalizer
								.createNormalizedTokensFromLabel(labelNewNode,
										true, true, true, true);

						comparedNormalizedTokensPair
								.setTokensOriginNodeNormalized(tokensOriginNodeNormalized);
						comparedNormalizedTokensPair
								.setTokensNewNodeNormalized(tokensNewNodeNormalized);
					}
					currentMetricResult = currentMethod.executeMatching(
							tempNodeOrigin, tempNodeNew, originalModel,
							newModel, metaModel, actTypeOriginModel,
							actTypeNewModel, labelOriginNodeNormalized,
							labelNewNodeNormalized, tokensOriginNodeNormalized,
							tokensNewNodeNormalized);
				}
			}

			/**
			 * save the result of the recent matchMethod properly
			 */
			saveMatchMethodResult(currentMethod, currentMetricResult);
		}

		/**
		 * save the result of the recent node compairson properly
		 */
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

		printResults(labelOriginNodeNormalized, labelNewNodeNormalized);
	}

	/**
	 * This methods prints the result of the matching of two nodes to the
	 * console
	 * 
	 * @param labelOrigin
	 *            The label from the node from original model
	 * @param labelNew
	 *            The label from the node from new model
	 * 
	 */
	private void printResults(String labelOrigin, String labelNew) {
		if (getWeightedSyntacticSimilarity() > 0
				|| getWeightedSemanticSimilarity() > 0
				|| getWeightedContextSimilarity() > 0) {
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
	}

	/**
	 * This method saves the result from the recent match into the
	 * comparedNodePair object
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeOriginModel
	 *            the type of the orign node
	 * @param actTypeNewModel
	 *            the type of the new node
	 * 
	 */
	private void saveComparedNodePaier(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {
		comparedNodePair.setLabelComparison(comparedLabelPair);
		comparedNodePair.setTokensComparison(comparedNormalizedTokensPair);
		comparedNodePair.setNodeTypes(actTypeOriginModel, actTypeNewModel);
		comparedNodePair.setNodeOriginalModel(tempNodeOrigin);
		comparedNodePair.setNodeNewModel(tempNodeNew);
	}

	/**
	 * @param currentMethod
	 *            the matchMethod which was currently applied
	 * @param currentMetricResult
	 *            the result of the currently applied matchMethod
	 */
	public void saveMatchMethodResult(MatchMethod currentMethod,
			double currentMetricResult) {

		switch (currentMethod.getMatchMethod()) {
		case EXACT_STRING_MATCHING:
			comparedLabelPair.setExactMatchResult(currentMetricResult);
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
		case ATTRIBUTE_MATCHING:
			comparedNodePair.setAttributeMatchResult(currentMetricResult);
			break;
		case CONTEXT_MATCHING:
			comparedNodePair.setContextMatchResult(currentMetricResult);
			break;
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
		double attributeMatch = comparedNodePair.getAttributeMatchResult();

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
			case ATTRIBUTE_MATCHING:
				result += currentMethod.getWeigth() * attributeMatch;
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