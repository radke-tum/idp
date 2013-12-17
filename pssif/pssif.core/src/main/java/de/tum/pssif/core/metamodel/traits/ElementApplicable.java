package de.tum.pssif.core.metamodel.traits;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


/**
 *  A concept for the applicability of
 *  model elements. 
 */
public interface ElementApplicable {

  /**
   * Retrieves the edges associated with the
   * provided Node in the context of this element type (metamodel).
   * @param node
   *    The Node whose related Edges to fetch.
   * @return
   *    The related Edges in the context of this element type (metamodel).
   */
  PSSIFOption<Edge> apply(Node node);

  /**
   * Retrieves the Nodes associated with the provided Edge
   * in the context of this element type (metamodel).
   * @param edge
   *    The Edge whose related Nodes to fetch.
   * @return
   *    The related Nodes in the context of this element type (metamodel).
   */
  PSSIFOption<Node> apply(Edge edge);
}
