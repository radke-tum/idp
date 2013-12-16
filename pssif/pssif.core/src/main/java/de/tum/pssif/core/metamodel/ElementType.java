package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.AttributeGroups;
import de.tum.pssif.core.metamodel.traits.Specializable;


/**
 * A type which describes the (graph) structure of a PSS-IF Metamodel.
 * Can be either a Node Type or an Edge Type.
 *
 * @param <T>
 */
public interface ElementType<T extends ElementType<T>> extends Named, AttributeGroups, Specializable<T> {

  //Nothing specific here

}
