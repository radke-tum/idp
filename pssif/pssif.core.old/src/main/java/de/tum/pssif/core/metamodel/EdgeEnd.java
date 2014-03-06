package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.traits.ElementApplicable;


/**
 * An association between an edge type and a node type.
 */
public interface EdgeEnd extends Named, Multiplicity, ElementApplicable {

  /**
   * @return
   *    The node type of the association.
   */
  NodeType getNodeType();

  /**
   * @return
   *    The edge type of the association.
   */
  EdgeType getEdgeType();
}
