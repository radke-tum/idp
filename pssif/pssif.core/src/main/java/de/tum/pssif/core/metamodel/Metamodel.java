package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.common.PSSIFOption;


public interface Metamodel {
  Collection<NodeTypeBase> getNodeTypes();

  Collection<EdgeType> getEdgeTypes();

  PSSIFOption<NodeTypeBase> getNodeType(String name);

  PSSIFOption<EdgeType> getEdgeType(String name);
}
