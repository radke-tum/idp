package de.tum.pssif.vsdx.impl;

import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxPage;
import de.tum.pssif.vsdx.VsdxShape;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


public class VsdxPageImpl implements VsdxPage {

  private final ZipArchiveEntry zipArchiveEntry;

  private VsdxDocumentImpl      document;

  VsdxPageImpl(ZipArchiveEntry zipArchiveEntry) {
    this.zipArchiveEntry = zipArchiveEntry;
  }

  void setDocument(VsdxDocumentImpl document) {
    this.document = document;
  }

  @Override
  public Set<VsdxShape> getShapes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VsdxDocument getVsdxDocument() {
    return this.document;
  }

  @Override
  public VsdxShape createNewShape(String masterName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public VsdxShape createNewConnector(String masterName, VsdxShape fromShape, VsdxShape toShape) {
    // TODO Auto-generated method stub
    return null;
  }

  ZipArchiveEntryWithData asZipArchiveEntryWithData() {
    //TODO this is a tricky one: serialize contents to byte array, then create new entry
    //with zipArchiveentry clone of the current one, and the serialized data.
    return null;
  }

}
