package de.tum.pssif.vsdx.impl;

import java.util.zip.ZipEntry;


public class ZipEntryWithData {

  private final ZipEntry zipEntry;
  private final byte[]   data;

  ZipEntryWithData(ZipEntry zipEntry, byte[] data) {
    this.zipEntry = zipEntry;
    this.data = data;
  }

  ZipEntry getZipEntry() {
    return zipEntry;
  }

  byte[] getData() {
    return data;
  }

}
