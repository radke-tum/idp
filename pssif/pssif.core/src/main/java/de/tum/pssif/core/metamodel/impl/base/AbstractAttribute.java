package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


public abstract class AbstractAttribute extends AbstractNamed implements Attribute {
  private final DataType          type;
  private final Unit              unit;
  private final boolean           visible;
  private final AttributeCategory category;

  public AbstractAttribute(String name, DataType type, Unit unit, boolean visible, AttributeCategory category) {
    super(name);
    this.type = type;
    this.unit = unit;
    this.visible = visible;
    this.category = category;
  }

  @Override
  public final DataType getType() {
    return type;
  }

  @Override
  public final Unit getUnit() {
    return unit;
  }

  @Override
  public final boolean isVisible() {
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
  public final Class<?> getMetaType() {
    return Attribute.class;
  }

  @Override
  public final AttributeCategory getCategory() {
    return this.category;
  }

  @Override
  public final String toString() {
    return "Attribute:" + this.getName();
  }

}
