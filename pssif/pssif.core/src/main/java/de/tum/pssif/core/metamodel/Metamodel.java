package de.tum.pssif.core.metamodel;

import java.util.Collection;


public interface Metamodel {

  NodeType createNodeType(String name);

  EdgeType createEdgeType(String name);

  NodeType findNodeType(String name);

  EdgeType findEdgeType(String name);

  Collection<NodeType> getNodeTypes();

  Collection<EdgeType> getEdgeTypes();

  Enumeration createEnumeration(String name);

  Collection<Enumeration> getEnumerations();

  Enumeration findEnumeration(String name);

  void removeEnumeration(Enumeration enumeration);

  Unit findUnit(String name);

  Collection<Unit> getUnits();

  DataType findDataType(String name);

  Collection<DataType> getDataTypes();

  Collection<PrimitiveDataType> getPrimitiveTypes();

  PrimitiveDataType findPrimitiveType(String name);

}
