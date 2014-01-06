package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.AttributeType;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFValue;


public class GetValueOperation {
  private final AttributeType type;

  /*package*/public GetValueOperation(AttributeType type) {
    this.type = type;
  }

  public AttributeType getAttributeType() {
    return type;
  }

  public PSSIFValue apply(Element element) {
    return element.apply(this);
  }
}
