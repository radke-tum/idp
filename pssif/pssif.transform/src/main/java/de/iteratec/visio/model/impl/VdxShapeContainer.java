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

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import com.google.common.collect.Lists;

import de.iteratec.visio.model.Document;
import de.iteratec.visio.model.IdHandler;
import de.iteratec.visio.model.Master;
import de.iteratec.visio.model.Shape;
import de.iteratec.visio.model.ShapeContainer;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;


/**
 * Abstract class to retrieve implementation-specific versions of shapes and documents. 
 */
public abstract class VdxShapeContainer implements ShapeContainer {

  private static final Logger                  LOGGER      = Logger.getLogger(VdxShapeContainer.class.getName());
  private static final VdxShapeFactory         FACTORY     = VdxShapeFactory.getInstance();
  /**
   * Caches copies of the wrapper classes.
   * 
   * This is a map from the XML shape elements to the matching Shape
   * instances. It is used for performance reasons since this way duplicate
   * shapes can be avoided. These would e.g. be created in the
   * ShapeContainer.getShapes() implementations.
   */
  // TODO: use a more efficient caching mechanism.
  // This map uses the hashCode() value of the shape elements as
  // a key. This causes the entries to be cached only for a limited time.
  // An earlier implementation used the shape itself as a key, which
  // caused a memory leak: The shape is referenced by the value (wrapper) 
  // and acts as a key at the same time. Since the WeakHashMap only cleans
  // up entries where the key is not referenced anymore, this map grew
  // indefinitely.
  protected static final Map<String, VdxShape> SHAPE_CACHE = new WeakHashMap<String, VdxShape>();

  public abstract Document getDocument();

  public List<VdxShape> getShapes() {
    List<VdxShape> result = Lists.newArrayList();
    Element shapesElement = VisioDOMUtils.getFirstChildWithName(getDOMElement(), "Shapes");
    if (shapesElement == null) {
      return result;
    }

    List<Element> shapesList = VisioDOMUtils.getChildrenWithName(shapesElement, "Shape");
    for (int i = 0; i < shapesList.size(); i++) {
      Element shape = shapesList.get(i);
      result.add(getWrapperForShape(shape, this));
    }
    return result;
  }

  public Shape createNewInnerShape(String masterName) throws MasterNotFoundException {
    IdHandler idHandler = getDocument().getIdHandler();
    Master masterShape = getDocument().getMaster(masterName);
    return FACTORY.createNewShape(this, masterShape, idHandler);
  }

  public Shape createNewEmptyInnerShape() {
    IdHandler idHandler = getDocument().getIdHandler();
    return FACTORY.createNewEmptyShape(this, idHandler);
  }

  /**
   * Retrieves a wrapper for the given XML shape element. 
   * 
   * The parent has to be specified since a new wrapper will be created if there is no wrapper
   * yet. Access is meant for the package, e.g. for the implementations of ShapeContainer.getShapes().
   */
  private VdxShape getWrapperForShape(Element shape, VdxShapeContainer parent) {
    String shapeHashCode = Integer.toString(shape.hashCode());
    VdxShape retVal = SHAPE_CACHE.get(shapeHashCode);
    if (retVal == null) {
      retVal = FACTORY.createNewShapeWrapper(shape, parent);
      SHAPE_CACHE.put(shapeHashCode, retVal);
      if (LOGGER.isLoggable(Level.FINEST)) {
        LOGGER.finest("Filling SHAPE_CACHE: " + shapeHashCode);
      }
    }
    else {
      if (LOGGER.isLoggable(Level.FINEST)) {
        LOGGER.finest("SHAPE_CACHE hit:     " + shapeHashCode);
      }
    }
    return retVal;
  }

  public Rectangle2D getBoundingBox() throws IllegalStateException {
    List<VdxShape> shapes = getShapes();
    Rectangle2D bbox = null; // can't place it arbitrarily since that would affect the result
    if (shapes.size() == 0) {
      throw new IllegalStateException("ShapeContainer has no shape on it");
    }
    for (Shape shape : shapes) {
      try {
        if (bbox == null) {
          bbox = shape.getBoundingBox();
        }
        else {
          bbox = bbox.createUnion(shape.getBoundingBox());
        }
      } catch (IllegalStateException e) {
        // ignore, we just don't use that shape
      }
    }
    if (bbox == null) {
      throw new IllegalStateException("No shape in the shape container can provide a bounding box");
    }
    return bbox;
  }

}
