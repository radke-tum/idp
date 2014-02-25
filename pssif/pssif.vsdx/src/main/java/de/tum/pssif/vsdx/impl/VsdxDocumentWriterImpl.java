package de.tum.pssif.vsdx.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.VsdxConnector;
import de.tum.pssif.vsdx.VsdxDocumentWriter;
import de.tum.pssif.vsdx.VsdxPage;
import de.tum.pssif.vsdx.VsdxShape;
import de.tum.pssif.vsdx.VsdxShapeContainer;
import de.tum.pssif.vsdx.exception.VsdxIOException;
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
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XMLOutputFactory factory = XMLOutputFactory.newInstance();

    try {
      XMLStreamWriter writer = factory.createXMLStreamWriter(out);
      writeDocumentHeader(page, writer);
      writeShapes(page, writer);
      writeConnectors(page, writer);
      writeDocumentFooter(page, writer);
      writer.flush();
    } catch (XMLStreamException e) {
      throw new VsdxIOException("Failed to generate Visio page XML.", e);
    }

    try {
      out.flush();
      out.close();
    } catch (IOException e) {
      throw new VsdxIOException("Failed to generate Visio page XML.", e);
    }
    return out.toByteArray();
  }

  private void writeDocumentHeader(VsdxPageImpl page, XMLStreamWriter writer) throws XMLStreamException {
    writer.writeStartDocument("UTF-8", "1.0");
    writer.writeStartElement(VsdxTokens.PAGE_CONTENTS);
    writer.writeAttribute(VsdxTokens.XMLNS, VsdxTokens.XSD_VISIO);
    writer.writeAttribute(VsdxTokens.XMLNS_R, VsdxTokens.XSD_OPENXML_RELATIONSHIPS);
    writer.writeAttribute(VsdxTokens.XML_SPACE, VsdxTokens.PRESERVE);
  }

  private void writeShapes(VsdxShapeContainer container, XMLStreamWriter writer) throws XMLStreamException {
    if (container.getShapes().size() > 0) {
      writer.writeStartElement(VsdxTokens.SHAPES);
      for (VsdxShape shape : container.getShapes()) {
        writeShape(shape, writer);
      }
      if (container instanceof VsdxPage) {
        for (VsdxConnector connector : ((VsdxPage) container).getConnectors()) {
          //TODO will this do? connectors seem to have more stuff...
          writeShape(connector, writer);
        }
      }
      writer.writeEndElement();
    }
  }

  private void writeShape(VsdxShape shape, XMLStreamWriter writer) throws XMLStreamException {
    //shape header
    writer.writeStartElement(VsdxTokens.SHAPE);
    writer.writeAttribute(VsdxTokens.ID, String.valueOf(shape.getId()));
    writer.writeAttribute(VsdxTokens.NAMEU, shape.getMaster().getName());
    writer.writeAttribute(VsdxTokens.NAME, shape.getMaster().getName());
    writer.writeAttribute(VsdxTokens.TYPE, VsdxTokens.SHAPE);
    writer.writeAttribute(VsdxTokens.MASTER, String.valueOf(shape.getMaster().getId()));

    //write static elements
    //TODO diversify coordinates. maybe layout shapes on a grid?
    writeCell(writer, VsdxTokens.STATIC_PIN_X, "2");
    writeCell(writer, VsdxTokens.STATIC_PIN_Y, "2");
    if (shape.isConnector()) {
      writeCell(writer, VsdxTokens.STATIC_LAYER_MEMBER, "1");
    }
    else {
      writeCell(writer, VsdxTokens.STATIC_LAYER_MEMBER, "0");
      writeCell(writer, VsdxTokens.STATIC_OBJECT_TYPE, "1");
    }
    //TODO can those be skipped and automatically inherited from the master?
    //    writeCell(writer, VsdxTokens.STATIC_LINE_CAP, "2");
    //    writeCell(writer, VsdxTokens.STATIC_SHADOW_PATTERN, "1");
    //    writeCell(writer, VsdxTokens.STATIC_SHADOW_FOREGROUND_TRANS, "0.8");
    //    writeCell(writer, VsdxTokens.STATIC_SHAPE_SHADOW_TYPE, "1");
    //    writeCell(writer, VsdxTokens.STATIC_SHAPE_SHADOW_OFFSET_X, "0.003", VsdxTokens.UNIT_IN, null);
    //    writeCell(writer, VsdxTokens.STATIC_SHAPE_SHADOW_OFFSET_Y, "-0.03", VsdxTokens.UNIT_IN, null);

    //write custom properties
    if (shape.getCustomPropertyNames().size() > 0) {
      writer.writeStartElement(VsdxTokens.SECTION);
      writer.writeAttribute(VsdxTokens.ATTRIBUTE_NAME, VsdxTokens.PROPERTY);
      for (String pName : shape.getCustomPropertyNames()) {
        if (shape.getCustomPropertyValue(pName) != null && !shape.getCustomPropertyValue(pName).trim().isEmpty()) {
          writeCustomProperty(writer, pName, shape.getCustomPropertyValue(pName));
        }
      }
      writer.writeEndElement();
    }
    if (shape.getText() != null && !shape.getText().trim().isEmpty()) {
      writer.writeStartElement(VsdxTokens.TEXT);
      writer.writeCharacters(shape.getText());
      writer.writeEndElement();
    }
    if (shape.getShapes().size() > 0) {
      writeShapes(shape, writer);
    }
    writer.writeEndElement();
  }

  private void writeCustomProperty(XMLStreamWriter writer, String pName, String pValue) throws XMLStreamException {
    writer.writeStartElement(VsdxTokens.ROW);
    writer.writeAttribute(VsdxTokens.ATTRIBUTE_NAME, pName);
    //Note skipped most of content, might still work
    writeCell(writer, VsdxTokens.VALUE, pValue, null, VsdxTokens.FORMULA_NONE);
    writeCell(writer, VsdxTokens.LABEL, pName);
    writer.writeEndElement();
  }

  private void writeCell(XMLStreamWriter writer, String nValue, String vValue) throws XMLStreamException {
    writeCell(writer, nValue, vValue, null, null);
  }

  private void writeCell(XMLStreamWriter writer, String nValue, String vValue, String uValue, String fValue) throws XMLStreamException {
    writer.writeEmptyElement(VsdxTokens.CELL);
    writer.writeAttribute(VsdxTokens.ATTRIBUTE_NAME, nValue);
    writer.writeAttribute(VsdxTokens.ATTRIBUTE_VALUE, vValue);
    if (uValue != null) {
      writer.writeAttribute(VsdxTokens.ATTRIBUTE_UNIT, uValue);
    }
    if (fValue != null) {
      writer.writeAttribute(VsdxTokens.ATTRIBUTE_FORMULA, fValue);
    }
  }

  private void writeConnectors(VsdxPageImpl page, XMLStreamWriter writer) throws XMLStreamException {
    if (page.getConnectors().size() > 0) {
      writer.writeStartElement(VsdxTokens.CONNECTS);
      for (VsdxConnector connector : page.getConnectors()) {
        writer.writeEmptyElement(VsdxTokens.CONNECT);
        writer.writeAttribute(VsdxTokens.FROM_SHEET, String.valueOf(connector.getId()));
        writer.writeAttribute(VsdxTokens.FROM_CELL, VsdxTokens.BEGIN_X);
        writer.writeAttribute(VsdxTokens.FROM_PART, String.valueOf(9));
        writer.writeAttribute(VsdxTokens.TO_SHEET, String.valueOf(connector.getSourceShape().getId()));
        writer.writeAttribute(VsdxTokens.TO_CELL, VsdxTokens.STATIC_PIN_X);
        writer.writeAttribute(VsdxTokens.TO_PART, String.valueOf(3));

        writer.writeEmptyElement(VsdxTokens.CONNECT);
        writer.writeAttribute(VsdxTokens.FROM_SHEET, String.valueOf(connector.getId()));
        writer.writeAttribute(VsdxTokens.FROM_CELL, VsdxTokens.END_X);
        writer.writeAttribute(VsdxTokens.FROM_PART, String.valueOf(12));
        writer.writeAttribute(VsdxTokens.TO_SHEET, String.valueOf(connector.getTargetShape().getId()));
        writer.writeAttribute(VsdxTokens.TO_CELL, VsdxTokens.STATIC_PIN_X);
        writer.writeAttribute(VsdxTokens.TO_PART, String.valueOf(3));
      }
      writer.writeEndElement();
    }
  }

  private void writeDocumentFooter(VsdxPageImpl page, XMLStreamWriter writer) throws XMLStreamException {
    //end PageContents element
    writer.writeEndElement();
  }
}
