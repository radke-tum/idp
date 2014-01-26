package de.tum.pssif.transform.mapper.graphml;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.mapper.Mapper;
import de.tum.pssif.transform.mapper.MapperFactory;


public class GraphMlWriteTest {

  @Test
  public void testWrite() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Mapper mapper = MapperFactory.getMapper(MapperFactory.GRAPHML);
    mapper.write(PSSIFCanonicMetamodelCreator.create(), new ModelImpl(), out);
    byte[] result = out.toByteArray();
    String str = new String(result);
    System.out.println(str);
    //TODO
  }

}
