package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


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
  public Edge create(Model model, Node from, Node to) {
    return new CreateEdgeOperation(this, from, to).apply(model);
  }

  @Override
  public void connectFrom(Edge edge, Node node) {
    if (!from.includesEdgeEnd(from.apply(edge).size() + 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new ConnectOperation(edge, from, node).apply();
  }

  @Override
  public void connectTo(Edge edge, Node node) {
    if (!to.includesEdgeEnd(to.apply(edge).size() + 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new ConnectOperation(edge, to, node).apply();
  }

  @Override
  public void disconnectFrom(Edge edge, Node node) {
    if (!from.includesEdgeEnd(from.apply(edge).size() - 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new DisconnectOperation(edge, from, node).apply();
  }

  @Override
  public void disconnectTo(Edge edge, Node node) {
    if (!to.includesEdgeEnd(to.apply(edge).size() - 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new DisconnectOperation(edge, to, node).apply();
  }
}
