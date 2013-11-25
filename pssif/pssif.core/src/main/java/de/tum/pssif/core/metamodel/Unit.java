package de.tum.pssif.core.metamodel;

public interface Unit extends Named {
  boolean isAllowedForDataType(DataType type);
}
