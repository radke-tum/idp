package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.AttributeType;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFValue;


public class SetValueOperation {
  private final AttributeType type;
  private final PSSIFValue    value;

  /*package*/public SetValueOperation(AttributeType type, PSSIFValue value) {
    this.type = type;
    this.value = value;
  }

  public AttributeType getAttributeType() {
    return type;
  }

  public PSSIFValue getValue() {
    return value;
  }

  public void apply(Element element) {
    element.apply(this);
  }
}
