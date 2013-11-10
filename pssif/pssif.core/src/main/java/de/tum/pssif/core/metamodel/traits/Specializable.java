package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ElementType;


public interface Specializable<T extends ElementType> {
  T getParent();

  Collection<T> getChildren();

  /**
   * Disinherit is (by convention) inherit with null!
   * @param other
   */
  void inherit(T other);
}
