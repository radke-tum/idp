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
package de.iteratec.visio.model;

import java.awt.Color;
import java.math.BigInteger;
import java.util.Map;

import de.iteratec.visio.model.exceptions.MasterNotFoundException;


public interface Shape extends ShapeContainer {

  /**
   * Creates a new Shape as inner shape of this one.
   * 
   * @param masterNameU 
   *            The unique name of the master to use.
   * @return The new inner shape.
   * @throws MasterNotFoundException
   *             If the master can not be found.
   * TODO change signature to use the Master class instead, lookup should not
   *      be the concern of this factory method
   */
  Shape createNewInnerShape(String masterNameU) throws MasterNotFoundException;

  /**
   * Checks if the shape is a group of shapes.
   * 
   * @return true if the Visio shape is a group of other shapes.
   */
  boolean isGroupShape();

  /**
   * Retrieves the master this shape is based upon. The master is a collection
   * of shapes that was used to create this shape. Note that the master is not
   * a shape itself, to get the shape this shape is based upon call
   * {@link getMasterShape()}.
   * 
   * @return The object representing the master.
   * @throws MasterNotFoundException
   *             If no master was found for the shape.
   */
  Master getMaster() throws MasterNotFoundException;

  /**
   * Retrieves the master shape this shape is based upon. This is the shape
   * within the master that was used to creates this shape.
   * 
   * @return The Shape instance representing the master shape.
   * @throws MasterNotFoundException
   *             If no master shape was found for the shape.
   */
  Shape getMasterShape() throws MasterNotFoundException;

  /**
   * Retrieves the Visio identifier for the shape. Each shape in a Visio
   * document has a unique number which is returned here.
   * 
   * @return The Visio identifier for the Shape. Will not be null.
   */
  BigInteger getID();

  /**
   * TODO document this one and the recursive version
   * @param properties
   *            A map from String keys matching the Visio custom property
   *            names to objects that should be set. Supported at the moment
   *            are Strings and Colors as values.
   */
  void setCustomProperties(Map<String, Object> properties);

  /**
   * A map from String keys matching the Visio custom property
   * names to objects that should be set. Supported at the moment
   * are Strings and Colors as values.
   * @return properties-Map propertyName->Object
   */
  Map<String, Object> getCustomProperties();

  /**
   * Sets a custom property of the given name to the given value. Creates the property if
   * it doesn't exist yet and overwrites the value if it does exist.
   * If you assign the value null to a property, the property will be removed.
   * @param propertyName
   *          Name of the property to set
   * @param value
   *          Value the property should be set to
   */
  void setCustomProperty(String propertyName, Object value);

  /**
   * Retrieves the value of the user defined cell as String.
   * 
   * @param cellName
   *            The name of the cell to use.
   * @return The value retrieved, null if there is no such value.
   */
  String getUserCellAsString(String cellName);

  /**
   * Retrieves the value of the user defined cell as double.
   * 
   * @param cellName
   *            The name of the cell to use.
   * @return The value retrieved, zero if there is no such value.
   * @todo check if it wouldn't be better to throw NoSuchElementException
   *       instead of returning zero.
   */
  int getUserCellAsInt(String cellName);

  /**
   * Retrieves the value of the user defined cell as double.
   * 
   * @param cellName
   *            The name of the cell to use.
   * @return The value retrieved, zero if there is no such value.
   * @todo check if it wouldn't be better to throw NoSuchElementException
   *       instead of returning zero.
   */
  double getUserCellAsDouble(String cellName);

  /**
   * Sets the {@link String} value of a user-defined cell.
   * 
   * @param cellName  The unqiue name ("NameU" attribute) of the shape
   * @param value  The new value as {@link String}
   * @return  True, if the user-defined cell has been found and the value has been set correctly.
   */
  boolean setUserCellValue(String cellName, String value);

  /**
   * Sets the int value of a user-defined cell.
   * 
   * @param cellName  The unqiue name ("NameU" attribute) of the shape
   * @param value  The new value as integer
   * @return  True, if the user-defined cell has been found and the value has been set correctly.
   */
  boolean setUserCellValue(String cellName, int value);

