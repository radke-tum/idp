package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractEdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ViewedEdgeEnd extends AbstractEdgeEnd {
  private final EdgeEnd baseEnd;

  public ViewedEdgeEnd(EdgeEnd baseEnd, String name, EdgeType edge, Multiplicity multiplicity, NodeType type) {
    super(name, edge, multiplicity, type);
    this.baseEnd = baseEnd;
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    return baseEnd.apply(node);
  }

  @Override
  public PSSIFOption<Node> apply(Edge edge) {
    return baseEnd.apply(edge);
  }

  protected EdgeEnd getBaseEnd() {
    return baseEnd;
  }
}
