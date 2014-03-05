package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;


public class UfpMapper extends GraphMLMapper {
  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    return readInternal(createGraphMlView(metamodel), inputStream);
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    writeInternal(createGraphMlView(metamodel), model, outputStream);
  }
}
