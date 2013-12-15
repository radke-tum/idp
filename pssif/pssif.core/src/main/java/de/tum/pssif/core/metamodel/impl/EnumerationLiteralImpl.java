package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.EnumerationLiteral;


public class EnumerationLiteralImpl extends NamedImpl implements EnumerationLiteral {

  private final EnumerationImpl owner;

  public EnumerationLiteralImpl(EnumerationImpl owner, String name) {
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
  public Enumeration getOwner() {
    return owner;
  }

  @Override
  public Class<?> getMetaType() {
    return EnumerationLiteral.class;
  }

}