  /**
   * Sets the double of a user-defined cell.
   * 
   * @param cellName  The unqiue name ("NameU" attribute) of the shape
   * @param value  The new value as double
   * @return  True, if the user-defined cell has been found and the value has been set correctly.
   */
  boolean setUserCellValue(String cellName, double value);

  /**
   * Places the shape at the given location. Note that the Visio coordinate
   * system is carthesian, i.e. (0,0) is the lower left corner with Y
   * increasing upwards and X increasing rightwards.
   * 
   * @param x
   *            The new horizontal position.
   * @param y
   *            The new vertical position.
   */
  void setPosition(double x, double y);

  /**
   * The horizontal position of the shape. This position is the position of
   * the pin point, which is at the center of most shapes but can be
   * configured to be somewhere else. See {@link #getLocPinX}.
   * 
   * @return The horizontal position of the shape.
   * @throws IllegalStateException
   *             iff the shape has no position attached or it can not be
   *             parsed
   */
  double getPinX();

  /**
   * The vertical position of the shape. This position is the position of the
   * pin point, which is at the center of most shapes but can be configured to
   * be somewhere else. See {@link getLocationPinY}.
   * 
   * @return The vertical position of the shape.
   * @throws IllegalStateException
   *             iff the shape has no position attached or it can not be
   *             parsed
   */
  double getPinY();

  /**
   * Set shape's center of rotation in relation to the origin of the shape.
   */
  void setLocPin(double x, double y);

  /**
   * The horizontal position of the pin point. This position is the relative
   * position of the pin point within the shape, commonly set to the center.
   * Note that this is usually a derived value and this method will only offer
   * access to the calculated value.
   * 
   * @return The horizontal position of the pin point within the shape.
   * @throws IllegalStateException
   *             iff the shape has no position attached or it can not be
   *             parsed
   */
  double getLocPinX();

  /**
   * The vertical position of the pin point. This position is the relative
   * position of the pin point within the shape, commonly set to the center.
   * Note that this is usually a derived value and this method will only offer
   * access to the calculated value.
   * 
   * @return The vertical position of the pin point within the shape.
   * @throws IllegalStateException
   *             iff the shape has no position attached or it can not be
   *             parsed
   */
  double getLocPinY();

  /**
   * Sets the beginning of a 1D shape. Note that the Visio coordinate system
   * is carthesian, i.e. (0,0) is the lower left corner with Y increasing
   * upwards and X increasing rightwards.
   * 
   * @param x
   *            The new horizontal beginning position.
   * @param y
   *            The new vertical beginning position.
   */
  void setBeginPosition(double x, double y);

  /**
   * The beginning horizontal position of a 1D shape.
   * 
   * @return The beginning horizontal position of a 1D shape.
   * @throws IllegalStateException
   *             iff the shape has no beginning position attached or it can
   *             not be parsed
   */
  double getBeginX();

  /**
   * The beginning vertical position of a 1D shape.
   * 
   * @return The beginning vertical position of a 1D shape.
   * @throws IllegalStateException
   *             iff the shape has no beginning position attached or it can
   *             not be parsed
   */
  double getBeginY();

  /**
   * Sets the end of a 1D shape. Note that the Visio coordinate system is
   * carthesian, i.e. (0,0) is the lower left corner with Y increasing upwards
   * and X increasing rightwards.
   * 
   * @param x
   *            The new horizontal end position.
   * @param y
   *            The new vertical end position.
   */
  void setEndPosition(double x, double y);

  /**
   * The beginning horizontal position of a 1D shape.
   * 
   * @return The beginning horizontal position of a 1D shape.
   * @throws IllegalStateException
   *             iff the shape has no beginning position attached or it can
   *             not be parsed
   */
  double getEndX();

  /**
   * The beginning vertical position of a 1D shape.
   * 
   * @return The beginning vertical position of a 1D shape.
   * @throws IllegalStateException
   *             iff the shape has no beginning position attached or it can
   *             not be parsed
   */
  double getEndY();

  /**
   * Sets the angle of the shape.
   * 
   * @param angle
   *            The angle to set the shape to, in radians, zero is upright,
   *            counterclockwise.
   */
  void setAngle(double angle);

  /**
   * The angle the shape is set to. 
   * @return angle in radians, zero is upright, counterclockwise.
   */
  double getAngle();

