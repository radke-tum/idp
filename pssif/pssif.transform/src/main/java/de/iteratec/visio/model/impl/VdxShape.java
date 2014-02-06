/*
 * gxl2visio is a library to manipulate visio documents developed by iteratec, GmbH Copyright (C) 2008 iteratec, GmbH This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as published by the Free Software
 * Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN
 * WHICH THE COPYRIGHT IS OWNED BY ITERATEC, ITERATEC DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. This program is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU Affero General Public License along with
 * this program; if not, see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA. You can contact iteratec GmbH headquarters at Inselkammerstr. 4 82008 Munich - Unterhaching, Germany, or via email
 * info@iteratec.de. The interactive user interfaces in modified source and object code versions of this program must display Appropriate Legal
 * Notices, as required under Section 5 of the GNU Affero General Public License version 3.
 */
package de.iteratec.visio.model.impl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.iteratec.visio.model.Document;
import de.iteratec.visio.model.Master;
import de.iteratec.visio.model.Shape;
import de.iteratec.visio.model.ShapeContainer;
import de.iteratec.visio.model.ShapeGeometry;
import de.iteratec.visio.model.ShapeText;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;


/**
 * A representation of a basic Visio shape.
 * 
 * @todo it might be a good idea to seperate Visio's one shape type into three:
 *       group shape, 2D shape and 1D shape. Visio distinguishes only by certain
 *       attributes, but there are a number of interdependencies such as: 
 *       * group shapes don't have master shapes but masters 
 *       * normal 2D shapes might not have masters -- not sure about that one yet 
 *       * only 1D shapes have beginning and end locations, but OTOH most likely no normal positions. 
 *       Quite likely there will be lots more of these dependencies,
 *       most of which (all of which?) could be hidden by having one abstract
 *       Shape class with three subclasses.
 */
public class VdxShape extends VdxShapeContainer implements Shape {

  private static final Logger         LOGGER = Logger.getLogger(VdxShape.class.getName());

  private Element                     shape;
  private ShapeContainer              parent;

  private ShapeText                   shapeText;
  private Map<Integer, ShapeGeometry> geometries;

  /**
   * This constructor is used to wrap pre-existing XML shape elements. 
   * The master will be set to the existing master if possible, null otherwise. 
   * The ID will be left unchanged.
   * 
   * @param shape
   *            The XML shape element to wrap.
   * @param parent
   *            The parent container.
   */
  VdxShape(Element shape, ShapeContainer parent) {
    this.shape = shape;
    this.parent = parent;
  }

  @Override
  public Element getDOMElement() {
    return shape;
  }

  public final Document getDocument() {
    return this.parent.getDocument();
  }

  public final double getUserCellAsDouble(String cellName) {
    String value = getUserCellAsString(cellName);
    return value != null ? Double.parseDouble(value) : 0;
  }

  public final void setFillBackgroundColor(int colorCode, double transparency) throws IllegalArgumentException {
    if (colorCode < 0) {
      throw new IllegalArgumentException("Color codes can not be less than zero");
    }
    setFillBackgroundColorInternal(String.valueOf(colorCode), transparency);
  }

  public final void setFillBackgroundColor(int red, int green, int blue, double transparency) throws IllegalArgumentException {
    if ((red < 0) || (green < 0) || (blue < 0)) {
      throw new IllegalArgumentException("Color component can not be less than zero");
    }
    if ((red > 255) || (green > 255) || (blue > 255)) {
      throw new IllegalArgumentException("Color component can not be more than 255");
    }
    setFillBackgroundColorInternal(VisioDOMUtils.getHexRepresentation(new Color(red, green, blue)), transparency);
  }

  public final void setFillBackgroundColor(Color color) {
    setFillBackgroundColorInternal(VisioDOMUtils.getHexRepresentation(color), (255 - color.getAlpha()) / 255d);
  }

  public final void setFillBackgroundColor(String hexColorString, double transparency) throws IllegalArgumentException {
    String colorString = validateAndCleanColorString(hexColorString);
    setFillBackgroundColorInternal(colorString, transparency);
  }

