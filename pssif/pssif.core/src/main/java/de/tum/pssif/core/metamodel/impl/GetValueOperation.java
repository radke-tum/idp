package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFValue;


public class GetValueOperation {
  private final Attribute type;

  /*package*/public GetValueOperation(Attribute type) {
    this.type = type;
  }

  public Attribute getAttributeType() {
    return type;
  }

  public PSSIFValue apply(Element element) {
    return element.apply(this);
  }
}
