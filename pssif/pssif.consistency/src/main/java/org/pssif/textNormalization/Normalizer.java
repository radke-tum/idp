package org.pssif.textNormalization;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;
import org.pssif.exception.ConsistencyException;
import org.pssif.exception.MatchMethodException;
import org.pssif.exception.NormalizationException;
import org.pssif.mainProcesses.MatchingProcess;
import org.pssif.matchingLogic.ContextMatcher;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.VsmMatcher;

//TODO only do normalzation once for each node
// at the moment it's done for every nodepair again

/**
 * This class is responsible for looking which normalization steps are necessary
 * based on the active matching Methods. Then the according objects for the
 * normalization steps are triggered.
 * 
 * @author Andreas
 * 
 */
public class Normalizer {

	private Tokenizer tokenizer;
	private CaseNormalizer caseNormalizer;
	private StopwordsFilter stopwordsFilter;
	private WordSplitter wordSplitter;
	private Stemmer stemmer;

	private MatchingProcess matchingProcess;

	/**
	 * if true the results of each token- and normalization step is printed to
	 * the console
	 */
	private static final boolean debugMode = false;

	/**
	 * this bool says whether we need to remove the whitespace from the labels
	 * of the nodes for exact matching
	 */
	private boolean whiteSpaceRemovalRequired;

	/**
	 * this bool says whether we have to tokenize and normalize the labels from
	 * the nodes for some metrics
	 */
	private boolean tokenizationRequired;

	/**
	 * this bool says whether we have to tokenize and normalize (without
	 * stemming) the labels from the nodes for some metrics
	 */
	private boolean tokenizationWithoutStemmingRequired;

	/**
	 * these two boolean variables are set so the context metric is able to look
	 * up if at least one syntactic metric active and/or if at least one
	 * semantic metric is active.
	 */
	/**
	 * this variable says whether at least one syntactic matching metric is
	 * active
	 */
	private boolean syntacticMetricActive = false;

	/**
	 * this variable says whether at least one semantic matching metric is
	 * active
	 */
	private boolean semanticMetricActive = false;

	/**
	 * @return the syntacticMetricActive
	 */
	public boolean isSyntacticMetricActive() {
		return syntacticMetricActive;
	}

	/**
	 * @return the semanticMetricActive
	 */
	public boolean isSemanticMetricActive() {
		return semanticMetricActive;
	}

	public Normalizer() {
		this.tokenizer = new Tokenizer();
		this.caseNormalizer = new CaseNormalizer();
		this.stopwordsFilter = new StopwordsFilter();
		try {
			this.wordSplitter = new WordSplitter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stemmer = new Stemmer();
	}

	/**
	 * This constructor is used in the consistency checker and handles the match
	 * methods chosen by the user.
	 * 
	 * @param matchMethods
	 *            the list of the matchMethods given by the user
	 * @return an object of this class with the information which normalization
	 *         steps shall be conducted
	 */
	public static Normalizer initialize(List<MatchMethod> matchMethods,
			MatchingProcess matchingProcess) {
		Normalizer normalizer = new Normalizer();
		normalizer.matchingProcess = matchingProcess;

		normalizer.checkMatchMethods(matchMethods);

		return normalizer;
	}

	/**
	 * This constructor is used in the model merger and handles the match
	 * methods which are used in the model merger.
	 * 
	 * @param matchMethods
	 *            the list of
	 * @return an object of this class with the information which normalization
	 *         steps shall be conducted
	 */
	public static Normalizer initialize(List<MatchMethod> matchMethods) {
		Normalizer normalizer = new Normalizer();

		if (normalizer == null) {
			throw new ConsistencyException(
					"Couldn't initialize the Normalizer. Maybe something was changed in one of the text normalization classes.",
					new NullPointerException());
		} else {
			normalizer.checkMatchMethods(matchMethods);

			return normalizer;
		}
	}

	/**
	 * This method checks which matching Methods are active and then saves if we
	 * have to remove the whitespace from the labels and/or if we have to
	 * tokenize the labels
	 * 
	 * Important: if the VSM metric was chosen we have to build the document
	 * corpus before being able to apply this metric for every node pair
	 */
	private void checkMatchMethods(List<MatchMethod> matchMethods) {
		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		if (matchMethods.isEmpty()) {
			throw new MatchMethodException(
					"No match Methods were selected. No Matching/Merging possible!");
		}

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			if (currentMethod.isActive()) {

				switch (currentMethod.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
					whiteSpaceRemovalRequired = true;
					syntacticMetricActive = true;
					break;
				case DEPTH_MATCHING:
					tokenizationRequired = true;
					syntacticMetricActive = true;
					break;
				case STRING_EDIT_DISTANCE_MATCHING:
					tokenizationWithoutStemmingRequired = true;
					syntacticMetricActive = true;
					break;
				case HYPHEN_MATCHING:
					tokenizationWithoutStemmingRequired = true;
					syntacticMetricActive = true;
					break;
				case LINGUISTIC_MATCHING:
					tokenizationRequired = true;
					semanticMetricActive = true;
					break;
				case VECTOR_SPACE_MODEL_MATCHING:
					tokenizationRequired = true;
					semanticMetricActive = true;
					((VsmMatcher) currentMethod).initializeDocumentCorpus(this,
							matchingProcess);
					((VsmMatcher) currentMethod).computeIDFWeigths();
					break;
				case LATENT_SEMANTIC_INDEXING_MATCHING:
					tokenizationRequired = true;
					semanticMetricActive = true;
					break;
				case ATTRIBUTE_MATCHING:
					semanticMetricActive = true;
					break;
				case CONTEXT_MATCHING:
					/**
					 * only works if other match methods were selected because
					 * they are then applied to the contexts of the nodes
					 */
					((ContextMatcher) currentMethod)
							.setMatchMethods(matchMethods);
					((ContextMatcher) currentMethod)
							.setConsistencyData(matchingProcess
									.getConsistencyData());
					((ContextMatcher) currentMethod).setNormalizer(this);
					break;
				default:
					;
				}
			}
		}
	}

