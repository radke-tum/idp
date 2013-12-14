package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;


public class ConnectionMappingImpl implements ConnectionMapping {
  private final EdgeEnd from;
  private final EdgeEnd to;

  public ConnectionMappingImpl(EdgeEnd from, EdgeEnd to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public EdgeEnd getFrom() {
    return from;
  }

  @Override
  public EdgeEnd getTo() {
    return to;
  }

  @Override
  public Edge create(Model model) {
    return model.createEdge(this);
  }
}
