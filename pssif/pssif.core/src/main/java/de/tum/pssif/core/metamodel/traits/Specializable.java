package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;


/**
 * Defines operations allowed in connection with structural
 * specialization between element types.
 *
 * @param <T>
 *      The concrete element type.
 */
public interface Specializable<T> {

  /**
   * @return
   *    The generalization of the context element type. May be <b>null</b>.
   */
  T getGeneral();

  /**
   * @return
   *    The specializations of the context element type. May be empty, but not <b>null</b>.
   */
  Collection<T> getSpecials();

  /**
   * Sets the context element as a specialization of the provided general element.
   * @param general
   *    The general element, of which the context element becomes a specialization.
   */
  void inherit(T general);

  /**
   * Registers a specialization with its generalizaton.
   * <b>USED INTERNALLY ONLY!</b> TODO get rid of it.
   * @param special
   */
  void registerSpecialization(T special);
}
