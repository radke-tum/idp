package de.tum.pssif.vsdx.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxDocumentLoader;
import de.tum.pssif.vsdx.exception.VsdxIOException;


public class DefaultVsdxDocumentLoader implements VsdxDocumentLoader {

  public static VsdxDocumentLoader INSTANCE            = new DefaultVsdxDocumentLoader();

  private Set<ZipEntryWithData>    transferOnlyEntries = Sets.newHashSet();

  private ZipEntryWithData         mastersXml          = null;
  private Set<ZipEntryWithData>    masters             = Sets.newHashSet();

  //we only read a page1 for now
  private ZipEntryWithData         page1Xml            = null;

  private DefaultVsdxDocumentLoader() {
    //Nothing
  }

  @Override
  public VsdxDocument loadDocument(InputStream inputStream) {
    ZipInputStream zis = new ZipInputStream(inputStream);

    ZipEntry entry = null;
    try {
      while ((entry = zis.getNextEntry()) != null) {
        assignEntryAndRead(entry, zis);
      }
    } catch (IOException e) {
      throw new VsdxIOException(e);
    }

    return new VsdxDocumentImpl();
  }

  private void assignEntryAndRead(ZipEntry entry, ZipInputStream zis) {
    if ("visio/pages/page1.xml".equalsIgnoreCase(entry.getName())) {
      this.page1Xml = new ZipEntryWithData((ZipEntry) entry.clone(), readData(entry, zis));
    }
    else if ("visio/masters/masters.xml".equalsIgnoreCase(entry.getName())) {
      this.mastersXml = new ZipEntryWithData((ZipEntry) entry.clone(), readData(entry, zis));
    }
    else if (entry.getName().startsWith("visio/masters/master")) {
      this.masters.add(new ZipEntryWithData((ZipEntry) entry.clone(), readData(entry, zis)));
    }
    else {
      this.transferOnlyEntries.add(new ZipEntryWithData((ZipEntry) entry.clone(), readData(entry, zis)));
    }
  }

  private byte[] readData(ZipEntry entry, ZipInputStream zis) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    //    int nextByte;
    //    try {
    //      //TODO wtf
    //      while ((nextByte = in.read()) != -1) {
    //        out.write(nextByte);
    //      }
    //      out.flush();
    //    } catch (IOException e) {
    //      throw new VsdxIOException("Failed to read zip file.", e);
    //    }
    return out.toByteArray();
  }
}
