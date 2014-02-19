/*
 * gxl2visio is a library to manipulate visio documents developed by iteratec, GmbH
 * Copyright (C) 2008 iteratec, GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY ITERATEC, ITERATEC DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact iteratec GmbH headquarters at Inselkammerstr. 4
 * 82008 Munich - Unterhaching, Germany, or via email info@iteratec.de.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 */
package de.iteratec.visio.model.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;


public final class VisioDOMUtils {

  public static final String VISIO_NAMESPACE         = "http://schemas.microsoft.com/visio/2003/core";

  public static final String PROPERTY_TAG_NAME       = "Prop";
  public static final String PROPERTY_NAME_ATTRIBUTE = "NameU";
  public static final String PROPERTY_VALUE_TAG      = "Value";
  public static final String UNIT_ATTRIBUTE          = "Unit";
  public static final String STRING_UNIT             = "STR";
  public static final String COLOR_UNIT              = "COLOR";
  public static final String PERCENT_UNIT            = "PER";
  public static final String POINT_UNIT              = "PT";

  public static final String XFORM                   = "XForm";
  public static final String XFORM1D                 = "XForm1D";
  public static final String LINE                    = "Line";
  public static final String TEXT_XFORM              = "TextXForm";
  public static final String TEXT_BLOCK              = "TextBlock";
  public static final String CHAR                    = "Char";
  public static final String PARA                    = "Para";

  private VisioDOMUtils() {
    // utility class, do not instantiate
  }

  /**
   * @param parentElement The element to use as the root for searching.
   * @param childName The name of the direct child to look for.
   * @return The child element for the specified name.
   */
  public static Element getFirstChildWithName(Element parentElement, String childName) {
    List<Element> childrenWithName = getChildrenWithName(parentElement, childName);
    return childrenWithName.isEmpty() ? null : childrenWithName.get(0);
  }

