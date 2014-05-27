package org.pssif.textNormalization;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;
import org.pssif.matchingLogic.MatchMethod;

//TODO only do normalzation once for each node
// at the moment it's done for every nodepair again

/**
 * @author Andreas TODO
 */
public class Normalizer {

	private Tokenizer tokenizer;
	private CaseNormalizer caseNormalizer;
	private StopwordsFilter stopwordsFilter;
	private WordSplitter wordSplitter;
	private Stemmer stemmer;

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

	public Normalizer() {
		this.tokenizer = new Tokenizer();
		this.caseNormalizer = new CaseNormalizer();
		this.stopwordsFilter = new StopwordsFilter();
		this.wordSplitter = new WordSplitter();
		this.stemmer = new Stemmer();
	}

	public static Normalizer initialize(List<MatchMethod> matchMethods) {
		Normalizer normalizer = new Normalizer();

		normalizer.checkMatchMethods(matchMethods);

		return normalizer;
	}

	/**
	 * This method checks which matching Methods are active and then saves if we
	 * have to remove the whitespace from the labels and/or if we have to
	 * tokenize the labels
	 */
	private void checkMatchMethods(List<MatchMethod> matchMethods) {
		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		if (matchMethods.isEmpty()) {
			throw new RuntimeException(
					"No match Methods were selected. No Merging possible!");
		}

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			if (currentMethod.isActive()) {

				switch (currentMethod.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
					whiteSpaceRemovalRequired = true;
					break;
				case DEPTH_MATCHING:
					tokenizationRequired = true;
					break;
				case STRING_EDIT_DISTANCE_MATCHING:
					tokenizationRequired = true;
					break;
				case HYPHEN_MATCHING:
					tokenizationRequired = true;
					break;
				case LINGUISTIC_MATCHING:
					tokenizationRequired = true;
					break;
				case VECTOR_SPACE_MODEL_MATCHING:
					tokenizationRequired = true;
					break;
				case LATENT_SEMANTIC_INDEXING_MATCHING:
					tokenizationRequired = true;
					break;
				default:
					;
				}
			}
		}
	}

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

	public List<Token> createNormalizedTokensFromLabel(String label,
			boolean normalizeCases, boolean filterStopwords,
			boolean splitWords, boolean stemWords) {
		List<Token> newSequence = new LinkedList<Token>();

		if (tokenizationRequired) {
			if (!label.isEmpty()) {

				newSequence = tokenizer.findTokens(label);
				printTokens("Tokenizer", newSequence);

				if (normalizeCases) {
					newSequence = caseNormalizer
							.convertTokensToLowerCase(newSequence);
					printTokens("CaseNormalizer", newSequence);

				}
				if (filterStopwords) {
					// TODO ask user which stopword language he wants to filter
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
				throw new RuntimeException(
						"The given label is null. No Tokenization possible!");
			}
		}

		return newSequence;
	}

	public void printTokens(String step, List<Token> tokens) {
		System.out.println("Result from normalization step: " + step);
		for (Token token : tokens) {
			System.out.print(token.getWord() + ", ");
		}
		System.out.println("");
	}

}
