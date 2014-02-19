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

import java.awt.geom.Rectangle2D;
import java.util.List;

import org.w3c.dom.Element;

import de.iteratec.visio.model.Page;
import de.iteratec.visio.model.Shape;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;


/**
 * Wraps a Visio page DOM element.
 */
public class VdxPage extends VdxShapeContainer implements Page {

  /**
   * The internal representation as a DOM element.
   */
  private org.w3c.dom.Element page;

  /**
   * A back-reference to the document we belong to. This is used to access the master shapes.
   */
  private VdxDocument         document;

  /**
   * Constructor to create a wrapper arount the given page DOM element. 
   * 
   * Package-private since it is supposed to be used only by the Document class.
   * 
   * @param page The DOM page element to wrap.
   * @param document The Document this page is part of.
   */
  public VdxPage(Element page, VdxDocument document) {
    this.page = page;
    this.document = document;
  }

  @Override
  public Element getDOMElement() {
    return page;
  }

  /**
   * Sets the dimensions of the page.
   * 
   * @todo this method assumes all elements exist -- it might be better to create them 
   *       on the fly if necessary
   * 
   * @param width  The new width of the page
   * @param height The new height of the page
   */
  public void setSize(double width, double height) {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element pagePropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PageProps");
    Element pageWidthElement = VisioDOMUtils.getFirstChildWithName(pagePropsElement, "PageWidth");
    pageWidthElement.setTextContent(String.valueOf(width));
    Element pageHeightElement = VisioDOMUtils.getFirstChildWithName(pagePropsElement, "PageHeight");
    pageHeightElement.setTextContent(String.valueOf(height));
  }

  /**
   * The width of the page.
   * 
   * @return The width of the page.
   * 
   * @throws IllegalStateException iff the page has no width attached or it can not be parsed
   */
  public double getWidth() {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element pagePropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PageProps");
    Element pageWidthElement = VisioDOMUtils.getFirstChildWithName(pagePropsElement, "PageWidth");
    try {
      return Double.parseDouble(pageWidthElement.getTextContent());
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Height of page can not be parsed");
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("Page has no height attached to it");
    }
  }

  /**
   * The height of the page.
   * 
   * @return The height of the page.
   * 
   * @throws IllegalStateException iff the page has no height attached or it can not be parsed
   */
  public double getHeight() {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element pagePropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PageProps");
    Element pageHeightElement = VisioDOMUtils.getFirstChildWithName(pagePropsElement, "PageHeight");
    try {
      return Double.parseDouble(pageHeightElement.getTextContent());
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Height of page can not be parsed");
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("Page has no height attached to it");
    }
  }

  public VdxDocument getDocument() {
    return this.document;
  }

  public Shape createNewConnector(String masterName, Shape startShape, Shape endShape) throws MasterNotFoundException {
    Shape connector = createNewInnerShape(masterName);

    // get <Connectors> element -- note that this might be created, in which case it has to be after
    // the <Shapes> element. The visio.xsd doesn't require that, but Visio does: if this rule is not
    // followed, the <Connector> elements will not be accepted since their @FromSheet or @ToSheet
    // references will be considered broken.
    Element newConnectsElement = VisioDOMUtils.getOrCreateFirstChildWithName(this.page, "Connects");

    // connect things
    Element firstConnectElement = newConnectsElement.getOwnerDocument().createElement("Connect");
    newConnectsElement.appendChild(firstConnectElement);
    firstConnectElement.setAttribute("FromSheet", connector.getID().toString());
    firstConnectElement.setAttribute("FromPart", String.valueOf(9));// 9 = start point of connector, see e.g.
    // http://msdn.microsoft.com/library/default.asp?url=/library/en-us/devref/HTML/XMLR_Elements_(C)_780.asp
    firstConnectElement.setAttribute("FromCell", "BeginX");
    firstConnectElement.setAttribute("ToSheet", startShape.getID().toString());
    firstConnectElement.setAttribute("ToPart", String.valueOf(3)); // 3 = whole shape (dynamic glue)
    firstConnectElement.setAttribute("ToCell", "PinX"); // when using dynamic connectors, this has
    // to be set to either "PinX" or "PinY", it does not matter which one.

    Element secondConnectElement = newConnectsElement.getOwnerDocument().createElement("Connect");
    newConnectsElement.appendChild(secondConnectElement);
    secondConnectElement.setAttribute("FromSheet", connector.getID().toString());
    secondConnectElement.setAttribute("FromPart", String.valueOf(12)); // 12 = end point of connector
    secondConnectElement.setAttribute("FromCell", "EndX");
    secondConnectElement.setAttribute("ToSheet", endShape.getID().toString());
    secondConnectElement.setAttribute("ToPart", String.valueOf(3)); // 3 = whole shape (dynamic glue)
    secondConnectElement.setAttribute("ToCell", "PinX"); // see above

    return connector;
  }

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
   * @todo this could be generic for ShapeContainer, which would change the semantics
   *       for the Shape class, though
   * 
   * @return the bounding box of the underlying shape in Visio's coordinate space. Can
   *         not be null.
   * 
   * @throws IllegalStateException iff there is no shape on the page or non of the shapes 
   *                               has a bounding box.
   */
  public Rectangle2D getBoundingBox() throws IllegalStateException {
    List<VdxShape> shapes = getShapes();
    Rectangle2D bbox = null; // can't place it arbitrarily since that would affect the result
    if (shapes.size() == 0) {
      throw new IllegalStateException("Page has no shape on it");
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
      throw new IllegalStateException("No shape on the page can provide a bounding box");
    }
    return bbox;
  }

