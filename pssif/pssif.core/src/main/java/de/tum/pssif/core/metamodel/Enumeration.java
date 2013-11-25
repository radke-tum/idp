package de.tum.pssif.core.metamodel;

public interface Enumeration extends DataType {
  EnumerationLiteral createLiteral(String name);

  void removeLiteral(EnumerationLiteral literal);
}
