package org.pssif.matchingLogic;

/**
 * This enum represents all currently available match methods between nodes in
 * the PSSIF-FW.
 * 
 * If one match method shall be added. The methods
 * checkMatchMethods(),saveMatchMethodResult
 * (),getWeightedSyntacticSimilarity(),getWeightedSemanticSimilarity() and
 * getWeightedContextSimilarity in the class "MatchingProcess" have to be
 * adapted properly. Furthermore the method createMatchMethodObject() in the
 * class MatchMethod has to be adapted.
 * 
 * Additionaly the new matching method has to be implemented through extending
 * the abstract Class "MatchMethod".
 * 
 * @author Andreas
 * 
 */
public enum MatchingMethods {

	EXACT_STRING_MATCHING(0, "Exact String Compairson"), DEPTH_MATCHING(1,
			"Depth Compairson (no Impl)"), STRING_EDIT_DISTANCE_MATCHING(2,
			"Levenshtein Distance"), HYPHEN_MATCHING(3,
			"Hyphen Compairson (no Impl)"), LINGUISTIC_MATCHING(4,
			"Linguistic Compairson"), VECTOR_SPACE_MODEL_MATCHING(5,
			"VSM Compairson"), LATENT_SEMANTIC_INDEXING_MATCHING(6,
			"LSI Compairson (no Impl)"), CONTEXT_MATCHING(7,
			"Contextual Compairson (no Impl)");

	private final int value;
	private final String description;

	private MatchingMethods(final int value, final String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return all names of each enum value as a String array
	 */
	public static String[] methods() {
		MatchingMethods[] methods = values();
		String[] names = new String[methods.length];

		for (int i = 0; i < methods.length; i++) {
			names[i] = methods[i].name();
		}

		return names;
	}

}
