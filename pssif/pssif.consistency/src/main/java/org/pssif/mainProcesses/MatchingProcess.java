package org.pssif.mainProcesses;

import java.util.Iterator;
import java.util.List;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.consistencyLogic.MatchMethod;
import org.pssif.textMining.Tokenizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas TODO
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

		checkMatchMethods();
	}

	private Model originalModel, newModel;
	private Metamodel metaModel;

	private ConsistencyData consistencyData;

	private List<MatchMethod> matchMethods;

	/**
	 * this bool says whether we need to remove the whitespace from the labels
	 * of the nodes for exact matching
	 */
	private boolean whiteSpaceRemovalRequired;

	/**
	 * this bool says whether we have to tokenize the labels from the nodes for
	 * some metrics
	 */
	private boolean tokenizationRequired;

	private ComparedLabelPair comparedLabelPair = null;
	private ComparedNormalizedTokensPair comparedNormalizedTokensPair = null;
	private ComparedNodePair comparedNodePair = null;

	private void checkMatchMethods() {
		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			if (currentMethod.isActive()) {

				switch (currentMethod.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
					whiteSpaceRemovalRequired = true;
				case DEPTH_MATCHING:
					tokenizationRequired = true;
				case STRING_EDIT_DISTANCE_MATCHING:
					tokenizationRequired = true;
				case HYPHEN_MATCHING:
					tokenizationRequired = true;
				case LINGUISTIC_MATCHING:
					tokenizationRequired = true;
				case VECTOR_SPACE_MODEL_MATCHING:
					tokenizationRequired = true;
				case LATENT_SEMANTIC_INDEXING_MATCHING:
					tokenizationRequired = true;
				default:
					throw new RuntimeException(
							"The last metric couln't be found.");
				}
			}
		}
	}

	/**
	 * @param tempNodeOrigin
	 * @param tempNodeNew
	 * @param actTypeOriginModel
	 *            TODO
	 * @param actTypeNewModel
	 */
	public void startMatchingProcess(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {

		double currentMetricResult;

		comparedLabelPair = null;
		comparedNormalizedTokensPair = null;
		comparedNodePair = new ComparedNodePair();

		/**
		 * here the strings of the old and the new node are read from the model.
		 */
		String labelOrigin = findName(actTypeOriginModel, tempNodeOrigin);
		String labelNew = findName(actTypeNewModel, tempNodeNew);

		Token[] tokensOrigin = null;
		Token[] tokensNew = null;

		if (tokenizationRequired) {
			tokensOrigin = Tokenizer.tokenize(labelOrigin);
			tokensNew = Tokenizer.tokenize(labelNew);

			createComparedNormalizedTokensPair(tokensOrigin, tokensNew);
		}

		/**
		 * Here the whitespace of the two labels is removed if necessary and
		 * they are converted to lowercase
		 * 
		 */
		if (whiteSpaceRemovalRequired) {
			labelOrigin = labelOrigin.replaceAll("\\s+", "").toLowerCase();
			labelNew = labelNew.replaceAll("\\s+", "").toLowerCase();
		}

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			if (currentMethod.isActive()) {
				// TODO: get unweigthed result here
				currentMetricResult = currentMethod.executeMatching(
						tempNodeOrigin, tempNodeNew, originalModel, newModel,
						metaModel, actTypeOriginModel, actTypeNewModel,
						labelOrigin, labelNew, tokensOrigin, tokensNew);

				saveMatchMethodResult(currentMethod, currentMetricResult,
						labelOrigin, labelNew);
			}
		}

		comparedNodePair.setLabelComparison(comparedLabelPair);
		comparedNodePair.setTokensComparison(comparedNormalizedTokensPair);

		if (!(consistencyData.putComparedEntry(tempNodeOrigin, tempNodeNew,
				comparedNodePair))) {
			throw new RuntimeException(
					"Something went wrong with the saving of the recently matched elements.");
		}

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

	/**
	 * @param currentMethod
	 * @param currentMetricResult
	 *            TODO
	 * @param labelOrigin
	 *            TODO
	 * @param labelNew
	 *            TODO
	 * @throws RuntimeException
	 */
	public void saveMatchMethodResult(MatchMethod currentMethod,
			double currentMetricResult, String labelOrigin, String labelNew)
			throws RuntimeException {

		switch (currentMethod.getMatchMethod()) {
		case EXACT_STRING_MATCHING:
			if (comparedLabelPair == null) {
				comparedLabelPair = new ComparedLabelPair(labelOrigin,
						labelNew, currentMetricResult);
			}
		case DEPTH_MATCHING:
			comparedNodePair.setDepthMatchResult(currentMetricResult);
		case STRING_EDIT_DISTANCE_MATCHING:
			comparedNormalizedTokensPair
					.setStringEditDistanceResult(currentMetricResult);
		case HYPHEN_MATCHING:
			comparedNormalizedTokensPair
					.setHyphenMatchResult(currentMetricResult);
		case LINGUISTIC_MATCHING:
			comparedNormalizedTokensPair
					.setLinguisticMatchResult(currentMetricResult);
		case VECTOR_SPACE_MODEL_MATCHING:
			comparedNormalizedTokensPair.setVsmMatchResult(currentMetricResult);
		case LATENT_SEMANTIC_INDEXING_MATCHING:
			comparedNormalizedTokensPair.setLsiMatchResult(currentMetricResult);
		case CONTEXT_MATCHING:
			comparedNodePair.setContextMatchResult(currentMetricResult);
		default:
			throw new RuntimeException(
					"The last applied metric couln't be found.");
		}
	}

	/**
	 * @param tokensOrigin
	 * @param tokensNew
	 */
	private void createComparedNormalizedTokensPair(Token[] tokensOrigin,
			Token[] tokensNew) {
		if (comparedNormalizedTokensPair == null) {
			comparedNormalizedTokensPair = new ComparedNormalizedTokensPair(
					tokensOrigin, tokensNew);
		}
	}

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
			case DEPTH_MATCHING:
				result += currentMethod.getWeigth() * depthMatch;
			case STRING_EDIT_DISTANCE_MATCHING:
				result += currentMethod.getWeigth() * stringEditDistanceMatch;
			case HYPHEN_MATCHING:
				result += currentMethod.getWeigth() * hyphenMatch;
			default:
				;
			}
		}
		return result;
	}

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
			case VECTOR_SPACE_MODEL_MATCHING:
				result += currentMethod.getWeigth() * vsmMatch;
			case LATENT_SEMANTIC_INDEXING_MATCHING:
				result += currentMethod.getWeigth() * lsiMatch;
			default:
				;
			}
		}

		return result;
	}

	public double getWeightedContextSimilarity() {
		double result = 0;

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case CONTEXT_MATCHING:
				result += currentMethod.getWeigth()
						* comparedNodePair.getContextMatchResult();
			default:
				;
			}
		}

		return result;
	}

	/**
	 * Get the name from the Node object
	 * 
	 * @return the actual name or "Name not available" if the name was not
	 *         defined
	 * @author Luc
	 */
	private String findName(NodeType actType, Node actNode) {
		String name = "Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp = actType
				.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		if (tmp.isOne()) {
			Attribute nodeName = tmp.getOne();

			if (nodeName.get(actNode) != null) {
				PSSIFValue value = null;
				if (nodeName.get(actNode).isOne()) {
					value = nodeName.get(actNode).getOne();
					name = value.asString();
				}
				if (nodeName.get(actNode).isNone()) {
					name = "Name not available";
				}
			} else
				name = "Name not available";
		}

		return name;
	}
}