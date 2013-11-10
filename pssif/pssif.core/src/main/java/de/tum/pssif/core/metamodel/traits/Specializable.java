package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ElementType;


public interface Specializable<T extends ElementType> {

  /**
   * Not null, any T if neccessary.
   * @return
   */
  T getGeneralization();

  /**
   * Not null, can be empty.
   * @return
   */
  Collection<T> getSpecializations();

  /**
   * this.inherit means this is the child.
   * Disinherit is (by convention) inherit with null!
   * @param other
   */
  void inherit(T other);
}
