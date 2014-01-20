package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.impl.base.AbstractAttributeGroup;
import de.tum.pssif.core.metamodel.impl.base.AbstractElementType;


public class AttributeGroupImpl extends AbstractAttributeGroup {
  public AttributeGroupImpl(String name, AbstractElementType<?> owner) {
    super(name, owner);
  }
}
