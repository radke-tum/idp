package de.tum.pssif.vsdx.impl;

import org.junit.Test;


public class DefaultVsdxDocumentLoaderTest {

  @Test
  public void testReadDocument() {
    DefaultVsdxDocumentLoader.INSTANCE.loadDocument(getClass().getResourceAsStream("/epk-template.vsdx"));
  }

}
