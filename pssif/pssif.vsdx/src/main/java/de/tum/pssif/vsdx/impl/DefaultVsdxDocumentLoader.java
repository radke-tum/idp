package de.tum.pssif.vsdx.impl;

import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxDocumentLoader;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


public class DefaultVsdxDocumentLoader implements VsdxDocumentLoader {

  public static VsdxDocumentLoader     INSTANCE            = new DefaultVsdxDocumentLoader();

  private Set<ZipArchiveEntryWithData> transferOnlyEntries = Sets.newHashSet();

  private ZipArchiveEntryWithData      mastersXml          = null;
  private Set<ZipArchiveEntryWithData> masters             = Sets.newHashSet();

  //we only read a page1 for now
  private ZipArchiveEntryWithData      page1Xml            = null;

  private DefaultVsdxDocumentLoader() {
    //Nothing
  }

  @Override
  public VsdxDocument loadDocument(InputStream inputStream) {
    //TODO entries are read in the zip package. here they should be assigned,
    //parsed to xml etc.
    return new VsdxDocumentImpl();
  }
}
