package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractEdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public final class EdgeEndImpl extends AbstractEdgeEnd {
  public EdgeEndImpl(String name, EdgeType edge, Multiplicity multiplicity, NodeType type) {
    super(name, edge, multiplicity, type);
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    return new ReadConnectedOperation(this).apply(node);
  }

  @Override
  public PSSIFOption<Node> apply(Edge edge) {
    return new ReadConnectedOperation(this).apply(edge);
  }
}
