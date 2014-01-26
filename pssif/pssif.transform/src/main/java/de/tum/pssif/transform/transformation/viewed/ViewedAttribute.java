package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.impl.base.AbstractAttribute;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class ViewedAttribute extends AbstractAttribute {
  private final Attribute baseAttribute;

  public ViewedAttribute(Attribute baseAttribute, String name, DataType type, Unit unit, boolean visible, AttributeCategory category) {
    super(name, type, unit, visible, category);
    this.baseAttribute = baseAttribute;
  }

  @Override
  public void set(Element element, PSSIFOption<PSSIFValue> value) {
    baseAttribute.set(element, value);
  }

  @Override
  public PSSIFOption<PSSIFValue> get(Element element) {
    return baseAttribute.get(element);
  }

}
