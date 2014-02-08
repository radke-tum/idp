package de.tum.pssif.vsdx;

public interface VsdxPage extends VsdxShapeContainer {

  VsdxShape createNewConnector(String masterName, VsdxShape fromShape, VsdxShape toShape);

}
