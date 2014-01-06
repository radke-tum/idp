package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.AttributeType;


public class GetValueOperation {
  private final AttributeType type;

  /*package*/public GetValueOperation(AttributeType type) {
    this.type = type;
  }

  public AttributeType getAttributeType() {
    return type;
  }
}
