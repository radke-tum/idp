package de.tum.pssif.core.metamodel;

public interface Metamodel {
  NodeType create(String name);

  EdgeType create(String name, String inName, NodeType inType, Multiplicity inMult, String outName, NodeType outType, Multiplicity outMult);

  EdgeEnd createAuxiliaryEnd(EdgeType onType, String name, Multiplicity mult, NodeType to);

  NodeType findNodeType(String name);

  EdgeType findEdgeType(String name);
}
