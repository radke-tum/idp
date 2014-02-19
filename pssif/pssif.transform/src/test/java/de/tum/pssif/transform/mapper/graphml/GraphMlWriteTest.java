package de.tum.pssif.transform.mapper.graphml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.mapper.Mapper;
import de.tum.pssif.transform.mapper.MapperFactory;


public class GraphMlWriteTest {

  @Test
  public void testWrite() {

    InputStream in = getClass().getResourceAsStream("/flow.graphml");
    GraphMLMapper importer = new GraphMLMapper();

    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Metamodel view = GraphMlViewCreator.createGraphMlView(metamodel);

    Model model = importer.read(view, in);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Mapper mapper = MapperFactory.getMapper(MapperFactory.GRAPHML);
    mapper.write(view, model, out);

    byte[] result = out.toByteArray();
    String str = new String(result);
    System.out.println(str);
    //TODO
  }
}
