package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.impl.CreateEdgeOperation;
import de.tum.pssif.core.metamodel.impl.CreateNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadNodesOperation;
import de.tum.pssif.core.metamodel.impl.base.ReadEdgesOperation;
import de.tum.pssif.core.util.PSSIFOption;


/**
 * A PSS-IF Model, an (ontological) instance of a PSS-IF Metamodel.
 */
public interface Model {

  /**
   * Creates a node.
   * @param op
   *    The node create operation supplied by the NodeType.
   * @return
   *    The created node.
   */
  Node apply(CreateNodeOperation op);

  /**
   * Creates an edge.
   * @param op
   *    The edge create operation supplied by the Connection Mapping of an Edge Type.
   * @return
   *    The created edge.
   */
  Edge apply(CreateEdgeOperation op);

  /**
   * Retrieves a collection of nodes.
   * @param op
   *    The node read operation supplied by a Node Type.
   * @return
   *    A PSS-IF Option containing the nodes.
   */
  PSSIFOption<Node> apply(ReadNodesOperation op);

  /**
   * Retrieves a collection of nodes.
   * @param op
   *    The node read operation supplied by a Node Type.
   * @return
   *    A PSS-IF Option containing the nodes.
   */
  PSSIFOption<Edge> apply(ReadEdgesOperation op);
}
