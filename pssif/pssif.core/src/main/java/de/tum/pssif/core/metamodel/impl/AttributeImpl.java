package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


public class AttributeImpl extends NamedImpl implements Attribute {
  private final DataType type;
  private final Unit     unit;

  public AttributeImpl(String name, DataType type, Unit unit) {
    super(name);
    this.type = type;
    this.unit = unit;
  }
}
