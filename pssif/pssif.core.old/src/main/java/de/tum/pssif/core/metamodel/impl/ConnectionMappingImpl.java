package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ConnectionMappingImpl extends AbstractConnectionMapping {
  public ConnectionMappingImpl(EdgeEnd from, EdgeEnd to) {
    super(from, to);
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    return new CreateEdgeOperation(this, from, to).apply(model);
  }

  @Override
  public void connectFrom(Edge edge, Node node) {
    EdgeEnd from = getFrom();
    if (!from.includesEdgeEnd(from.apply(edge).size() + 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new ConnectOperation(edge, from, node).apply();
  }

  @Override
  public void connectTo(Edge edge, Node node) {
    EdgeEnd to = getTo();
    if (!to.includesEdgeEnd(to.apply(edge).size() + 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new ConnectOperation(edge, to, node).apply();
  }

  @Override
  public void disconnectFrom(Edge edge, Node node) {
    EdgeEnd from = getFrom();
    if (!from.includesEdgeEnd(from.apply(edge).size() - 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new DisconnectOperation(edge, from, node).apply();
  }

  @Override
  public void disconnectTo(Edge edge, Node node) {
    EdgeEnd to = getTo();
    if (!to.includesEdgeEnd(to.apply(edge).size() - 1)) {
      throw new PSSIFStructuralIntegrityException("multiplicity constraint violation");
    }
    new DisconnectOperation(edge, to, node).apply();
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    return model.apply(new ReadEdgesOperation(this));
  }
}
