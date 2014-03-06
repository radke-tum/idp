package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Model;


public final class ReadJunctionNodeOperation {
  private final JunctionNodeType type;
  private final String           id;

  /*package*/ReadJunctionNodeOperation(JunctionNodeType type, String id) {
    this.type = type;
    this.id = id;
  }

  public JunctionNodeType getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public PSSIFOption<JunctionNode> apply(Model model) {
    return model.apply(this);
  }

}