  protected final static String validateAndCleanColorString(String hexColorString) throws IllegalArgumentException {
    String colorString;
    if (hexColorString.startsWith("#")) {
      colorString = hexColorString.toLowerCase();
    }
    else {
      colorString = "#" + hexColorString.toLowerCase();
    }
    if (!colorString.matches("#[0-9a-f]{6}")) {
      throw new IllegalArgumentException("Color string '" + hexColorString + "' does not match the expected pattern '#rrggbb'");
    }
    return colorString;
  }

  protected static final void validateTransparency(double transparency) throws IllegalArgumentException {
    if (transparency < 0) {
      throw new IllegalArgumentException("Transparency can not be less than zero");
    }
    if (transparency > 1) {
      throw new IllegalArgumentException("Transparency can not be more than one");
    }
  }

  public final void setFillForegroundColor(int colorCode, double transparency) throws IllegalArgumentException {
    if (colorCode < 0) {
      throw new IllegalArgumentException("Color codes can not be less than zero");
    }
    setFillForegroundColorInternal(String.valueOf(colorCode), transparency);
  }

  public final void setFillForegroundColor(int red, int green, int blue, double transparency) throws IllegalArgumentException {
    if ((red < 0) || (green < 0) || (blue < 0)) {
      throw new IllegalArgumentException("Color component can not be less than zero");
    }
    if ((red > 255) || (green > 255) || (blue > 255)) {
      throw new IllegalArgumentException("Color component can not be more than 255");
    }
    setFillForegroundColorInternal(VisioDOMUtils.getHexRepresentation(new Color(red, green, blue)), transparency);
  }

  public final void setFillForegroundColor(Color color) {
    setFillForegroundColorInternal(VisioDOMUtils.getHexRepresentation(color), (255 - color.getAlpha()) / 255d);
  }

  public final void setFillForegroundColor(String hexColorString, double transparency) throws IllegalArgumentException {
    String colorString = validateAndCleanColorString(hexColorString);
    setFillForegroundColorInternal(colorString, transparency);
  }

  public int getUserCellAsInt(String cellName) {
    String value = getUserCellAsString(cellName);
    return value != null ? Integer.parseInt(value) : 0;
  }

  public String getUserCellAsString(String cellName) {
    List<Element> userList = VisioDOMUtils.getChildrenWithName(this.shape, "User");
    for (Iterator<Element> iter = userList.iterator(); iter.hasNext();) {
      Element userElement = iter.next();
      if (cellName.equals(userElement.getAttribute("NameU"))) {
        Element valueElement = VisioDOMUtils.getFirstChildWithName(userElement, VisioDOMUtils.PROPERTY_VALUE_TAG);
        // a cursor was used in the XMLBeans implementation. Is a cursor necessary?
        return valueElement.getTextContent();
      }
    }
    return null;
  }

  @Override
  public boolean setUserCellValue(String cellName, String value) {
    List<Element> userList = VisioDOMUtils.getChildrenWithName(this.shape, "User");
    boolean hasValueBeenSet = false;
    for (Iterator<Element> iter = userList.iterator(); iter.hasNext();) {
      Element userElement = iter.next();
      if (cellName.equals(userElement.getAttribute("NameU"))) {
        Element valueElement = VisioDOMUtils.getFirstChildWithName(userElement, VisioDOMUtils.PROPERTY_VALUE_TAG);
        valueElement.setTextContent(value);
        hasValueBeenSet = true;
      }
    }
    return hasValueBeenSet;
  }

  @Override
  public boolean setUserCellValue(String cellName, int value) {
    return setUserCellValue(cellName, String.valueOf(value));
  }

  @Override
  public boolean setUserCellValue(String cellName, double value) {
    return setUserCellValue(cellName, String.valueOf(value));
  }

  /**
   * Calculates the bounding box of the shape. The bounding box is the
   * smallest upright rectangle that includes all of the shape. Takes
   * contained shapes into account.
   * 
   * TODO The calculation of the represented shape's bounding box, depending
   * on the pin point, needs to be looked at.
   * 
   * @return the bounding box of the underlying shape in Visio's coordinate
   *         space
   * @throws IllegalStateException
   *             if the shape has not been placed and sized yet
   */
  public final Rectangle2D getBoundingBox() throws IllegalStateException {
    double minX = getPinX() - getLocPinX();
    double maxX = getPinX() - getLocPinX() + getWidth();
    double minY = getPinY() - getLocPinY();
    double maxY = getPinY() - getLocPinY() + getHeight();

    double width = maxX - minX;
    double height = maxY - minY;

    Rectangle2D boundingBox = new Rectangle2D.Double(minX + width / 2, minY + height / 2, width, height);

    if (!getShapes().isEmpty()) {
      Rectangle2D containedShapesBB = super.getBoundingBox();
      boundingBox = containedShapesBB.createUnion(boundingBox);
    }

    return boundingBox;
  }

