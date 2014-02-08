package de.tum.pssif.vsdx.impl;

import java.util.Set;

import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


class VsdxMasterRepositoryCreator {

  static final VsdxMasterRepositoryCreator INSTANCE = new VsdxMasterRepositoryCreator();

  private VsdxMasterRepositoryCreator() {
    //Nothing
  }

  public VsdxMasterRepository create(ZipArchiveEntryWithData masters, Set<ZipArchiveEntryWithData> definitions) {
    //TODO
    return new VsdxMasterRepository();
  }

}
