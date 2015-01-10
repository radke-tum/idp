package jena.database.impl;

import java.util.Iterator;

import javax.swing.JOptionPane;

import jena.database.Database;
import jena.database.URIs;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class DatabaseImpl implements Database {
	public static Dataset ds = null;
	public static DatasetAccessor accessor;
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

	public DatabaseImpl() {
		// Load Database from Fuseki Server
		String serviceURI = URIs.uri.concat("/data");
		try {
			accessor = DatasetAccessorFactory.createHTTP(serviceURI);
			// Get Default Model from Server
			model = accessor.getModel();
			// Create RDFModel
			rdfModel = new RDFModelImpl(URIs.modelname, model);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problems with connecting to Server!\n", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Getter
	public RDFModelImpl getRdfModel() {
		return rdfModel;
	}

	@Override
	public void printModelNames() {
		if (ds != null) {
			ds.begin(ReadWrite.READ);
			// get names of all stored models
			for (Iterator<String> is = ds.listNames(); is.hasNext();) {
				String modelName = is.next();
				System.out.println(modelName);
			}
			ds.end();
		}
	}

	@Override
	public RDFModelImpl createModel(String name) {
		if (ds != null) {
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
		return null;
	}

	@Override
	public void saveModel(Model model) {
		// TODO Testing
		rdfModel.writeModelToFile("TestPSSIF", "C:\\Users\\Andrea\\Desktop\\");
		if (accessor != null)
			accessor.putModel(model);
		else if (ds != null)
			model.commit();
	}

	@Override
	public void commit() {
		if (ds != null) {
			ds.commit();
		}
	}

	@Override
	public void begin(ReadWrite rw) {
		if (ds != null) {
			ds.begin(rw);
		}
	}

	@Override
	public void end() {
		if (ds != null) {
			ds.end();
		}
	}

	@Override
	public void close() {
		if (ds != null) {
			ds.close();
		}
	}

	@Override
	public void removeNamedModel(String uri) {
		if (ds != null)
			ds.removeNamedModel(uri);
		else if (accessor != null)
			accessor.deleteDefault();
	}
}
