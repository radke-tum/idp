package de.tum.pssif.core.metamodel.constraints;

import de.tum.pssif.core.metamodel.NodeType;


public interface NodeTypeMapping {
  boolean satisfies(NodeType from, NodeType to);

  boolean satisfies(NodeType type);
}
