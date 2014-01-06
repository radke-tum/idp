package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;
import de.tum.pssif.core.util.PSSIFValue;


/**
 * Common super-type for nodes and edges.
 */
public interface Element {
  void apply(SetValueOperation op);

  PSSIFValue apply(GetValueOperation op);
}
