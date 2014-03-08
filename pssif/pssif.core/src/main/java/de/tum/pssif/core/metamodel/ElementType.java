package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.Named;


public interface ElementType extends Named {
  boolean isAssignableFrom(ElementType type);
}
