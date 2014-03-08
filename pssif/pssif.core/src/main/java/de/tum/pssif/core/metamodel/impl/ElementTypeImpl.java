package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.mutable.MutableElementType;


public abstract class ElementTypeImpl extends NamedImpl implements MutableElementType {

  public ElementTypeImpl(String name) {
    super(name);
  }
}
