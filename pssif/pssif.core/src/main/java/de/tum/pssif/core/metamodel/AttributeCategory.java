package de.tum.pssif.core.metamodel;

public enum AttributeCategory {

  MONETARY("Monetary"), WEIGHT("Weight"), DENSITY("Density"), TIME("Time"), GEOMETRY("Geometry"), METADATA("MetaData"), MATERIAL("Material");

  private final String name;

  private AttributeCategory(String name) {
    this.name = name;
  }

  public String toString() {
    return this.name;
  }

  public String getName() {
    return this.name;
  }

}