  /**
   * Resizes the shape to the given dimensions.
   * 
   * @param width
   *            The new width of the shape.
   * @param height
   *            The new height of the shape.
   */
  void setSize(double width, double height);

  /**
   * The width of the shape. This is either the width defined on the shape or
   * the width of the master shape if no there is no explicit width on this
   * shape.
   * 
   * @return The width of the shape.
   * @throws IllegalStateException
   *             iff the shape has no width attached or it can not be parsed
   */
  double getWidth() throws IllegalStateException;

  /**
   * The height of the shape.
   * 
   * @return The height of the shape.
   * @throws IllegalStateException
   *             iff the shape has no height attached or it can not be parsed
   */
  double getHeight();

  /**
   * Set coordinates of the first vertex of the shape.
   * @deprecated as of 1.2.1
   */
  @Deprecated
  void setFirstVertexOfGeometry(double x, double y);

  /**
   * Set elliptical arc of the shape.
   * 
   * @param ix The index of the element within its parent.
   * @param x The x-coordinate of the ending vertex of the arc.
   * @param y The y-coordinate of the ending vertex of the arc.
   * @param a The x-coordinate of the arc's control point.
   * @param b The y-coordinate of the arc's control point.
   * @param c Angle of the arc's major axis relative to the x-axis of its parent.
   * @param d Ratio of the arc's major axis to its minor axis.
   * @deprecated as of 1.2.1
   */
  @Deprecated
  void setEllipticalArc(int ix, double x, double y, double a, double b, double c, double d);

  /**
    * @deprecated as of 1.2.1
    */
  @Deprecated
  void setArc(int ix, double x, double y, double a);

  /**
   * Sets a geometry of the ID ix to this shape.
   * Changes in the ShapeGeometry object won't be automatically written to the shape.
   * Use {@link #setGeometry(ShapeGeometry, int)} again for that.
   * @param geom
   *          {@link ShapeGeometry} object to set
   * @param IX
   *          IX id of the geometry
   */
  void setGeometry(ShapeGeometry geom, int ix);

  /**
   * Removes the geometry with the given IX id from this shape.
   * @param IX
   *          IX id of the geometry
   */
  void removeGeometry(int ix);

  /**
   * Returns the geometries of this shape in a map mapping the IX id of the geometry
   * to the {@link ShapeGeometry} object.
   * Changes in the returned objects won't be automatically written to the shape.
   * Use {@link #setGeometry(ShapeGeometry, int)} for that.
   * @return Map of all geometries
   */
  Map<Integer, ShapeGeometry> getGeometries();

  /**
   * Sets the background color for the fill to the given value. This variant
   * sets the value to an integer which resolves to a predefined color value,
   * which in turn has to be set in the template file this shape is based
   * upon.
   * 
   * @param colorCode
   *            An integer representing a predefined color. Must be
   *            non-negative.
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   * @throws IllegalArgumentException
   *             Iff one of the parameters is outside the range.
   */
  void setFillBackgroundColor(int colorCode, double transparency) throws IllegalArgumentException;

  /**
   * Sets the background color for the fill to the given value. This variant
   * sets the value to the given RGB values.
   * 
   * @param red
   *            The amount of red in the color (within [0,255])
   * @param green
   *            The amount of green in the color (within [0,255])
   * @param blue
   *            The amount of blue in the color (within [0,255])
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   * @throws IllegalArgumentException
   *             Iff one of the parameters is outside the range.
   */
  void setFillBackgroundColor(int red, int green, int blue, double transparency) throws IllegalArgumentException;

  /**
   * Sets the background color for the fill to the given value. This variant
   * sets the value to the given Color object, including the transparency
   * value represented by the color's alpha component.
   * 
   * @param color
   *            An object representing the color and transparency to be set.
   */
  void setFillBackgroundColor(Color color);

  /**
   * Sets the foreground color for the fill to the given value. This variant
   * sets the value to the color given as string in the typical hexadecimal
   * pattern ("#rrggbb" with rr being the red byte, gg the green and bb the
   * blue byte).
   * 
   * @param hexColorString
   *            The string representation of the color.
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   */
  void setFillBackgroundColor(String hexColorString, double transparency) throws IllegalArgumentException;

