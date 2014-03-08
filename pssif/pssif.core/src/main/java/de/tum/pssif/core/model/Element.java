package de.tum.pssif.core.model;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;


public interface Element {
  void setId(String id);

  String getId();

  void apply(SetValueOperation op);

  PSSIFOption<PSSIFValue> apply(GetValueOperation op);
}
