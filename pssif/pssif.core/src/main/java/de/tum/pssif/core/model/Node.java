package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.util.PSSIFOption;


/**
 * A node in a PSS-IF Model and an (ontological) instance of a
 * Node Type. 
 */
public interface Node extends Element {

  /**
   * Retrieves edges connected to this node.
   * @param op
   *    The read connected operation, supplied by an edge end.
   * @return
   *    A PSS-IF Option holding the connected edges.
   */
  PSSIFOption<Edge> apply(ReadConnectedOperation op);

  /**
   * Performs a connect operation on this node.
   * @param op
   *    The connect operation, supplied by a connection mapping.
   */
  void apply(ConnectOperation op);

  /**
   * Performs a disconnect operation on this node.
   * @param op
   *    The disconnect operation, supplied by a connection mapping.
   */
  void apply(DisconnectOperation op);
}
