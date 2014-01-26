package de.iteratec.visio.model;

import java.util.List;


/**
 * Interface for objects representing a part of a visio geometry object
 */
public interface GeometryPart {

  /**
   * Returns the name of this GeometryPart (like MoveTo, LineTo,...) as it appears on the ShapeSheet, too.
   * @return Name of the GeometryPart
   */
  String getName();

  /**
   * Returns a immutable copy of the fields of this {@link GeometryPart}.
   * @return List of {@link GeometryPartField}s
   */
  List<GeometryPartField> getFields();

  /**
   * Returns the field with the given name
   * @param name
   *          The name of the field to return
   * @return the field of the given name
   */
  GeometryPartField getField(String name);

}
