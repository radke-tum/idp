package de.iteratec.visio.model.impl;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.iteratec.visio.model.GeometryPart;
import de.iteratec.visio.model.ShapeGeometry;


public class VdxShapeGeometry implements ShapeGeometry {

  private boolean            noFill;
  private boolean            noLine;
  private boolean            noShow;
  private boolean            noSnap;
  private List<GeometryPart> geometryParts;

  public VdxShapeGeometry() {
    this.geometryParts = Lists.newArrayList();
  }

  @Override
  public boolean isNoFill() {
    return noFill;
  }

  @Override
  public void setNoFill(boolean noFill) {
    this.noFill = noFill;
  }

  @Override
  public boolean isNoLine() {
    return noLine;
  }

  @Override
  public void setNoLine(boolean noLine) {
    this.noLine = noLine;
  }

  @Override
  public boolean isNoShow() {
    return noShow;
  }

  @Override
  public void setNoShow(boolean noShow) {
    this.noShow = noShow;
  }

  @Override
  public boolean isNoSnap() {
    return noSnap;
  }

  @Override
  public void setNoSnap(boolean noSnap) {
    this.noSnap = noSnap;
  }

  @Override
  public List<GeometryPart> getParts() {
    return geometryParts;
  }

  @Override
  public void setParts(List<GeometryPart> parts) {
    this.geometryParts = parts;
  }

  @Override
  public void addGeomPart(GeometryPart part) {
    this.geometryParts.add(part);
  }

  /**
   * Writes a {@link ShapeGeometry} to a DOM shape element 
   */
  public static class Writer {

    /**
     * Writes this geometry to the given DOM element.
     * @param geom the {@link ShapeGeometry} to write.
     * @param shape the DOM element representing the shape this geometry should be set to
     * @param ix the ID of the Geom element to write to (0 based)
     */
    public static void writeToShapeElement(ShapeGeometry geom, Element shape, int ix) {
      Element geomElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, "Geom", "IX", String.valueOf(ix));
      if (geomElement == null) {
        geomElement = VisioDOMUtils.createChildWithName(shape, "Geom");
        geomElement.setAttribute("IX", String.valueOf(ix));
      }
      else {
        VisioDOMUtils.clearNode(geomElement);
      }

      Element noFillElement = VisioDOMUtils.getOrCreateFirstChildWithName(geomElement, "NoFill");
      VisioDOMUtils.setValue(noFillElement, geom.isNoFill() ? 1 : 0);

      Element noLineElement = VisioDOMUtils.getOrCreateFirstChildWithName(geomElement, "NoLine");
      VisioDOMUtils.setValue(noLineElement, geom.isNoLine() ? 1 : 0);

      Element noShowElement = VisioDOMUtils.getOrCreateFirstChildWithName(geomElement, "NoShow");
      VisioDOMUtils.setValue(noShowElement, geom.isNoShow() ? 1 : 0);

      Element noSnapElement = VisioDOMUtils.getOrCreateFirstChildWithName(geomElement, "NoSnap");
      VisioDOMUtils.setValue(noSnapElement, geom.isNoSnap() ? 1 : 0);

      List<GeometryPart> parts = geom.getParts();
      for (int i = 1; i <= parts.size(); i++) {
        AVdxGeomPart.Writer.writeToGeomElement(parts.get(i - 1), geomElement, i);
      }
    }
  }

  /**
   * Reads a {@link ShapeGeometry} from a DOM shape element
   */
  public static class Reader {

    /**
     * Reads all geometries of a DOM shape element into a map mapping
     * the IX id to the ShapeGeometry object
     * @param shape
     * @return
     */
    public static Map<Integer, ShapeGeometry> readAllGeometries(Element shape) {
      Map<Integer, ShapeGeometry> result = Maps.newHashMap();
      List<Element> geomElements = VisioDOMUtils.getChildrenWithName(shape, "Geom");
      for (Element geomElement : geomElements) {
        result.put(Integer.parseInt(geomElement.getAttribute("IX")), readGeomElement(geomElement));
      }
      return result;
    }

    /**
     * Reads geometry information from the given DOM element into this {@link VdxShapeGeometry}.
     * @param shape the DOM element representing the shape this geometry is in.
     * @param ix the ID of the Geom element to read (0 based)
     * @return the {@link VdxShapeGeometry} read from the shape
     */
    public static ShapeGeometry readFromShapeElement(Element shape, int ix) {
      Element geomElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, "Geom", "IX", String.valueOf(ix));
      return readGeomElement(geomElement);
    }

    private static ShapeGeometry readGeomElement(Element geomElement) {
      ShapeGeometry geom = new VdxShapeGeometry();
      if (geomElement != null) {
        Element noFillElement = VisioDOMUtils.getFirstChildWithName(geomElement, "NoFill");
        geom.setNoFill(parseBoolean(noFillElement.getTextContent()));

        Element noLineElement = VisioDOMUtils.getFirstChildWithName(geomElement, "NoLine");
        geom.setNoLine(parseBoolean(noLineElement.getTextContent()));

        Element noShowElement = VisioDOMUtils.getFirstChildWithName(geomElement, "NoShow");
        geom.setNoShow(parseBoolean(noShowElement.getTextContent()));

        Element noSnapElement = VisioDOMUtils.getFirstChildWithName(geomElement, "NoSnap");
        geom.setNoSnap(parseBoolean(noSnapElement.getTextContent()));

        // This code part could be optimized performance-wise. The current implementation iterates over the
        // children nodes of the geomElement several times (once in each "hasPart" call, once in each "read..." call).
        // Should be unnecessary, since here we want to load all parts anyway.
        int partIx = 1;
        while (AVdxGeomPart.Reader.hasPart(geomElement, partIx)) {
          GeometryPart geomPart = AVdxGeomPart.Reader.readFromGeomElement(geomElement, partIx++);
          geom.addGeomPart(geomPart);
        }
      }
      return geom;
    }

    private static boolean parseBoolean(String string) {
      return Boolean.parseBoolean(string) || "1".equals(string);
    }
  }

}
