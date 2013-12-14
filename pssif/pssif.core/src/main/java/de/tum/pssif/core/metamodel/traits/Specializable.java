package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;


public interface Specializable<T> {
  T getGeneral();

  Collection<T> getSpecials();

  void inherit(T general);

  void registerSpecialization(T special);
}