  public final void setBeginPosition(double x, double y) {
    setBeginX(x);
    setBeginY(y);
  }

  public final void setEndPosition(double x, double y) {
    setEndX(x);
    setEndY(y);
  }

  public final void setPosition(double x, double y) {
    setPinX(x);
    setPinY(y);
  }

  public final void setSize(double width, double height) {
    setWidth(width);
    setHeight(height);
  }

  //*****************************

  protected Element getOrCreatePropertyValueElement(String propertyNameU) {
    Element propElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.PROPERTY_TAG_NAME,
        VisioDOMUtils.PROPERTY_NAME_ATTRIBUTE, propertyNameU);
    if (propElement == null) {
      // no property with that NameU found, let's create one
      propElement = shape.getOwnerDocument().createElement(VisioDOMUtils.PROPERTY_TAG_NAME);
      propElement.setAttribute(VisioDOMUtils.PROPERTY_NAME_ATTRIBUTE, propertyNameU);

      List<Element> propList = VisioDOMUtils.getChildrenWithName(shape, VisioDOMUtils.PROPERTY_TAG_NAME);
      if (propList.isEmpty()) {
        // no <Prop> found, insert at front so we don't break the <Prop>s before <Shapes> rule
        // getFirstChild() == null is ok since it is specified to append
        shape.insertBefore(propElement, shape.getFirstChild());
      }
      else {
        Node lastPropElement = propList.get(propList.size() - 1);
        // getNextSibling() == null is ok since it appends
        shape.insertBefore(propElement, lastPropElement.getNextSibling());
      }
    }
    return VisioDOMUtils.getOrCreateFirstChildWithName(propElement, VisioDOMUtils.PROPERTY_VALUE_TAG);
  }

  /**
   * Sets the value as element text for the given value element.
   * 
   * Note: Visio does not display some custom properties if the attribute F is set to "No Formula".
   * Thus, it is removed completely because it has no other effect.
   *  
   * @param valueElement The element for which the value should be set
   * @param value The value string to set as the text of the element.
   */
  protected void setCustomStringProperty(Element valueElement, String value) {
    valueElement.setAttribute(VisioDOMUtils.UNIT_ATTRIBUTE, VisioDOMUtils.STRING_UNIT);
    valueElement.removeAttribute("V");
    valueElement.removeAttribute("F");
    valueElement.setTextContent(value);
  }

  protected void setCustomColorProperty(Element valueElement, Color colorVal) {
    valueElement.setAttribute(VisioDOMUtils.UNIT_ATTRIBUTE, VisioDOMUtils.COLOR_UNIT);
    valueElement.removeAttribute("V");
    // create hex representation
    String hexRepresentation = VisioDOMUtils.getHexRepresentation(colorVal);
    //a cursor was used in the XMLBeans implementation. Is a cursor necessary? 
    valueElement.setTextContent(hexRepresentation);
  }

  protected Element getFillElement() {
    return VisioDOMUtils.getOrCreateFirstChildWithName(this.shape, "Fill");
  }

  protected Element getText() {
    return VisioDOMUtils.getOrCreateFirstChildWithName(this.shape, "Text");
  }

  protected void rescaleFont(double scaleNeeded) {
    // copy from master to have full font info
    Element charElement = VisioDOMUtils.getFirstChildWithName(shape, VisioDOMUtils.CHAR);
    Element sizeElement = VisioDOMUtils.getFirstChildWithName(charElement, "Size");
    double fontSize = Double.parseDouble(sizeElement.getTextContent());
    sizeElement.setTextContent(String.valueOf(scaleNeeded * fontSize));
    if (sizeElement.getAttributeNode("F") != null) {
      sizeElement.removeAttribute("F"); // make sure it doesn't inherit the value from the master again
    }
  }

  public Master getMaster() throws MasterNotFoundException {
    String masterIdAsString = this.shape.getAttribute("Master");
    if (masterIdAsString.length() == 0) {
      throw new MasterNotFoundException("Shape has no master attached.");
    }
    BigInteger masterId = new BigInteger(masterIdAsString);
    return getDocument().getMaster(masterId);
  }

  public Shape getMasterShape() throws MasterNotFoundException {
    String masterShapeIdAsString = this.shape.getAttribute("MasterShape");
    if (masterShapeIdAsString.length() == 0) {
      // the outermost shape of a group has only a reference to the master, but
      // it is based the outermost shape in the master (of type 'group')
      String masterIdAsString = this.shape.getAttribute("Master");
      if (masterIdAsString.length() == 0) {
        throw new MasterNotFoundException("Shape has no master shape attached.");
      }
      else {
        return getMaster().getShapes().get(0);
      }
    }
    BigInteger masterShapeId = new BigInteger(masterShapeIdAsString);
    List<? extends Shape> masterShapes = getMaster().getShapes();
    for (Shape masterShape : masterShapes) {
      if (masterShape.getID().equals(masterShapeId)) {
        return masterShape;
      }
    }
    throw new MasterNotFoundException("Can not find shape in master.");
  }

  public void setCustomProperties(Map<String, Object> properties) {
    for (Map.Entry<String, Object> propEntry : properties.entrySet()) {
      setCustomProperty(propEntry.getKey(), propEntry.getValue());
    }
  }

  public void setCustomProperty(String key, Object value) {
    if (value == null) {
      removeProperty(key);
      LOGGER.info("Attribute \"" + key + "\" was removed because null was given as value.");
    }
    else {
      Element valueElement = getOrCreatePropertyValueElement(key);
      if (value instanceof String) {
        setCustomStringProperty(valueElement, (String) value);
      }
      else if (value instanceof Color) {
        setCustomColorProperty(valueElement, (Color) value);
      }
      else {
        setCustomStringProperty(valueElement, String.valueOf(value));
        LOGGER.warning("Attribute \"" + key + "\" has value \"" + value + "\" of unrecognized type \"" + value.getClass().getSimpleName()
            + "\". Writing as string.");
      }
    }
  }

  private void removeProperty(String key) {
    Element propElementToRemove = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.PROPERTY_TAG_NAME,
        VisioDOMUtils.PROPERTY_NAME_ATTRIBUTE, key);
    if (propElementToRemove != null) {
      VisioDOMUtils.removeElementFromParent(shape, propElementToRemove);
    }
  }

  public Map<String, Object> getCustomProperties() {
    Map<String, Object> result = Maps.newHashMap();

    List<Element> propList = VisioDOMUtils.getChildrenWithName(shape, VisioDOMUtils.PROPERTY_TAG_NAME);
    for (Element propElement : propList) {
      String propName = propElement.getAttribute(VisioDOMUtils.PROPERTY_TAG_NAME);
      Element valueElement = VisioDOMUtils.getFirstChildWithName(propElement, "value");
      String unit = valueElement.getAttribute(VisioDOMUtils.UNIT_ATTRIBUTE);

      Object value = valueElement.getTextContent();
      if (VisioDOMUtils.COLOR_UNIT.equals(unit)) {
        value = Color.decode(valueElement.getTextContent());
      }

      result.put(propName, value);
    }

    return result;
  }

  public void insertText(String textValue) {
    Element textElement = getText();
    Node lastChild = textElement.getLastChild();
    if (lastChild.getNodeType() == Node.TEXT_NODE) {
      if ("".equals(lastChild.getTextContent().trim())) {
        // remove last child, if it contains only whitespace:
        // necessary to get rid of a potential linebreak in the visio template
        // Assumption: Visio puts a linebreak if the text of a shape only consists of a field.
        textElement.removeChild(lastChild);
      }
    }
    textElement.appendChild(shape.getOwnerDocument().createTextNode(textValue));
  }

  public BigInteger getID() {
    return new BigInteger(this.shape.getAttribute("ID"));
  }

  protected void setFillForegroundColorInternal(String colorValue, double transparency) throws IllegalArgumentException {
    validateTransparency(transparency);
    Element fillElement = getFillElement();
    Element fillForegndElement = VisioDOMUtils.getOrCreateFirstChildWithName(fillElement, "FillForegnd");
    fillForegndElement.setTextContent(colorValue);
    Element fillForegndTransElement = VisioDOMUtils.getOrCreateFirstChildWithName(fillElement, "FillForegndTrans");
    fillForegndTransElement.setTextContent(String.valueOf(transparency));
  }

  protected void setFillBackgroundColorInternal(String colorValue, double transparency) throws IllegalArgumentException {
    validateTransparency(transparency);
    Element fillElement = getFillElement();
    Element fillBkgndElement = VisioDOMUtils.getOrCreateFirstChildWithName(fillElement, "FillBkgnd");
    fillBkgndElement.setTextContent(colorValue);
    Element fillBkgndTransElement = VisioDOMUtils.getOrCreateFirstChildWithName(fillElement, "FillBkgndTrans");
    fillBkgndTransElement.setTextContent(String.valueOf(transparency));
  }

  public boolean isGroupShape() {
    return "Group".equals(shape.getAttributeNS(VisioDOMUtils.VISIO_NAMESPACE, "Type"));
  }

  public final void setShapeText(String text) {
    Element textElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Text");
    textElement.setTextContent(text);
    textElement.removeAttribute("F");
    textElement.removeAttribute("V");
  }

  public final void setShapeText(ShapeText shapeText) {
    this.shapeText = new VdxShapeText(shapeText);
    VdxShapeText.Writer.writeToShapeElement(shapeText, shape);
  }

  public final ShapeText getShapeText() {
    if (shapeText == null) {
      shapeText = VdxShapeText.Reader.readFromShapeElement(shape);
    }
    return new VdxShapeText(shapeText);
  }

  public final void setLocPin(double x, double y) {
    setLocPinX(x);
    setLocPinY(y);
  }

  public final void setFirstVertexOfGeometry(double x, double y) {
    Element geomElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Geom");
    Element moveToElement = VisioDOMUtils.getFirstChildWithName(geomElement, "MoveTo");
    VisioDOMUtils.getFirstChildWithName(moveToElement, "X").setTextContent(String.valueOf(x));
    VisioDOMUtils.getFirstChildWithName(moveToElement, "Y").setTextContent(String.valueOf(y));
  }

  public final void setLineEnd(int ix, double x, double y) {
    Element geomElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Geom");
    List<Element> points = VisioDOMUtils.getChildrenWithName(geomElement, "LineTo");
    for (Element point : points) {
      if (point.getAttribute("IX").equals(String.valueOf(ix))) {
        VisioDOMUtils.getFirstChildWithName(point, "X").setTextContent(String.valueOf(x));
        VisioDOMUtils.getFirstChildWithName(point, "Y").setTextContent(String.valueOf(y));
      }
    }
  }

  public final void setEllipticalArc(int ix, double x, double y, double a, double b, double c, double d) {
    Element geomElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Geom");
    List<Element> arcs = VisioDOMUtils.getChildrenWithName(geomElement, "EllipticalArcTo");
    for (Element arc : arcs) {
      if (arc.getAttribute("IX").equals(String.valueOf(ix))) {
        VisioDOMUtils.getFirstChildWithName(arc, "X").setTextContent(String.valueOf(x));
        VisioDOMUtils.getFirstChildWithName(arc, "Y").setTextContent(String.valueOf(y));
        VisioDOMUtils.getFirstChildWithName(arc, "A").setTextContent(String.valueOf(a));
        VisioDOMUtils.getFirstChildWithName(arc, "B").setTextContent(String.valueOf(b));
        VisioDOMUtils.getFirstChildWithName(arc, "C").setTextContent(String.valueOf(c));
        VisioDOMUtils.getFirstChildWithName(arc, "D").setTextContent(String.valueOf(d));
      }
    }
  }

  public final void setArc(int ix, double x, double y, double a) {
    Element geomElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Geom");
    List<Element> arcs = VisioDOMUtils.getChildrenWithName(geomElement, "ArcTo");
    for (Element arc : arcs) {
      if (arc.getAttribute("IX").equals(String.valueOf(ix))) {
        VisioDOMUtils.getOrCreateFirstChildWithName(arc, "X").setTextContent(String.valueOf(x));
        VisioDOMUtils.getOrCreateFirstChildWithName(arc, "Y").setTextContent(String.valueOf(y));
        VisioDOMUtils.getOrCreateFirstChildWithName(arc, "A").setTextContent(String.valueOf(a));
      }
    }
  }

  public void removeGeometry(int ix) {
    if (geometries == null) {
      getGeometries();
    }
    geometries.remove(Integer.valueOf(ix));
    Element geomElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, "Geom", "IX", String.valueOf(ix));
    VisioDOMUtils.removeElementFromParent(shape, geomElement);
  }

  public void setGeometry(ShapeGeometry geom, int ix) {
    if (geometries == null) {
      getGeometries();
    }
    geometries.put(Integer.valueOf(ix), geom);
    VdxShapeGeometry.Writer.writeToShapeElement(geom, shape, ix);
  }

  public Map<Integer, ShapeGeometry> getGeometries() {
    if (geometries == null) {
      geometries = VdxShapeGeometry.Reader.readAllGeometries(shape);
    }
    // TODO should return copies of the geometries
    return geometries;
  }

  public final void setTextXForm(double pinx, double piny, double width, double height, double locpinx, double locpiny, double angle) {

    setTxtPinX(pinx);
    setTxtPinY(piny);
    setTxtWidth(width);
    setTxtHeight(height);
    setTxtLocPinX(locpinx);
    setTxtLocPinY(locpiny);
    setTextAngle(angle);
  }

  public void setTextElementPosition(double x, double y) {
    setTxtPinX(x);
    setTxtPinY(y);
  }

  public void setTextElementSize(double width, double height) {
    setTxtWidth(width);
    setTxtHeight(height);
  }

  public final void lockPosition(int lock) {

    Element protElement = VisioDOMUtils.getOrCreateFirstChildWithName(this.shape, "Protection");
    Element lockElement = VisioDOMUtils.getOrCreateFirstChildWithName(protElement, "LockMoveX");
    lockElement.setTextContent(Integer.toString(lock));

    lockElement = VisioDOMUtils.getOrCreateFirstChildWithName(protElement, "LockMoveY");
    lockElement.setTextContent(Integer.toString(lock));
  }

  public final void lockSize(int lock) {

    Element protElement = VisioDOMUtils.getFirstChildWithName(this.shape, "Protection");
    Element lockElement = VisioDOMUtils.getFirstChildWithName(protElement, "LockWidth");
    lockElement.setTextContent(Integer.toString(lock));

    lockElement = VisioDOMUtils.getFirstChildWithName(protElement, "LockHeight");
    lockElement.setTextContent(Integer.toString(lock));
  }

  //*****************************
  // XForm getter
  //*****************************
  @Override
  public double getAngle() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "Angle");
  }

  @Override
  public double getPinX() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "PinX");
  }

  @Override
  public double getPinY() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "PinY");
  }

  @Override
  public double getLocPinX() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "LocPinX");
  }

  @Override
  public double getLocPinY() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "LocPinY");
  }

  @Override
  public double getWidth() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "Width");
  }

  @Override
  public double getHeight() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM, "Height");
  }

  @Override
  public double getBeginX() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM1D, "BeginX");
  }

  @Override
  public double getBeginY() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM1D, "BeginY");
  }

  @Override
  public double getEndX() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM1D, "EndX");
  }

  @Override
  public double getEndY() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.XFORM1D, "EndY");
  }

  @Override
  public double getLineWeight() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.LINE, "LineWeight");
  }

  @Override
  public int getLinePattern() {
    return VisioDOMUtils.getIntegerValue(shape, VisioDOMUtils.LINE, "LinePattern");
  }

  @Override
  public Color getLineColor() {
    String colorString = VisioDOMUtils.getStringValue(shape, VisioDOMUtils.LINE, "LineColor");
    Color simpleColor = Color.decode(colorString);
    double transparency = VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.LINE, "LineColorTrans");

    return new Color(simpleColor.getRed(), simpleColor.getGreen(), simpleColor.getBlue(), Math.round(transparency * 255));
  }

  @Override
  public double getLineTransparency() {
    return VisioDOMUtils.getDoubleValue(shape, VisioDOMUtils.LINE, "LineColorTrans");
  }

  @Override
  public void setLineBeginArrow(int type) {
    if (type < 0 || type > 45) {
      throw new IllegalArgumentException("Arrow type must be in the range of 0 to 45!");
    }
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "BeginArrow", type);
  }

  @Override
  public void setLineBeginArrow(String formula) {
    VisioDOMUtils.setFormula(shape, VisioDOMUtils.LINE, "BeginArrow", formula);
  }

  @Override
  public void setLineBeginArrowSize(int size) {
    if (size < 0 || size > 6) {
      throw new IllegalArgumentException("Arrow size must be in the range of 0 to 6!");
    }
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "BeginArrowSize", size);
  }

  @Override
  public void setLineBeginArrowSize(String formula) {
    VisioDOMUtils.setFormula(shape, VisioDOMUtils.LINE, "BeginArrowSize", formula);
  }

  @Override
  public void setLineEndArrow(int type) {
    if (type < 0 || type > 45) {
      throw new IllegalArgumentException("Arrow type must be in the range of 0 to 45!");
    }
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "EndArrow", type);
  }

  @Override
  public void setLineEndArrow(String formula) {
    VisioDOMUtils.setFormula(shape, VisioDOMUtils.LINE, "EndArrow", formula);
  }

  @Override
  public void setLineEndArrowSize(int size) {
    if (size < 0 || size > 6) {
      throw new IllegalArgumentException("Arrow size must be in the range of 0 to 6!");
    }
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "EndArrowSize", size);
  }

  @Override
  public void setLineEndArrowSize(String formula) {
    VisioDOMUtils.setFormula(shape, VisioDOMUtils.LINE, "EndArrowSize", formula);
  }

  @Override
  public void setAngle(double angle) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "Angle", angle);
  }

  public void setPinX(double x) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "PinX", x);
  }

  public void setPinY(double y) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "PinY", y);
  }

  public void setWidth(double width) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "Width", width);
  }

  public void setHeight(double height) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "Height", height);
  }

  public void setLocPinX(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "LocPinX", value);
  }

  public void setLocPinY(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM, "LocPinY", value);
  }

  public void setBeginX(double x) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM1D, "BeginX", x);
  }

  public void setBeginY(double y) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM1D, "BeginY", y);
  }

  public void setEndX(double x) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM1D, "EndX", x);
  }

  public void setEndY(double y) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.XFORM1D, "EndY", y);
  }

  @Override
  public void setLineWeight(double weight) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "LineWeight", weight);
  }

  @Override
  public void setLinePattern(int patternId) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "LinePattern", patternId);
  }

  @Override
  public void setLineColor(Color color) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "LineColor", VisioDOMUtils.getHexRepresentation(color));
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "LineColorTrans", (255 - color.getAlpha()) / 255d);
  }

  @Override
  public void setLineTransparency(double transparency) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.LINE, "LineColorTrans", transparency);
  }

  public void setTxtPinX(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtPinX", value);
  }

  public void setTxtPinY(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtPinY", value);
  }

  public void setTxtWidth(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtWidth", value);
  }

  public void setTxtHeight(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtHeight", value);
  }

  public void setTxtLocPinX(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtLocPinX", value);
  }

  public void setTxtLocPinY(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtLocPinY", value);
  }

  public void setTextAngle(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_XFORM, "TxtAngle", value);
  }

  //*****************************
  // TextBlock setter
  //*****************************
  @Override
  public void setTextLeftMargin(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_BLOCK, "LeftMargin", value);
  }

  @Override
  public void setTextVertAlign(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.TEXT_BLOCK, "VerticalAlign", value);
  }

  //*****************************
  // Char setter
  //*****************************
  @Override
  public void setCharSize(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.CHAR, "Size", value);
  }

  @Override
  public void setCharStyle(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.CHAR, "Style", value);
  }

  //*****************************
  // Paragraph setter
  //*****************************
  @Override
  public void setParaIndFirst(double value) {
    VisioDOMUtils.setValue(shape, VisioDOMUtils.PARA, "IndFirst", value);
  }

  @Override
  public ShapeContainer getParent() {
    return parent;
  }

  @Override
  public List<? extends Shape> getInnerShapes() {
    // TODO fetch inner shapes from DOM, see master and page
    return Lists.newArrayList();
  }

}