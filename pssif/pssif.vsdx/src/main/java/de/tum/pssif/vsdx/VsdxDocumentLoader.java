package de.tum.pssif.vsdx;

import java.io.InputStream;


public interface VsdxDocumentLoader {

  VsdxDocument loadDocument(InputStream inputStream);

}
