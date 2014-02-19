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
import java.util.List;

import de.iteratec.visio.model.exceptions.MasterNotFoundException;
import de.iteratec.visio.model.impl.VdxShape;


/**
 * Any element in the Visio model that can contain a Shape instance.
 */
public interface ShapeContainer extends DOMElementWrapper {
  /**
   * Retrieves all direct content of the shape container.
   * 
   * Note that these shapes can contain other shapes. The array returned
   * contains only the children, not all descendants of the container.
   * 
   * @return  An array of all shapes contained directly in this container. 
   *          Can be empty but might not be null.
   */
  List<? extends Shape> getShapes();

  /**
   * All containers should allow access to the document.
   * 
   * This should typically be implemented as a recursion.
   * 
   * @return  The Visio document to which this container belongs to. Might
   *          not be null. 
   */
  Document getDocument();

  /**
   * Creates a new shape with this ShapeContainer as parent.
   * @param masterName
   *          The name of the master to create the shape from.
   * @return The newly created shape
   * @throws MasterNotFoundException
   */
  Shape createNewInnerShape(String masterName) throws MasterNotFoundException;

  /**
   * Creates a new shape with this ShapeContainer as parent.
   * @return The newly created shape
   */
  Shape createNewEmptyInnerShape();

  /**
   * Calculates the bounding box of the shape container.
   * 
   * This is the smallest rectangle all shapes in the shape container fit into.
   * 
   * All the restrictions documented on {@link VdxShape#getBoundingBox()} apply. Shapes
   * for which no bounding box can be calculated (i.e. for which the method throws an
   * IllegalStateException) are ignored -- they are just assumed not to have a
   * bounding box. If no shape has a bounding box, the method throws an exception
   * itself.
   * 
   * @return the bounding box of the underlying shape in Visio's coordinate space. Can
   *         not be null.
   * @throws IllegalStateException if there is no shape on the page or non of the shapes 
   *                               has a bounding box.
   */
  Rectangle2D getBoundingBox() throws IllegalStateException;

  List<? extends Shape> getInnerShapes();
}
