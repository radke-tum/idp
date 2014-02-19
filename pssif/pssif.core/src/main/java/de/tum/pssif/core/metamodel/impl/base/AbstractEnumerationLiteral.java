package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.EnumerationLiteral;


public abstract class AbstractEnumerationLiteral extends AbstractNamed implements EnumerationLiteral {
  private final AbstractEnumeration owner;

  public AbstractEnumerationLiteral(AbstractEnumeration owner, String name) {
    super(name);
    this.owner = owner;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof EnumerationLiteral)) {
      return false;
    }
    return super.equals(obj) && getOwner().equals(((EnumerationLiteral) obj).getOwner());
  }

  @Override
  public final int hashCode() {
    return getMetaType().hashCode() ^ (owner.getName() + getName()).hashCode();
  }

  @Override
  public final Enumeration getOwner() {
    return owner;
  }

  @Override
  public final Class<?> getMetaType() {
    return EnumerationLiteral.class;
  }

  @Override
  public final String toString() {
    return "EnumerationLiteral:" + this.getName();
  }
}
