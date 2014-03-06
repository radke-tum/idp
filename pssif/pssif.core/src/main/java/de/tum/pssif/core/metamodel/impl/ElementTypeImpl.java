package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.mutable.MutableElementType;


public abstract class ElementTypeImpl<T extends ElementType<T>> implements MutableElementType<T> {
  private final String name;
  private T            general         = null;
  private final Set<T> specializations = Sets.newHashSet();

  public ElementTypeImpl(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isAssignableFrom(T type) {
    if (this.equals(type)) {
      return true;
    }
    else {
      for (T special : getSpecials()) {
        if (special.isAssignableFrom(type)) {
          return true;
        }
      }
    }

    return false;
  }

  //TODO get rid of the cast
  @SuppressWarnings("unchecked")
  @Override
  public void inherit(T general) {
    if (general.isAssignableFrom((T) this)) {
      throw new PSSIFStructuralIntegrityException("inheritance cycle detected");
    }
    if (this.general != null) {
      this.general.unregisterSpecialization((T) this);
    }
    this.general = general;
    this.general.registerSpecialization((T) this);
  }

  @Override
  public T getGeneral() {
    return general;
  }

  @Override
  public Collection<T> getSpecials() {
    return ImmutableSet.copyOf(specializations);
  }

  @Override
  public void registerSpecialization(T special) {
    specializations.add(special);
  }

  @Override
  public void unregisterSpecialization(T special) {
    specializations.remove(special);
  }
}
