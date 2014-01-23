package de.iteratec.visio.model.impl;

import de.iteratec.visio.model.GeometryPartField;


/**
 * A freeform geometry part with free access to additional fields so you can build
 * all kinds of Geometry parts.
 */
public class VdxFreeGeomPart extends AVdxGeomPart {

  public VdxFreeGeomPart(String name) {
    super(name);
  }

  public void addField(String name, GeometryPartField field) {
    field.setName(name);
    addField(field);
  }

}
