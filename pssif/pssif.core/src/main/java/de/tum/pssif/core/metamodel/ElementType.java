package de.tum.pssif.core.metamodel;

public interface ElementType {
  String getName();

  boolean isAssignableFrom(ElementType type);
}
