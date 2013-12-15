package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.util.PSSIFOption;


public interface Edge extends Element {
  void apply(ConnectOperation op);

  void apply(DisconnectOperation op);

  PSSIFOption<Node> apply(ReadConnectedOperation op);
}
