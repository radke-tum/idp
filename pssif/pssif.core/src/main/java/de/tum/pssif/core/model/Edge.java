package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.util.PSSIFOption;


/**
 * An edge in a PSS-IF Model and an (ontological) instance of an
 * Edge Type.
 */
public interface Edge extends Element {
  /**
   * Retrieves nodes connected to this edge.
   * @param op
   *    The read connected operation, supplied by an edge end.
   * @return
   *    A PSS-IF Option holding the connected edges.
   */
  PSSIFOption<Node> apply(ReadConnectedOperation op);

  /**
   * Applies a connect operation to this edge.
   * @param op
   *    The connect operation, supplied by a connection mapping.
   */
  void apply(ConnectOperation op);

  /**
   * Performs a disconnect operation.
   * @param op
   *    The disconnect operation, supplied by a connection mapping.
   */
  void apply(DisconnectOperation op);

}
