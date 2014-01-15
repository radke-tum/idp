package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class AttributeImpl extends NamedImpl implements Attribute {

  private final DataType          type;
  private final Unit              unit;
  private final boolean           visible;
  private final AttributeCategory category;

  public AttributeImpl(String name, DataType type, Unit unit, boolean visible, AttributeCategory category) {
    super(name);
    this.type = type;
    this.unit = unit;
    this.visible = visible;
    this.category = category;
  }

  @Override
  public DataType getType() {
    return type;
  }

  @Override
  public Unit getUnit() {
    return unit;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Attribute)) {
      return false;
    }
    return super.equals(obj) && getType().equals(((Attribute) obj).getType());
  }

  @Override
  public final int hashCode() {
    return getMetaType().hashCode() ^ (type.getName() + getName()).hashCode();
  }

  @Override
  public Class<?> getMetaType() {
    return Attribute.class;
  }

  @Override
  public AttributeCategory getCategory() {
    return this.category;
  }

  @Override
  public String toString() {
    return "Attribute:" + this.getName();
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
