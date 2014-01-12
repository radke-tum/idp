package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFValue;


public class SetValueOperation {
  private final Attribute type;
  private final PSSIFValue    value;

  /*package*/public SetValueOperation(Attribute type, PSSIFValue value) {
    this.type = type;
    this.value = value;
  }

  public Attribute getAttributeType() {
    return type;
  }

  public PSSIFValue getValue() {
    return value;
  }

  public void apply(Element element) {
    element.apply(this);
  }
}
