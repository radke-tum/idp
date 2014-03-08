package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.mutable.MutableElementType;


public abstract class ElementTypeImpl implements MutableElementType {
  private final String name;

  public ElementTypeImpl(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
