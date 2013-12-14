package de.tum.pssif.core.metamodel;

public interface Attribute extends Named {

  DataType getType();

  Unit getUnit();

  boolean isVisible();

}
