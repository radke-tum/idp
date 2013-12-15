package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.ElementApplicable;


public interface EdgeEnd extends Named, Multiplicity, ElementApplicable {
  NodeType getNodeType();

  EdgeType getEdgeType();
}