  /**
   * Sets the foreground color for the fill to the given value. This variant
   * sets the value to an integer which resolves to a predefined color value,
   * which in turn has to be set in the template file this shape is based
   * upon.
   * 
   * @param colorCode
   *            An integer representing a predefined color. Must be
   *            non-negative.
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   * @throws IllegalArgumentException
   *             Iff one of the parameters is outside the range.
   */
  void setFillForegroundColor(int colorCode, double transparency) throws IllegalArgumentException;

  /**
   * Sets the foreground color for the fill to the given value. This variant
   * sets the value to the given RGB values.
   * 
   * @param red
   *            The amount of red in the color (within [0,255])
   * @param green
   *            The amount of green in the color (within [0,255])
   * @param blue
   *            The amount of blue in the color (within [0,255])
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   * @throws IllegalArgumentException
   *             Iff one of the parameters is outside the range.
   */
  void setFillForegroundColor(int red, int green, int blue, double transparency) throws IllegalArgumentException;

  /**
   * Sets the foreground color for the fill to the given value. This variant
   * sets the value to the given Color object, including the transparency
   * value represented by the color's alpha component.
   * 
   * @param color
   *            An object representing the color and transparency to be set.
   */
  void setFillForegroundColor(Color color);

  /**
   * Sets the foreground color for the fill to the given value. This variant
   * sets the value to the color given as string in the typical hexadecimal
   * pattern ("#rrggbb" with rr being the red byte, gg the green and bb the
   * blue byte).
   * 
   * @param hexColorString
   *            The string representation of the color.
   * @param transparency
   *            A value between 0 and 1 representing the transparency of the
   *            shape (0 = opaque, 1 = invisible).
   */
  void setFillForegroundColor(String hexColorString, double transparency) throws IllegalArgumentException;

  /**
   * Sets the line pattern of the shape. This has to match an existing line
   * pattern defined in the Visio template.
   * 
   * @param patternId
   *            The number of the Visio pattern to use.
   */
  void setLinePattern(int patternId);

  /**
   * The line pattern used on the shape. This is an integer matching a list of
   * line patterns Visio manages.
   * 
   * @return The line pattern id used for this shape.
   */
  int getLinePattern();

  /**
   * Sets the weight of the shape's line.
   * 
   * @param weight
   *            The weight for the line measured in inches.
   */
  void setLineWeight(double weight);

  /**
   * The weight of the shape's line. This defines how wide a line is.
   * 
   * @return The line weight in points.
   */
  double getLineWeight();

  /**
   * Sets the line color to the given value.
   * 
   * @param color
   *            An object representing the color and transparency to be set.
   */
  void setLineColor(Color color);

  /**
   * The line color of the shape
   * @return An object representing the color and transparency
   */
  Color getLineColor();

  /**
   * Sets the transparency of the shape's line.
   * 
   * @param transparency
   *            The transparency for the line between 0 (opaque) and 1 (fully transparent)
   */
  void setLineTransparency(double transparency);

  /**
   * The transparency of the shape's line.
   * 
   * @return The transparency for the line between 0 (opaque) and 1 (fully transparent)
   */
  double getLineTransparency();

  /**
   * Set ending vertex of straight line segment
   * 
   * @param ix The index of the element within its parent.
   */
  void setLineEnd(int ix, double x, double y);

  /**
   * Set begin arrow by type (default is 0=no arrow)
   * @param type  Arrow type (from 1-45).
   */
  void setLineBeginArrow(int type);

  /**
   * Set begin arrow formula
   * @param formula  The formula, which must return a result from 0 to 45. 
   */
  void setLineBeginArrow(String formula);

  /**
   * Set size of begin arrow
   * @param size  Arrow size (from 0=Very small to 6=Colossal)
   */
  void setLineBeginArrowSize(int size);

  /**
   * Set formula for size of begin arrow
   * @param size  The formula, which must return a result from 0 to 6. 
   */
  void setLineBeginArrowSize(String formula);

  /**
   * Set end arrow by type (default is 0=no arrow)
   * @param type  Arrow type (from 1-45).
   */
  void setLineEndArrow(int type);

  /**
   * Set end arrow formula
   * @param formula  The formula, which must return a result from 0 to 45. 
   */
  void setLineEndArrow(String formula);

