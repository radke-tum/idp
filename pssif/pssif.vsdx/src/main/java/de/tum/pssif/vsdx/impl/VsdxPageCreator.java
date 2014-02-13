package de.tum.pssif.vsdx.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import com.google.common.collect.Sets;

import de.tum.pssif.vsdx.exception.VsdxXmlException;
import de.tum.pssif.vsdx.zip.ZipArchiveEntryWithData;


class VsdxPageCreator {

  static VsdxPageCreator INSTANCE = new VsdxPageCreator();

  private VsdxPageCreator() {
    //Nothing
  }

  public VsdxPageImpl create(ZipArchiveEntryWithData pageEntry) {
    return new VsdxPageImpl(pageEntry.getZipEntry(), readShapes(pageEntry.getData()));
  }

  private Set<VsdxShapeImpl> readShapes(byte[] data) {
    Set<VsdxShapeImpl> shapes = Sets.newHashSet();
    InputStream in = new ByteArrayInputStream(data);
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try {
      XMLStreamReader reader = factory.createXMLStreamReader(in);
      Stack<VsdxShapeImpl> shapeStack = new Stack<VsdxShapeImpl>();
      while (reader.hasNext()) {
        int event = reader.next();
        switch (event) {
          case XMLEvent.START_ELEMENT:
            startElement(reader, shapeStack);
            break;
          case XMLEvent.END_ELEMENT:
            endElement(reader, shapeStack, shapes);
          case XMLEvent.CHARACTERS:
            readShapeText(reader, shapeStack);
        }
      }
    } catch (XMLStreamException e) {
      throw new VsdxXmlException("Failed to read masters xml file from visio document.", e);
    }
    return shapes;
  }

  private void startElement(XMLStreamReader reader, Stack<VsdxShapeImpl> stack) {
    if (VsdxTokens.SHAPE.equals(reader.getName().getLocalPart())) {
      int masterId = Integer.valueOf(reader.getAttributeValue(null, VsdxTokens.MASTER)).intValue();
      int shapeId = Integer.valueOf(reader.getAttributeValue(null, VsdxTokens.ID));
      stack.push(new VsdxShapeImpl(shapeId, masterId));
    }
    //TODO connectors
  }

  private void endElement(XMLStreamReader reader, Stack<VsdxShapeImpl> stack, Set<VsdxShapeImpl> topLevelShapes) {
    if (!VsdxTokens.SHAPE.equals(reader.getName().getLocalPart())) {
      return;
    }
    if (stack.size() == 1) {
      topLevelShapes.add(stack.pop());
    }
    else {
      stack.peek().addInnerShape(stack.pop());
    }
  }

  private void readShapeText(XMLStreamReader reader, Stack<VsdxShapeImpl> stack) {
    //Note: this only works because the only text elements
    //(known so far) are the text elements of the shapes.
    if (!stack.isEmpty() && reader.hasText()) {
      stack.peek().setText(reader.getText());
    }
  }
}
