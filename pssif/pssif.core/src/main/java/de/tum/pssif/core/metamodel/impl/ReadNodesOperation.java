package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public final class ReadNodesOperation {
  private final NodeType type;

  /*package*/ReadNodesOperation(NodeType type) {
    this.type = type;
  }

  public NodeType getType() {
    return type;
  }

  public PSSIFOption<Node> apply(Model model) {
    return model.apply(this);
  }
}
