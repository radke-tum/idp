package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public final class ReadNodeOperation {
  private final NodeType type;
  private final String   id;

  /*package*/ReadNodeOperation(NodeType type, String id) {
    this.type = type;
    this.id = id;
  }

  public NodeType getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public PSSIFOption<Node> apply(Model model) {
    return model.apply(this);
  }

}
