package de.iteratec.visio.model.impl;

import de.iteratec.visio.model.GeometryPartField;


public class VdxPolyLineGeomPart extends VdxSimpleGeomPart {

  public VdxPolyLineGeomPart() {
    super("PolylineTo");
  }

  /**
   * Sets the polyline formula field as in the geometry section of a Visio shapesheet.
   * typeX and typeY in the polyline formula are each either 1 or 0. 0 means the following
   * entries in the polyline formula are treated as percentages of the shape's width or height respectively.
   * 1 means the entries are treated as coordinates in inch.
   * 
   * The polyline formula only describes the intermediate points of the polyline. The endpoint is set by the
   * X and Y fields of the geometry part. The start point is the "current" point, usually set by the previous
   * geometry part.
   * @param formula
   *          valid entries usually are as follows: "POLYLINE(typeX, typeY, x1, y1, x2, y2, ...)"
   *          where the parameters are float values
   */
  public void setPolyLineFormula(String formula) {
    GeometryPartField formulaField = new VdxGeomPartField("A");
    formulaField.setValue(formula);
    formulaField.setFormula(formula);
    formulaField.setUnit("POLYLINE");
    formulaField.setNoFormula(false);
    addField(formulaField);
  }

  /**
   * @return the polyline formula field entry. See {@link #setPolyLineFormula(String)} for more details.
   */
  public String getPolyLineFormula() {
    GeometryPartField polyLineFormulaField = getField("A");
    if (polyLineFormulaField == null) {
      return "";
    }
    else {
      return polyLineFormulaField.getValue();
    }
  }

}
