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
	private String location = ""; // location of data
	private String ns = ""; // Create Namespace
	private static String modelname = URIs.modelname;
	private static RDFModelImpl rdfModel = null;
	private static Model model = null;

	public DatabaseImpl(String location, String ns) {
		this.ns = ns;
		this.location = location;

		// Create Dataset
		// TDB is a serious triple store suitable for enterprise applications
		// that require scalability and performance
		ds = TDBFactory.createDataset(location);

		// create or get existing model
		createModel(modelname);
	}

	// TODO in interface
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

	// // Adds all models in database to list of RDFModelImpl
	// private void setModels() {
	// // get names of all stored models
	// for (Iterator<String> is = ds.listNames(); is.hasNext();) {
	// String modelName = is.next();
	// createModel(modelName);
	// }
	// }

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

	// @Override
	// public RDFModelImpl getModel(String name) {
	// Iterator<RDFModelImpl> iter = models.iterator();
	// while (iter.hasNext()) {
	// RDFModelImpl mod = iter.next();
	// if (mod.getName() == name)
	// return mod;
	// }
	// return null;
	// }

	// TODO override
	public void commit() {
		ds.commit();
	}

	// TODO override
	public void begin(ReadWrite rw) {
		ds.begin(rw);
	}

	// TODO override
	public void end() {
		ds.end();
	}

	// TODO override
	public void close() {
		ds.close();
	}

	// TODO override
	public void removeNamedModel(String uri) {
		ds.removeNamedModel(uri);
	}
}
