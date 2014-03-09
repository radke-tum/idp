package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.impl.ConnectionMappingImpl;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public class ViewedConnectionMapping extends ConnectionMappingImpl {
  private final ConnectionMapping baseMapping;

  public ViewedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to) {
    super(type, from, to);
    this.baseMapping = baseMapping;
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    return baseMapping.apply(model);
  }

  @Override
  public Node applyFrom(Edge edge) {
    return baseMapping.applyFrom(edge);
  }

  @Override
  public Node applyTo(Edge edge) {
    return baseMapping.applyTo(edge);
  }

  @Override
  public PSSIFOption<Edge> applyIncoming(Node node) {
    return baseMapping.applyIncoming(node);
  }

  @Override
  public PSSIFOption<Edge> applyOutgoing(Node node) {
    return baseMapping.applyOutgoing(node);
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    return baseMapping.create(model, from, to);
  }
}
