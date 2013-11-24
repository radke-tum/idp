package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public interface EdgeEnd extends Named, Multiplicity {
  Collection<NodeType> getTypes();

  EdgeType getType();

  PSSIFOption<Node> nodes(PSSIFOption<Edge> edges);

  PSSIFOption<Edge> edges(PSSIFOption<Node> nodes);

  boolean equals(String name, NodeType type);
}
