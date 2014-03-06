package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public final class ReadConnectedOperation {
  private final EdgeEnd end;

  /*package*/ReadConnectedOperation(EdgeEnd end) {
    this.end = end;
  }

  public EdgeEnd getEnd() {
    return end;
  }

  public PSSIFOption<Edge> apply(Node node) {
    return node.apply(this);
  }

  public PSSIFOption<Node> apply(Edge edge) {
    return edge.apply(this);
  }
}
