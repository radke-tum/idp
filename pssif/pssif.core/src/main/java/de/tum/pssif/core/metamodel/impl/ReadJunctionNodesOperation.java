package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Model;


public final class ReadJunctionNodesOperation {
  private final JunctionNodeType type;

  /*package*/ReadJunctionNodesOperation(JunctionNodeType type) {
    this.type = type;
  }

  public JunctionNodeType getType() {
    return type;
  }

  public PSSIFOption<JunctionNode> apply(Model model) {
    return model.apply(this);
  }
}
