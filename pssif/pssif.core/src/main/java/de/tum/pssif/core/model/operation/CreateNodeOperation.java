package de.tum.pssif.core.model.operation;

import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;


public class CreateNodeOperation {

  private final NodeTypeImpl nodeType;

  public CreateNodeOperation(NodeTypeImpl nodeType) {
    this.nodeType = nodeType;
  }

  public NodeTypeImpl getNodeType() {
    return nodeType;
  }

}
