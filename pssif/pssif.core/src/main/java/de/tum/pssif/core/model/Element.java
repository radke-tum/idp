package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;


/**
 * Common super-type for nodes and edges.
 */
public interface Element {
  void setValue(SetValueOperation op);

  Object getValue(GetValueOperation op);
}
