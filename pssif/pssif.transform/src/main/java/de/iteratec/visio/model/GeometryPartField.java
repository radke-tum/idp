package de.iteratec.visio.model;

public interface GeometryPartField extends Comparable<GeometryPartField> {

  /**
   * Returns the name of this field
   * @return the field name
   */
  String getName();

  /**
   * Sets the name of this field
   * @param name the name to set
   */
  void setName(String name);

  /**
   * Returns the value of this field as String
   * @return the value
   */
  String getValue();

  /**
   * Sets the value for this field
   * @param value the value to set
   */
  void setValue(String value);

  /**
   * Returns the formula of this field
   * @return the formula
   */
  String getFormula();

  /**
   * Sets the formula for this field
   * @param formula the formula to set
   */
  void setFormula(String formula);

  /**
   * Sets whether the formula should be ignored or not.
   * @param noFormula true if the absolute value should take priority
   */
  void setNoFormula(boolean noFormula);

  /**
   * @return Whether the formula should be ignored or not.
   */
  boolean isNoFormula();

  /**
   * Sets the unit attribute for this geometry part field
   * @param unit
   */
  void setUnit(String unit);

  /**
   * Returns the unit of this geometry part field
   * @return
   */
  String getUnit();

}
