package org.pssif.matchingLogic;

/**
 * This enum represents all currently available match methods between nodes in
 * the PSSIF-FW.
 * 
 * If one match method shall be added. The methods
 * checkMatchMethods(),saveMatchMethodResult
 * (),getWeightedSyntacticSimilarity(),getWeightedSemanticSimilarity() and
 * getWeightedContextSimilarity in the class "MatchingProcess" have to be
 * adapted properly.
 * 
 * Additionaly the new matching method has to be implemented through extending
 * the abstract Class "MatchMethod".
 * 
 * @author Andreas
 * 
 */
public enum MatchingMethods {

	EXACT_STRING_MATCHING, DEPTH_MATCHING, STRING_EDIT_DISTANCE_MATCHING, HYPHEN_MATCHING, LINGUISTIC_MATCHING, VECTOR_SPACE_MODEL_MATCHING, LATENT_SEMANTIC_INDEXING_MATCHING, CONTEXT_MATCHING;

}
