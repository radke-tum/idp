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

import java.awt.geom.Rectangle2D;

import de.iteratec.visio.model.exceptions.MasterNotFoundException;


public interface Page extends ShapeContainer {
  interface Orientation {
    String getVisioValue();
  }

  /**
   * Represent landscape mode for printing.
   * 
   * @see setPrintOrientation(Orientation)
   */
  public static final Orientation LANDSCAPE = new Orientation() {
                                              public String getVisioValue() {
                                                return "1";
                                              }
                                            };
  /**
   * Represent portrait mode for printing.
   * 
   * @see setPrintOrientation(Orientation)
   */
  public static final Orientation PORTRAIT  = new Orientation() {
                                              public String getVisioValue() {
                                                return "2";
                                              }
                                            };

  /**
   * Creates a new shape on this page based on the master shape with the given name.
   * 
   * Note that we do not use the unique name of the shape since that is not editable in Visio
   * and thus hard to explain to a user of this library.
   * 
   * @param masterName  The name of the master shape in the Visio template used.
   * @return A new shape on the page based on this master shape.
   * @throws MasterNotFoundException  If no matching master can be found.
   */
  Shape createNewInnerShape(String masterName) throws MasterNotFoundException;

  /**
   * Sets the dimensions of the page.
   * 
   * TODO this method assumes all elements exist -- it might be better to create them 
   *       on the fly if necessary
   * 
   * @param width  The new width of the page
   * @param height The new height of the page
   */
  void setSize(double width, double height);

  /**
   * The width of the page.
   * 
   * @return The width of the page.
   * 
   * @throws IllegalStateException if the page has no width attached or it can not be parsed
   */
  double getWidth();

  /**
   * The height of the page.
   * 
   * @return The height of the page.
   * 
   * @throws IllegalStateException if the page has no height attached or it can not be parsed
   */
  double getHeight();

  /**
   * Creates a new connector between two shapes.
   * 
   * @param masterName The name of the master for the connector (1D Shape)
   * @param startShape The shape where the connection should start
   * @param endShape The shape where the connection should end
   * @return The new connector.
   * @throws MasterNotFoundException if the masterName is not a valid name for a master in the document
   */
  Shape createNewConnector(String masterName, Shape startShape, Shape endShape) throws MasterNotFoundException;

  /**
   * Calculates the bounding box of the shape.
   * 
   * This is the smallest rectangle all shapes on the page fit into.
   * 
   * All the restrictions documented on {@link Shape.getBoundingBox()} apply. Shapes
   * for which no bounding box can be calculated (i.e. for which the method throws an
   * IllegalStateException) are ignored -- they are just assumed not to have a
   * bounding box. If no shape has a bounding box, the method throws an exception
   * itself.
   * 
   * TODO this could be generic for ShapeContainer, which would change the semantics
   *       for the Shape class, though
   * 
   * @return the bounding box of the underlying shape in Visio's coordinate space. Can
   *         not be null.
   * 
   * @throws IllegalStateException if there is no shape on the page or non of the shapes 
   *                               has a bounding box.
   */
  Rectangle2D getBoundingBox() throws IllegalStateException;

  /**
   * Adjusts the page size so it fits the existing shapes.
   * 
   * The correctness of this function depends on the correctness of 
   * {@link getBoundingBox()}. Additionally only the size will be adjusted, shapes
   * with negative coordinates will cause the page to be of the right size but not
   * yet in the right position.
   * 
   * TODO add moving of shapes if necessary
   * 
   * @param margin  The distance to leave as a margin on each side in inches
   * @param increaseOnly  If set the page will never be made smaller, only bigger
   */
  void adjustSize(double margin, boolean increaseOnly);

  /**
   * Changes the orientation of the page for printing purposes.
   * 
   * TODO this code is still untested
   * 
   * @param orientation  Either Page.LANDSCAPE or Page.PORTRAIT
   */
  void setPrintOrientation(Orientation orientation);

  /**
   * Sets the number of print pages that this page is mapped to.
   * @param pagesX
   *    The number of pages in the X (horizontal) direction.
   * @param pagesY
   *    The number of pages in the Y (vertical) direction.
   */
  void setNumberOfPrintPages(int pagesX, int pagesY);

  /**
   * Retrieves the number of pages that this page is mapped to for printing.
   * @return
   *    The number of pages in the X (horizontal) direction.
   */
  int getNumberOfPringPagesX();

  /**
   * Retrieves the number of pages that this page is mapped to for printing.
   * @return
   *    The number of pages in the Y (horizontal) direction.
   */
  int getNumberOfPringPagesY();

}