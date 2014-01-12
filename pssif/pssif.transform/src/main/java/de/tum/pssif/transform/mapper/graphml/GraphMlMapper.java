package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.mapper.Mapper;


public class GraphMlMapper implements Mapper {

  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    //TODO provide metamodel as parameter to the importer!
    GraphMLImporter importer = new GraphMLImporter();
    return importer.read(inputStream);
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    // TODO implement
    throw new UnsupportedOperationException("Not supperted yet.");
  }

}
