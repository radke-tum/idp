package de.tum.pssif.core.metamodel;

import java.util.Collection;


public interface Metamodel {

  NodeType create(String name);

  EdgeType create(String name, String inName, NodeType inType, Multiplicity inMult, String outName, NodeType outType, Multiplicity outMult);

  EdgeEnd createAuxiliaryEnd(EdgeType onType, String name, Multiplicity mult, NodeType to);

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
