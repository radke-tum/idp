package de.tum.pssif.vsdx.impl;

import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxConnector;
import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxMaster;
import de.tum.pssif.vsdx.VsdxPage;
import de.tum.pssif.vsdx.VsdxShape;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


public class VsdxPageImpl implements VsdxPage {

  private final ZipArchiveEntry        zipArchiveEntry;
  private final Set<VsdxShapeImpl>     shapes;
  private final Set<VsdxConnectorImpl> connects;

  private VsdxDocumentImpl             document;

  VsdxPageImpl(ZipArchiveEntry zipArchiveEntry, Set<VsdxShapeImpl> shapes, Set<VsdxConnectorImpl> connects) {
    this.zipArchiveEntry = zipArchiveEntry;
    this.shapes = shapes;
    this.connects = connects;
  }

  int setDocument(VsdxDocumentImpl document) {
    this.document = document;
    int maxId = 0;
    for (VsdxShapeImpl shape : shapes) {
      maxId = Math.max(maxId, shape.setDocument(document));
    }
    for (VsdxConnectorImpl connect : connects) {
      maxId = Math.max(maxId, connect.setDocument(document));
    }
    return maxId;
  }

  @Override
  public Set<VsdxShape> getShapes() {
    return Sets.<VsdxShape> newHashSet(shapes);
  }

  @Override
  public Set<VsdxConnector> getConnectors() {
    return Sets.<VsdxConnector> newHashSet(connects);
  }

  @Override
  public VsdxDocument getVsdxDocument() {
    return this.document;
  }

  @Override
  public VsdxShape createNewShape(VsdxMaster master) {
    VsdxShapeImpl shape = new VsdxShapeImpl(document.getNewShapeId(), master.getId());
    shapes.add(shape);
    return shape;
  }

  @Override
  public VsdxShape createNewConnector(VsdxMaster master, VsdxShape fromShape, VsdxShape toShape) {
    VsdxConnectorImpl connect = new VsdxConnectorImpl(document.getNewShapeId(), master.getId());
    connect.setSource((VsdxShapeImpl) fromShape);
    connect.setTarget((VsdxShapeImpl) toShape);
    connects.add(connect);
    return connect;
  }

  ZipArchiveEntryWithData asZipArchiveEntryWithData() {
    //TODO this is a tricky one: serialize contents to byte array, then create new entry
    //with zipArchiveentry clone of the current one, and the serialized data.
    return null;
  }

}
