package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;


public final class ConnectOperation {
  private final Edge    edge;
  private final EdgeEnd end;
  private final Node    node;

  /*package*/ConnectOperation(Edge edge, EdgeEnd end, Node node) {
    this.edge = edge;
    this.end = end;
    this.node = node;
  }

  public Edge getEdge() {
    return edge;
  }

  public EdgeEnd getEnd() {
    return end;
  }

  public Node getNode() {
    return node;
  }

  public void apply() {
    edge.apply(this);
    node.apply(this);
  }
}
