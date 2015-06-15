package jena.mapper;

import model.MyModelContainer;

public interface DBMapper {

	/**
	 * Saves the existing Model from MyModelContainer to the Database
	 * 
	 * @param model
	 *            MyModelContainer from which the model should be saved
	 * @param modelname
	 *            Name of the Model in the Database
	 */
	void modelToDB(MyModelContainer model, String modelname);

	/**
	 * Saves changes of the Model to the Database
	 * 
	 * @param modelname
	 *            Name of the Model in the Database
	 */
	void saveToDB(String modelname);

	/**
	 * Removes the whole Model from the Database
	 */
	void removeModel();

}