	/**
	 * This method takes a label, removes all spaces and converts all letters to
	 * lower cases
	 * 
	 * @param label
	 *            the label to be normalized
	 * @return the given label in its normalized form
	 * 
	 */
	public String normalizeLabel(String label) {
		String tmp = label;

		if (whiteSpaceRemovalRequired) {
			if (!label.isEmpty()) {
				if (whiteSpaceRemovalRequired) {
					tmp = tmp.replaceAll("\\s+", "").toLowerCase();
				}
			} else {
				throw new RuntimeException(
						"The given label is null. No Normalization possible!");
			}
		}
		return tmp;
	}

	/**
	 * This method takes a label and applies several normalization techniques to
	 * it based on the active matching methods.
	 * 
	 * @param label
	 *            the label to be tokenized & normalized
	 * @param normalizeCases
	 * @param filterStopwords
	 * @param splitWords
	 * @param stemWords
	 * @return the given label in its tokenized & normalized form
	 * 
	 */
	public List<Token> createNormalizedTokensFromLabel(String label,
			boolean normalizeCases, boolean filterStopwords,
			boolean splitWords, boolean stemWords) {
		List<Token> newSequence = new LinkedList<Token>();

		if (tokenizationRequired || tokenizationWithoutStemmingRequired) {
			if (!label.isEmpty()) {

				newSequence = tokenizer.findTokens(label);
				printTokens("Tokenizer", newSequence);

				if (normalizeCases) {
					newSequence = caseNormalizer
							.convertTokensToLowerCase(newSequence);
					 printTokens("CaseNormalizer", newSequence);

				}
				if (filterStopwords) {
					newSequence = stopwordsFilter.filterStopWords(newSequence,
							true, false);
					printTokens("StopWordFilter", newSequence);

				}
				if (splitWords) {
					newSequence = wordSplitter.splitTokens(newSequence);
					printTokens("WordSplitter", newSequence);

				}
				if (stemWords) {
					newSequence = stemmer.stemTokens(newSequence);
					printTokens("Stemming", newSequence);

				}

			} else {
				throw new NormalizationException(
						"The given label is null. No tokenization & normalization possible!", new NullPointerException());
			}
		}

		return newSequence;
	}

	/**
	 * This method prints the result from the last normalization step to the
	 * console.
	 * 
	 * @param step
	 *            the recent normalization step
	 * @param tokens
	 *            the result of the recent normalization step
	 * 
	 */
	public void printTokens(String step, List<Token> tokens) {
		if (debugMode) {
			System.out.println("Result from normalization step: " + step);
			for (Token token : tokens) {
				System.out.print(token.getWord() + ", ");
			}
			System.out.println("");
		}
	}

}
