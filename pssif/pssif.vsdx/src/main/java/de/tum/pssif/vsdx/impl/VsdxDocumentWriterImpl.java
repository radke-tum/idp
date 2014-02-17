package de.tum.pssif.vsdx.impl;

import java.io.OutputStream;
import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxDocumentWriter;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;
import de.tum.pssif.vsdx.zip.ZipWriter;


public class VsdxDocumentWriterImpl implements VsdxDocumentWriter {

  private final VsdxDocumentImpl document;

  VsdxDocumentWriterImpl(VsdxDocumentImpl document) {
    this.document = document;
  }

  @Override
  public void write(OutputStream outputStream) {
    ZipWriter writer = ZipWriter.create(outputStream);
    Set<ZipArchiveEntryWithData> zipEntries = Sets.newHashSet();
    zipEntries.addAll(document.getTransferOnlyEntries());

    ZipArchiveEntry pageEntry = document.getPage().getZipArchiveEntry();
    byte[] pageContents = serializePage(document.getPage());
    ZipArchiveEntryWithData pageEntryWithData = new ZipArchiveEntryWithData(pageEntry, pageContents);
    zipEntries.add(pageEntryWithData);

    writer.write(zipEntries);
  }

  private byte[] serializePage(VsdxPageImpl page) {
    //TODO serialize page here!
    //    zipEntries.add(document.getPage().asZipArchiveEntryWithData());
    //TODO this is a tricky one: serialize contents to byte array, then create new entry
    //with zipArchiveentry clone of the current one, and the serialized data.
    //TODO
    return new byte[0];
  }
}
