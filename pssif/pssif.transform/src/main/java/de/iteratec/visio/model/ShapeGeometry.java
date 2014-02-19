package de.iteratec.visio.model;

import java.util.List;


/**
 * Interface to manipulate a geometry element of a visio shape
 */
public interface ShapeGeometry {

  /**
   * Returns whether the fill of this geometry will be displayed or not.
   * @return true if fill will not be displayed, false otherwise
   */
  boolean isNoFill();

  /**
   * Sets whether the fill of this geometry should be displayed or not. 
   * @param noFill true if fill should not be displayed, false otherwise
   */
  void setNoFill(boolean noFill);

  /**
   * Returns whether the lines of this geometry will be displayed or not.
   * @return true if fill will not be displayed, false otherwise
   */
  boolean isNoLine();

  /**
   * Sets whether the lines of this geometry should be displayed or not. 
   * @param noLine true if lines should not be displayed, false otherwise
   */
  void setNoLine(boolean noLine);

  /**
   * Returns whether this geometry will be displayed or not.
   * @return true if the geometry will not be displayed, false otherwise
   */
  boolean isNoShow();

  /**
   * Sets whether this geometry should be displayed or not. 
   * @param noShow true if the geometry should not be displayed, false otherwise
   */
  void setNoShow(boolean noShow);

  /**
   * Returns whether this geometry will allow other shapes to snap to it.
   * @return true if the geometry will not allow snapping, false otherwise
   */
  boolean isNoSnap();

  /**
   * Sets whether this geometry should allow other shapes to snap to it.
   * @param noSnap true if the geometry should not allow snapping, false otherwise
   */
  void setNoSnap(boolean noSnap);

  /**
   * Returns a list of the geometry's parts.
   * @return List of {@link GeometryPart}s
   */
  List<GeometryPart> getParts();

  /**
   * Sets the list of the geometry's parts.
   * @param parts List of {@link GeometryPart}s
   */
  void setParts(List<GeometryPart> parts);

  /**
   * Adds a new Geometry part to the end of the current parts
   * @param part a {@link GeometryPart}
   */
  void addGeomPart(GeometryPart part);

}
