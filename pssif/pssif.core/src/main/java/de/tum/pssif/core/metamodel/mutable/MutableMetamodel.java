package de.tum.pssif.core.metamodel.mutable;

import java.util.Collection;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Metamodel;


public interface MutableMetamodel extends Metamodel {
  MutableNodeType createNodeType(String name);

  MutableJunctionNodeType createJunctionNodeType(String name);

  MutableEdgeType createEdgeType(String name);

  Collection<MutableNodeType> getMutableNodeTypes();

  Collection<MutableEdgeType> getMutableEdgeTypes();

  PSSIFOption<MutableNodeType> getMutableNodeType(String name);

  PSSIFOption<MutableEdgeType> getMutableEdgeType(String name);
}
