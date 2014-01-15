package de.tum.pssif.transform.metamodel.alternatives;

import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;


public interface View extends Metamodel {

  void add(ElementType<?> elementType);

  void replace(ElementType<?> toReplace, ElementType<?> replaceWith);

  void remove(ElementType<?> elementType);

}
