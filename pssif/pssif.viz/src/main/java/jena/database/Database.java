package jena.database;

public interface Database {
	/**
	 * Creates a new RDF model if not already existing else return existing RDF
	 * model
	 * 
	 * @param name
	 *            Name of the dataset model
	 * @return New/Existing RDFModel
	 */
	public RDFModel createModel(String name);

	/**
	 * Prints out names of all the models in a dataset Just for testing purpose
	 */
	public void printModelNames();

	// /**
	// * Searches for a certain RDFModel in the list of RDFModels
	// *
	// * @param Name
	// * of the model
	// * @return If found the RDFModel else null
	// */
	// public RDFModelImpl getModel(String name);
}
