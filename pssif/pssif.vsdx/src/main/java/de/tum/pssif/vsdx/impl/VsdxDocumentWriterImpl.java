package de.tum.pssif.vsdx.impl;

import java.io.OutputStream;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxDocumentWriter;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;
import de.tum.pssif.vsdx.zip.ZipWriter;


public class VsdxDocumentWriterImpl implements VsdxDocumentWriter {

  private final VsdxDocumentImpl document;

  private VsdxDocumentWriterImpl(VsdxDocumentImpl document) {
    this.document = document;
  }

  public static VsdxDocumentWriter create(VsdxDocumentImpl document) {
    return new VsdxDocumentWriterImpl(document);
  }

  @Override
  public void write(OutputStream outputStream) {
    ZipWriter writer = ZipWriter.create(outputStream);
    Set<ZipArchiveEntryWithData> zipEntries = Sets.newHashSet();
    zipEntries.addAll(document.getTransferOnlyEntries());
    zipEntries.add(document.getPage().asZipArchiveEntryWithData());
    writer.write(zipEntries);
  }

}
