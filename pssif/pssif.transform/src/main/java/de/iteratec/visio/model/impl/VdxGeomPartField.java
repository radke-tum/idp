package de.iteratec.visio.model.impl;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import de.iteratec.visio.model.GeometryPartField;


public class VdxGeomPartField implements GeometryPartField {

  private static final List<String>                VALID_NAMES         = ImmutableList.of("X", "Y", "A", "B", "C", "D", "E");

  private static final Ordering<GeometryPartField> GEOM_FIELD_ORDERING = Ordering.explicit(VALID_NAMES).onResultOf(
                                                                           new Function<GeometryPartField, String>() {
                                                                             @Override
                                                                             public String apply(GeometryPartField input) {
                                                                               return input.getName();
                                                                             }
                                                                           });
  private String                                   name;
  private String                                   value;
  private String                                   formula;
  private String                                   unit;
  private boolean                                  noFormula;

  public VdxGeomPartField(String name) {
    assert (VALID_NAMES.contains(name));
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    assert (VALID_NAMES.contains(name));
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  @Override
  public void setNoFormula(boolean noFormula) {
    this.noFormula = noFormula;
  }

  @Override
  public boolean isNoFormula() {
    return noFormula;
  }

  @Override
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public String getUnit() {
    return this.unit;
  }

  @Override
  public int compareTo(GeometryPartField o) {
    return GEOM_FIELD_ORDERING.compare(this, o);
  }

}
