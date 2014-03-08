package de.tum.pssif.transform.mapper.graphml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;


public class GraphMlWriteTest {
  private final Metamodel     metamodel     = PSSIFCanonicMetamodelCreator.create();
  private final GraphMLMapper canonicMapper = new PssifMapper();

  @Test
  public void testWrite() {

    InputStream in = getClass().getResourceAsStream("/flow.graphml");
    GraphMLMapper importer = new UfpMapper();

    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Metamodel view = GraphMLMapper.createGraphMlView(metamodel);

    Model model = importer.read(view, in);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Mapper mapper = MapperFactory.getMapper(MapperFactory.UOFP);
    mapper.write(view, model, out);

    byte[] result = out.toByteArray();
    String str = new String(result);
    System.out.println(str);
    //TODO
  }

  @Test
  public void testWriteEpk() {
    InputStream in = getClass().getResourceAsStream("/visio/epk-data.vsdx");
    Mapper mapper = MapperFactory.getMapper(MapperFactory.EPK);
    Model model = mapper.read(metamodel, in);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    canonicMapper.write(metamodel, model, os);
    System.out.println(os.toString());

    Model readModel = canonicMapper.read(metamodel, new ByteArrayInputStream(os.toByteArray()));
  }
}
