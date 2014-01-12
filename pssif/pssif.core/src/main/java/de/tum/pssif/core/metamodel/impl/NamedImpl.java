package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Named;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class NamedImpl implements Named {

  private final String        name;
  private PSSIFOption<String> names;

  public NamedImpl(String name) {
    this.name = name;
    this.names = PSSIFOption.one(name);
  }

  @Override
  public final String getName() {
    return name;
  }

  protected void addName(String name) {
    names = PSSIFOption.merge(names, PSSIFOption.one(name));
  }

  public final boolean hasName(String name) {
    for (String alias : names.getMany()) {
      if (PSSIFUtil.areSame(name, alias)) {
        return true;
      }
    }
    return false;
  }

  public final PSSIFOption<String> getNames() {
    return names;
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

  public abstract Class<?> getMetaType();

}
