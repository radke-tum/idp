package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.AttributeType;


public class SetValueOperation {
  private final AttributeType type;
  private final Object        value;

  /*package*/public SetValueOperation(AttributeType type, Object value) {
    this.type = type;
    this.value = value;
  }

  public AttributeType getAttributeType() {
    return type;
  }

  public Object getValue() {
    return value;
  }
}
