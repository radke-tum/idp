package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.Annotatable;
import de.tum.pssif.core.metamodel.traits.AttributeGroups;
import de.tum.pssif.core.metamodel.traits.Specializable;
import de.tum.pssif.core.model.Element;


/**
 * A type which describes the (graph) structure of a PSS-IF Metamodel.
 * Can be either a Node Type or an Edge Type.
 *
 * @param <T>
 */
public interface ElementType<T extends ElementType<T, E>, E extends Element> extends Named, AttributeGroups, Specializable<T>, Annotatable<E> {

  //Nothing specific here

}
