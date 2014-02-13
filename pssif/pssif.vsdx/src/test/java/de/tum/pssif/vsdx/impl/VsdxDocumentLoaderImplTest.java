package de.tum.pssif.vsdx.impl;

import org.junit.Test;

import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxShape;


public class VsdxDocumentLoaderImplTest {

  @Test
  public void testReadTemplate() {
    System.out.println("--- Template ---");
    VsdxDocument document = VsdxDocumentLoaderImpl.INSTANCE.loadDocument(getClass().getResourceAsStream("/epk-template.vsdx"));
    System.out.println(document.getMasters());
    System.out.println(document.getPage().getShapes());
  }

  @Test
  public void testReadData() {
    System.out.println("--- Data ---");
    VsdxDocument document = VsdxDocumentLoaderImpl.INSTANCE.loadDocument(getClass().getResourceAsStream("/epk-data.vsdx"));
    System.out.println(document.getMasters());
    for (VsdxShape shape : document.getPage().getShapes()) {
      System.out.println(shape);
    }
  }

}
