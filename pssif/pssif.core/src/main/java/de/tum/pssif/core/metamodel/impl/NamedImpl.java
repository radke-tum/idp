package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Named;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class NamedImpl implements Named {

  private final String name;

  public NamedImpl(String name) {
    this.name = name;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Named)) {
      return false;
    }
    return PSSIFUtil.areSame(getName(), ((Named) obj).getName()) && getMetaType().equals(((Named) obj).getMetaType());
  }

  @Override
  public int hashCode() {
    return getMetaType().hashCode() ^ getName().hashCode();
  }

  @Override
  public abstract Class<?> getMetaType();

}
