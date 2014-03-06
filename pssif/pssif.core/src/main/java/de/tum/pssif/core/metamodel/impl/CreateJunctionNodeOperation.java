package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Model;


public final class CreateJunctionNodeOperation {
  private final JunctionNodeType type;

  /*package*/CreateJunctionNodeOperation(JunctionNodeType type) {
    this.type = type;
  }

  public JunctionNodeType getType() {
    return type;
  }

  public JunctionNode apply(Model model) {
    return model.apply(this);
  }
}
