package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttribute;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class AttributeImpl extends AbstractAttribute {
  public AttributeImpl(String name, DataType type, Unit unit, boolean visible, AttributeCategory category) {
    super(name, type, unit, visible, category);
  }

  @Override
  public void set(Element element, PSSIFOption<PSSIFValue> value) {
    new SetValueOperation(this, value).apply(element);
  }

  @Override
  public PSSIFOption<PSSIFValue> get(Element element) {
    return new GetValueOperation(this).apply(element);
  }

}
