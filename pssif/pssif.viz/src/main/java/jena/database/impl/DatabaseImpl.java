package jena.database.impl;

import java.util.Iterator;

import jena.database.Database;
import jena.database.URIs;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class DatabaseImpl implements Database {
	public static Dataset ds = null;
	private static String modelname = URIs.modelname;
	private static RDFModelImpl rdfModel = null;
	private static Model model = null;

	public DatabaseImpl(String location, String ns) {

		// Create Dataset
		// TDB is a serious triple store suitable for enterprise applications
		// that require scalability and performance
		ds = TDBFactory.createDataset(location);

		// create or get existing model
		createModel(modelname);
	}

	// Getter
	public RDFModelImpl getRdfModel() {
		return rdfModel;
	}

	@Override
	public void printModelNames() {
		ds.begin(ReadWrite.READ);
		// get names of all stored models
		for (Iterator<String> is = ds.listNames(); is.hasNext();) {
			String modelName = is.next();
			System.out.println(modelName);
		}
		ds.end();
	}

	@Override
	public RDFModelImpl createModel(String name) {

		// Create Model
		// Alternative default model: model = ds.getDefaultModel() -> But
		// it's good practice to use a named model. The default model always
		// exist in the Dataset. It will exist even if it is not used.
		if (model == null)
			model = ds.getNamedModel(name);

		// If model doesn't exist already
		if (rdfModel == null) {
			// create new RDFModel
			rdfModel = new RDFModelImpl(name, model);
		}

		return rdfModel;
	}

	@Override
	public void commit() {
		ds.commit();
	}

	@Override
	public void begin(ReadWrite rw) {
		ds.begin(rw);
	}

	@Override
	public void end() {
		ds.end();
	}

	@Override
	public void close() {
		ds.close();
	}

	@Override
	public void removeNamedModel(String uri) {
		ds.removeNamedModel(uri);
	}
}
