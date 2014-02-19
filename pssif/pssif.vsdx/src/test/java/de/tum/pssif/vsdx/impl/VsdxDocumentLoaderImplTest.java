package de.tum.pssif.vsdx.impl;

import org.junit.Test;


public class VsdxDocumentLoaderImplTest {

  @Test
  public void testReadDocument() {
    VsdxDocumentLoaderImpl.INSTANCE.loadDocument(getClass().getResourceAsStream("/epk-template.vsdx"));
  }

}
