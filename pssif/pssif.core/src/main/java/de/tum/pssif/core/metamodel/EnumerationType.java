package de.tum.pssif.core.metamodel;

import java.util.Collection;


public interface EnumerationType extends DataType {

  Collection<EnumerationLiteral> getLiterals();

  EnumerationLiteral findLiteral(String name);

}
