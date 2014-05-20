package comparedDataStructures;

public class ComparedLabelPair extends Compared {

	/*
	 * stores a pair of Labels to be able to lookup if two labels have been
	 * already matched once
	 */
	private String LabelOriginModel, LabelNewModel;
	
	/*
	 * the result of a direct string/token comparison
	 */
	private double exactMatchResult;

}
