package de.tum.pssif.vsdx.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new ByteArrayInputStream(data));
      document.normalizeDocument();
      Element rootNode = document.getDocumentElement();

      if (rootNode.getFirstChild() != null) {
        return readShapes(rootNode.getFirstChild());
      }
      return Sets.newHashSet();
    } catch (IOException e) {
      throw new VsdxXmlException("Failed to read masters xml file from visio document.", e);
    } catch (ParserConfigurationException e) {
      throw new VsdxXmlException("Failed to read masters xml file from visio document.", e);
    } catch (SAXException e) {
      throw new VsdxXmlException("Failed to read masters xml file from visio document.", e);
    }

  }

  private Set<VsdxShapeImpl> readShapes(Node shapesElement) {
    Set<VsdxShapeImpl> shapesImpl = Sets.newHashSet();
    NodeList shapes = shapesElement.getChildNodes();
    for (int i = 0; i < shapes.getLength(); i++) {
      Node shape = shapes.item(i);
      if (VsdxTokens.SHAPE.equals(shape.getNodeName())) {
        VsdxShapeImpl shapeImpl = readShape(shape);
        if (shapeImpl != null) {
          shapesImpl.add(shapeImpl);
        }
      }
    }
    return shapesImpl;
  }

  private VsdxShapeImpl readShape(Node shapeNode) {
    NamedNodeMap attrsMap = shapeNode.getAttributes();
    Node shapeIdNode = attrsMap.getNamedItemNS(null, VsdxTokens.ID);
    Node masterIdNode = attrsMap.getNamedItemNS(null, VsdxTokens.MASTER);
    String shapeIdString = shapeIdNode != null ? shapeIdNode.getNodeValue() : null;
    String masterIdString = masterIdNode != null ? masterIdNode.getNodeValue() : null;

    if (shapeIdString == null || shapeIdString.trim().isEmpty() || masterIdString == null || masterIdString.trim().isEmpty()) {
      return null;
    }

    int shapeId = Integer.valueOf(shapeIdString).intValue();
    int masterId = Integer.valueOf(masterIdString).intValue();
    VsdxShapeImpl shape = new VsdxShapeImpl(shapeId, masterId);
    Node textNode = locateChildElement(shapeNode, VsdxTokens.TEXT);
    if (textNode != null) {
      shape.setText(seekText(textNode));
      String text = seekText(textNode);
      if (text.trim().isEmpty()) {
        seekText(textNode);
      }
    }
    Node innerShapesNode = locateChildElement(shapeNode, VsdxTokens.SHAPES);
    if (innerShapesNode != null) {
      shape.setInnerShapes(readShapes(innerShapesNode));
    }
    return shape;
  }

  private static String seekText(Node node) {
    if (!node.getTextContent().trim().isEmpty()) {
      return node.getTextContent();
    }
    NodeList children = node.getChildNodes();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < children.getLength(); i++) {
      String childText = seekText(children.item(i));
      if (!childText.trim().isEmpty()) {
        builder.append(childText);
        builder.append(" ");
      }
    }
    return builder.toString();
  }

  private static Node locateChildElement(Node inElement, String localName) {
    //Note: BFS
    NodeList nodeList = inElement.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (localName.equals(node.getNodeName())) {
        return node;
      }
    }
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node shapesNode = locateChildElement(nodeList.item(i), localName);
      if (shapesNode != null) {
        return shapesNode;
      }
    }
    return null;
  }
}
