package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public final class CreateNodeOperation {
  private final NodeType type;

  /*package*/CreateNodeOperation(NodeType type) {
    this.type = type;
  }

  public NodeType getType() {
    return type;
  }

  public Node apply(Model model) {
    return model.apply(this);
  }
}
