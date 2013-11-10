package de.tum.pssif.core.metamodel.constraints;

import de.tum.pssif.core.metamodel.NodeType;


public class BaseNodeTypeMapping implements NodeTypeMapping {
  private Constraint from;
  private Constraint to;
  private Constraint aux;

  @Override
  public boolean satisfies(NodeType from, NodeType to) {
    return this.from.satisfies(from) && this.to.satisfies(to);
  }

  @Override
  public boolean satisfies(NodeType type) {
    return aux.satisfies(type);
  }

}
