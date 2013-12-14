package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.AttributeGroups;
import de.tum.pssif.core.metamodel.traits.Specializable;


public interface ElementType<T extends ElementType<T>> extends Named, AttributeGroups, Specializable<T> {
}
