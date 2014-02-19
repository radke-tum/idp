package de.iteratec.visio.model.impl;

import de.iteratec.visio.model.GeometryPartField;


/**
 * Used for GeometryParts like MoveTo or LineTo which don't need additional fields
 */
public class VdxSimpleGeomPart extends AVdxGeomPart {

  public VdxSimpleGeomPart(String name) {
    super(name);
    addField(new VdxGeomPartField("X"));
    addField(new VdxGeomPartField("Y"));
  }

  /**
   * Creates a simple geometry part with the given name and x and y values without formulas
   * @param name
   * @param x
   * @param y
   */
  public VdxSimpleGeomPart(String name, double x, double y) {
    super(name);

    VdxGeomPartField xField = new VdxGeomPartField("X");
    xField.setValue(String.valueOf(x));
    xField.setNoFormula(true);
    addField(xField);

    VdxGeomPartField yField = new VdxGeomPartField("Y");
    yField.setValue(String.valueOf(y));
    yField.setNoFormula(true);
    addField(yField);
  }

  public GeometryPartField getX() {
    return getField("X");
  }

  protected void setX(GeometryPartField x) {
    x.setName("X");
    addField(x);
  }

  public GeometryPartField getY() {
    return getField("Y");
  }

  protected void setY(GeometryPartField y) {
    y.setName("Y");
    addField(y);
  }
}
