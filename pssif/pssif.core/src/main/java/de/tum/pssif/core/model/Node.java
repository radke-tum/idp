package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.util.PSSIFOption;


public interface Node extends Element {
  PSSIFOption<Edge> apply(ReadConnectedOperation op);

  void apply(ConnectOperation op);

  void apply(DisconnectOperation op);
}
