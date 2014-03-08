package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.common.PSSIFOption;


public interface Metamodel {
  Collection<Enumeration> getEnumerations();

  Collection<NodeTypeBase> getNodeTypes();

  Collection<EdgeType> getEdgeTypes();

  PSSIFOption<Enumeration> getEnumeration(String name);

  Collection<DataType> getDataTypes();

  PSSIFOption<DataType> getDataType(String name);

  Collection<PrimitiveDataType> getPrimitiveTypes();

  PSSIFOption<PrimitiveDataType> getPrimitiveType(String name);

  PSSIFOption<NodeTypeBase> getNodeType(String name);

  PSSIFOption<EdgeType> getEdgeType(String name);
}