  /**
   * @param parentElement The element to use as the root for searching.
   * @param childName The name of the direct child to look for.
   * @return The list of found child elements.
   */
  public static List<Element> getChildrenWithName(Element parentElement, String childName) {
    List<Element> results = new ArrayList<Element>();
    NodeList childNodes = parentElement.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node child = childNodes.item(i);
      if (childName.equals(child.getNodeName())) {
        results.add((Element) child);
      }
    }
    return results;
  }

  /**
   * Retrieves the first occurance of an element with the given tag name in the search scope 
   * or creates a new one.
   * 
   * If an element with the given tag name is a child of the scope element, this element is
   * returned. Otherwise a new empty element with the given tag name will be created and
   * appended to the parent element.
   * 
   * @param parentElement The element to use as the root for searching.
   * @param tagName The name of the direct child to look for.
   * 
   * @return The first child element with the given name, or a new child with this name.
   */
  public static Element getOrCreateFirstChildWithName(Element parentElement, String tagName) {
    // try finding the requested element
    NodeList childNodes = parentElement.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node curChild = childNodes.item(i);
      if (curChild.getNodeType() == Node.ELEMENT_NODE && tagName.equals(curChild.getNodeName())) {
        return (Element) curChild; // ok, return it
      }
    }
    // we didn't find any such element, create an empty one and return it
    return createChildWithName(parentElement, tagName);
  }

  /**
   * Creates a new child node for the given parent element with the given tag name
   * and appends it to the parent element's children list.
   * @param parentElement
   * @param tagName
   * @return the newly created node
   */
  public static Element createChildWithName(Element parentElement, String tagName) {
    Element createdNode = parentElement.getOwnerDocument().createElement(tagName);
    parentElement.appendChild(createdNode);
    return createdNode;
  }

  /**
   * Retrieves the first child element of a parent element with the given tagName which
   * has the given propertyValue assigned in the given attribute.
   * 
   * @param parentElement The element to use as the root for searching.
   * @param tagName The tag name of the element to look for
   * @param attributeName The name of the attribute used in the search
   * @param attributeValue The value of the attribute used in the search
   * 
   * @return The first property element with the given NameU attribute or null if none exists.
   */
  public static Element getFirstChildWithNameAndAttribute(Element parentElement, String tagName, String attributeName, String attributeValue) {
    String nullSafeAttributeValue = attributeValue == null ? "" : attributeValue;
    List<Element> childrenList = VisioDOMUtils.getChildrenWithName(parentElement, tagName);
    for (Element childElement : childrenList) {
      if (nullSafeAttributeValue.equals(childElement.getAttribute(attributeName))) {
        return childElement;
      }
    }
    return null;
  }

  /**
   * Removes the given element from the given parent
   * @param parentElement
   * @param elementToRemove
   */
  public static void removeElementFromParent(Element parentElement, Element elementToRemove) {
    parentElement.removeChild(elementToRemove);
  }

  public static String readInheritableAttribute(Element element, String attr) {
    String formula = element.getAttribute(attr);
    if ("Inh".equals(formula)) {
      formula = getInheritedAttribute(element, attr);
    }
    return formula;
  }

  public static String getInheritedAttribute(Element element, String attr) {
    ArrayList<Element> path = Lists.newArrayList();
    Element shapeElement = getShapeElement(element, path);
    if (shapeElement == null) {
      return element.getAttribute(attr);
    }

    Element masterElement = getMaster(element.getOwnerDocument(), shapeElement.getAttribute("Master"));
    if (masterElement == null) {
      return element.getAttribute(attr);
    }

    Element templateElement = getTemplateElementFromMaster(path, masterElement);
    if (templateElement == null || !templateElement.hasAttribute(attr)) {
      return element.getAttribute(attr);
    }
    else {
      return templateElement.getAttribute(attr);
    }
  }

  private static Element getTemplateElementFromMaster(ArrayList<Element> path, Element masterElement) {
    Element templateElement = VisioDOMUtils.getFirstChildWithName(masterElement, "Shapes");
    for (Element pathElement : path) {
      if (templateElement == null) {
        return null;
      }
      if (pathElement.hasAttribute("IX")) {
        templateElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(templateElement, pathElement.getTagName(), "IX",
            pathElement.getAttribute("IX"));
      }
      else {
        templateElement = VisioDOMUtils.getFirstChildWithName(templateElement, pathElement.getTagName());
      }
    }
    return templateElement;
  }

  private static Element getMaster(Document document, String masterId) {
    if (masterId.isEmpty()) {
      return null;
    }
    NodeList mastersList = document.getElementsByTagName("Masters");
    Element masterElement = null;
    for (int i = 0; i < mastersList.getLength() && masterElement == null; i++) {
      Node mastersNode = mastersList.item(i);
      masterElement = VisioDOMUtils.getFirstChildWithNameAndAttribute((Element) mastersNode, "Master", "ID", masterId);
    }
    return masterElement;
  }

  private static Element getShapeElement(Element currentElement, List<Element> path) {
    path.add(0, currentElement);
    if (currentElement.getTagName().equals("Shape")) {
      return (Element) currentElement;
    }
    else {
      Node parentNode = currentElement.getParentNode();
      if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
        return getShapeElement((Element) parentNode, path);
      }
      else {
        return null;
      }
    }
  }

  public static void setValue(Element parentElement, String path, String element, Object value) {
    Element pathElement = VisioDOMUtils.getOrCreateFirstChildWithName(parentElement, path);
    Element elementToWrite = VisioDOMUtils.getOrCreateFirstChildWithName(pathElement, element);
    setValue(elementToWrite, value);
  }

  public static void setFormula(Element parentElement, String path, String element, String formula) {
    Element pathElement = VisioDOMUtils.getOrCreateFirstChildWithName(parentElement, path);
    Element elementToWrite = VisioDOMUtils.getOrCreateFirstChildWithName(pathElement, element);
    elementToWrite.setAttribute("F", formula);
  }

  /**
   * Sets the given value as text content of the element and removes the F (formula) attribute.
   * @param elementToWrite
   *          DOM element to write to
   * @param value
   *          Value to write
   */
  public static void setValue(Element elementToWrite, Object value) {
    elementToWrite.setTextContent(String.valueOf(value));
    elementToWrite.removeAttribute("F"); // remove formula so value set will not be overwritten
  }

  public static int getIntegerValue(Element parentElement, String path, String element) {
    String xmlValue = getXmlValue(parentElement, path, element);
    try {
      return Integer.parseInt(xmlValue);
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Value \"" + xmlValue + "\" cannot be parsed to integer for element with path '/" + path + "/" + element + "'.");
    }
  }

  public static double getDoubleValue(Element parentElement, String path, String element) {
    String xmlValue = getXmlValue(parentElement, path, element);
    try {
      return Double.parseDouble(xmlValue);
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Value \"" + xmlValue + "\" cannot be parsed to double for element with path '/" + path + "/" + element + "'.");
    }
  }

  public static String getStringValue(Element parentElement, String path, String element) {
    return getXmlValue(parentElement, path, element);
  }

  /**
   * Searches in the document for a FaceName entry fitting the given fontName and returns its ID.
   * @param document
   *          The document to search in
   * @param fontName
   *          The font name to search for
   * @return ID of the FaceName element defining the font, or "0" for the default font if the font name wasn't found
   */
  public static String getFontId(Document document, String fontName) {
    Element faceNamesElement = getFirstChildWithName(document.getDocumentElement(), "FaceNames");
    if (faceNamesElement != null) {
      Element faceNameElement = getFirstChildWithNameAndAttribute(faceNamesElement, "FaceName", "Name", fontName);
      if (faceNameElement != null) {
        return faceNameElement.getAttribute("ID");
      }
    }
    return "0";
  }

  /**
   * Searches in the document for a FaceName entry fitting the given ID and returns its name.
   * @param document
   *          The document to search in
   * @param id
   *          The id name to search for
   * @return Name of the FaceName element defining the font, or empty string if the ID wasn't found
   */
  public static String getFontName(Document document, String id) {
    Element faceNamesElement = getFirstChildWithName(document.getDocumentElement(), "FaceNames");
    if (faceNamesElement != null) {
      Element faceNameElement = getFirstChildWithNameAndAttribute(faceNamesElement, "FaceName", "ID", id);
      if (faceNameElement != null) {
        return faceNameElement.getAttribute("Name");
      }
    }
    return "";
  }

  /**
   * Sets a hex representation of the given color as text content of the given element and
   * the Unit attribute of the element to Color.
   * @param colorElement
   *          The element to write to
   * @param colorValue
   */
  public static void setColorValue(Element colorElement, Color colorValue) {
    setValue(colorElement, getHexRepresentation(colorValue));
    colorElement.setAttribute(UNIT_ATTRIBUTE, COLOR_UNIT);
  }

  /**
   * Removes all children and descendants of the given Node
   * @param node
   */
  public static void clearNode(Node node) {
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node child = childNodes.item(i);
      if (child.hasChildNodes()) {
        clearNode(child);
      }
      node.removeChild(child);
    }
  }

  /**
   * Returns a hex representation of the given color as string
   * @param color
   *          the color
   * @return String with a hex representation of the color.
   */
  public static String getHexRepresentation(Color color) {
    return "#" + getHexRepresentation(color.getRed()) + getHexRepresentation(color.getGreen()) + getHexRepresentation(color.getBlue());
  }

  private static String getHexRepresentation(int value) {
    String intToStr = Integer.toString(value, 16);
    return value >= 16 ? intToStr : "0" + intToStr;
  }

  private static String getXmlValue(Element parentElement, String path, String element) {
    try {
      Element elementXForm = VisioDOMUtils.getFirstChildWithName(parentElement, path);
      Element elementPinX = VisioDOMUtils.getFirstChildWithName(elementXForm, element);
      String value = elementPinX.getTextContent();
      return value;
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("Shape does not have value for element with path '/" + path + "/" + element + "'.");
    }
  }

}
