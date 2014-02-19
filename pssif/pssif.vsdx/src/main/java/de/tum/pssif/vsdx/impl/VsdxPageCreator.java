package de.tum.pssif.vsdx.impl;

import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


class VsdxPageCreator {

  static VsdxPageCreator INSTANCE = new VsdxPageCreator();

  private VsdxPageCreator() {
    //Nothing
  }

  public VsdxPageImpl create(ZipArchiveEntryWithData pageEntry) {
    //TODO do xml stuff with the bytes of the entry
    return new VsdxPageImpl(pageEntry.getZipEntry());
  }

}
