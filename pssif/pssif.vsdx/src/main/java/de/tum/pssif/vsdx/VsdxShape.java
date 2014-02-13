package de.tum.pssif.vsdx;

public interface VsdxShape extends VsdxShapeContainer {

  int getId();

  void setText(String text);

  String getText();

  VsdxMaster getMaster();

  //TODO more stuff here
  //espeecially: setAttribute/getattribute

}