  /**
   * Adjusts the page size so it fits the existing shapes.
   * 
   * The correctness of this function depends on the correctness of 
   * {@link getBoundingBox()}. Additionally only the size will be adjusted, shapes
   * with negative coordinates will cause the page to be of the right size but not
   * yet in the right position.
   * 
   * @todo add moving of shapes if necessary
   * 
   * @param margin  The distance to leave as a margin on each side in inches
   * @param increaseOnly  Iff set the page will never be made smaller, only bigger
   */
  public void adjustSize(double margin, boolean increaseOnly) {
    Rectangle2D bbox = getBoundingBox();
    boolean changed = false;

    double effectiveHeight = getHeight() - 2 * margin;
    double effectiveWidth = getWidth() - 2 * margin;

    if (!increaseOnly || bbox.getWidth() > effectiveWidth) {
      effectiveWidth = bbox.getWidth();
      changed = true;
    }
    if (!increaseOnly || bbox.getHeight() > effectiveHeight) {
      effectiveHeight = bbox.getHeight();
      changed = true;
    }

    if (changed) {
      setSize(effectiveWidth + 2 * margin, effectiveHeight + 2 * margin);
    }
  }

  /**
   * Changes the orientation of the page for printing purposes.
   * 
   * @todo this code is still untested
   * 
   * @param orientation  Either Page.LANDSCAPE or Page.PORTRAIT
   */
  public void setPrintOrientation(Page.Orientation orientation) {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element printPropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PrintProps");
    Element printPageOrientationElement = VisioDOMUtils.getFirstChildWithName(printPropsElement, "PrintPageOrientation");
    printPageOrientationElement.setTextContent(orientation.getVisioValue());
  }

  public void setNumberOfPrintPages(int pagesX, int pagesY) {

    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element printPropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PrintProps");

    Element pagesXElement = VisioDOMUtils.getFirstChildWithName(printPropsElement, "PagesX");
    pagesXElement.setTextContent(String.valueOf(pagesX));
    pagesXElement.removeAttribute("F");

    Element pagesYElement = VisioDOMUtils.getFirstChildWithName(printPropsElement, "PagesY");
    pagesYElement.setTextContent(String.valueOf(pagesY));
    pagesYElement.removeAttribute("F");
  }

  public int getNumberOfPringPagesX() {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element printPropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PrintProps");

    Element pagesXElement = VisioDOMUtils.getFirstChildWithName(printPropsElement, "PagesX");

    int result = 1;
    try {
      result = Integer.parseInt(pagesXElement.getTextContent());
    } catch (NumberFormatException e) {
      throw new IllegalStateException("The number of pages in the X direction is not an integer number.");
    }

    return result;
  }

  public int getNumberOfPringPagesY() {
    Element pageSheetElement = VisioDOMUtils.getFirstChildWithName(this.page, "PageSheet");
    Element printPropsElement = VisioDOMUtils.getFirstChildWithName(pageSheetElement, "PrintProps");

    Element pagesYElement = VisioDOMUtils.getFirstChildWithName(printPropsElement, "PagesY");

    int result = 1;
    try {
      result = Integer.parseInt(pagesYElement.getTextContent());
    } catch (NumberFormatException e) {
      throw new IllegalStateException("The number of pages in the Y direction is not an integer number.");
    }

    return result;
  }

  @Override
  public List<? extends Shape> getInnerShapes() {
    return getShapes();
  }
}
