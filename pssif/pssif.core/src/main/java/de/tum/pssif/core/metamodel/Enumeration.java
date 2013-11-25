package de.tum.pssif.core.metamodel;

import java.util.Collection;

public interface Enumeration extends DataType {

  EnumerationLiteral createLiteral(String name);

  void removeLiteral(EnumerationLiteral literal);

  Collection<EnumerationLiteral> getLiterals();

  EnumerationLiteral findLiteral(String name);

}