  /**
   * Set size of end arrow
   * @param size  Arrow size (from 0=Very small to 6=Colossal)
   */
  void setLineEndArrowSize(int size);

  /**
   * Set formula for size of end arrow
   * @param size  The formula, which must return a result from 0 to 6. 
   */
  void setLineEndArrowSize(String formula);

  /**
   * Specify positioning information about a shape's text block.
   * 
   * @param pinx The x-coordinate of the center of rotation in relation to the origin of the shape.
   * @param piny The y-coordinate of the center of rotation in relation to the origin of the shape.
   * @param width The width.
   * @param height The height.
   * @param locpinx The x-coordinate of the center of rotation in relation to the origin of the text block. 
   * @param locpiny The y-coordinate of the center of rotation in relation to the origin of the text block.
   * @param angle The current angle of rotation in relation to the x-axis of the shape.
   */
  void setTextXForm(double pinx, double piny, double width, double height, double locpinx, double locpiny, double angle);

  /**
   * Inserts text into the shape. 
   * 
   * This inserts text at the end of the existing text. Existing formatting and/or text is kept.
   * 
   * @param textValue
   *            The text to be inserted.
   *            
   * TODO there is no way to reset the text, style the text or insert field references.
   *      
   *      There should be something along the lines of:
   *        - clearText(): remove all text and field definitions (Visio doesn't like unused
   *                       field definitions)
   *        - insertFontChange(java.awt.Font): creates a &lt;cp&gt; and matching &lt;Char&gt element for 
   *                       the font; could be extended by other aspects of the &lt;Char&gt element such
   *                       as color. Alternatively: do not use Font, but everything by itself.
   *        - insertFieldReference(String): creates a reference to a custom property or other field, i.e.
   *                       a &lt;fld&gt; and a matching &lt;Field&gt; element.
   *        - insertParagraphSetting(.?.): insert the elements required for paragraph formatting
   *      
   *      Alternative: structure all this by having an explicit Text class
   *      
   *      see http://msdn2.microsoft.com/en-us/library/aa218413(office.10).aspx for an overview of
   *      the text features in Visio
   *      
   * @deprecated since 1.2.0: Use {@link #setShapeText(ShapeText)} for text operations instead.
   */
  @Deprecated
  void insertText(String textValue);

  /**
   * Set the (editable) text of the shape.
   * @param text
   * @deprecated since 1.2.0: Use {@link #setShapeText(ShapeText)} for text operations instead.
   */
  @Deprecated
  void setShapeText(String text);

  /**
   * Sets the text, including styling information, for the shape to display.
   * Changes in the ShapeText object won't be automatically written to the shape.
   * Use {@link #setShapeText(ShapeText)} again for that.
   * @param text
   *          {@link ShapeText} containing the text information
   */
  void setShapeText(ShapeText text);

  /**
   * Returns the {@link ShapeText} representing the text and styling information of the shape's displayed text.
   * Changes in this object won't be automatically written back to the shape. Use {@link #setShapeText(ShapeText)}
   * for that.
   * @return {@link ShapeText} containing the text information
   */
  ShapeText getShapeText();

  void setCharSize(double size);

  void setCharStyle(double style);

  void setParaIndFirst(double value);

  void setTextLeftMargin(double value);

  void setTextVertAlign(double value);

  /**
   * Places the text of the shape at the given location. Note that the Visio coordinate
   * system is carthesian, i.e. (0,0) is the lower left corner with Y increasing upwards
   * and X increasing rightwards. The coordinates are relative to the shape's position.
   * 
   * @param x
   *            The new horizontal position.
   * @param y
   *            The new vertical position.
   */
  void setTextElementPosition(double x, double y);

  /**
   * Resizes the shape's text to the given dimensions.
   * 
   * @param width
   *            The new width of the text element.
   * @param height
   *            The new height of the text element.
   */
  void setTextElementSize(double width, double height);

  /**
   * Sets the angle of the shape's text.
   * 
   * @param angle
   *            The angle to set the text to, in radians, zero is upright,
   *            counterclockwise.
   */
  void setTextAngle(double value);

  /**
   * Protect the position of this shape from being edited by the user.
   */
  void lockPosition(int lock);

  /**
   * Protect the size of this shape from being edited by the user.
   */
  void lockSize(int lock);

  ShapeContainer getParent();
}