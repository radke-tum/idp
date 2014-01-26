package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


/**
 * Common super-type for nodes and edges.
 */
public interface Element {

  String getId();

  void apply(SetValueOperation op);

  PSSIFOption<PSSIFValue> apply(GetValueOperation op);

}
