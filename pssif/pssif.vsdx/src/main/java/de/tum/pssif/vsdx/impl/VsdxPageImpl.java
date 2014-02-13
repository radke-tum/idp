package de.tum.pssif.vsdx.impl;

import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxPage;
import de.tum.pssif.vsdx.VsdxShape;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


public class VsdxPageImpl implements VsdxPage {

  private final ZipArchiveEntry    zipArchiveEntry;
  private final Set<VsdxShapeImpl> shapes;

  private VsdxDocumentImpl         document;

  VsdxPageImpl(ZipArchiveEntry zipArchiveEntry, Set<VsdxShapeImpl> shapes) {
    this.zipArchiveEntry = zipArchiveEntry;
    this.shapes = shapes;
  }

  int setDocument(VsdxDocumentImpl document) {
    this.document = document;
    int maxId = 0;
    for (VsdxShapeImpl shape : shapes) {
      maxId = Math.max(maxId, shape.setDocument(document));
    }
    return maxId;
  }

  @Override
  public Set<VsdxShape> getShapes() {
    return Sets.<VsdxShape> newHashSet(shapes);
  }

  @Override
  public VsdxDocument getVsdxDocument() {
    return this.document;
  }

  @Override
  public VsdxShape createNewShape(String masterName) {
    return new VsdxShapeImpl(document.getNewShapeId(), document.getMaster(masterName).getId());
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
