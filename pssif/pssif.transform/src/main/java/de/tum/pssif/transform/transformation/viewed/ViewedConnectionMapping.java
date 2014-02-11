package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ViewedConnectionMapping extends AbstractConnectionMapping {
  private final ConnectionMapping baseMapping;

  public ViewedConnectionMapping(ConnectionMapping baseMapping, EdgeEnd from, EdgeEnd to) {
    super(from, to);
    this.baseMapping = baseMapping;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    return baseMapping.create(model, from, to);
  }

  @Override
  public void connectFrom(Edge edge, Node node) {
    baseMapping.connectFrom(edge, node);
  }

  @Override
  public void connectTo(Edge edge, Node node) {
    baseMapping.connectTo(edge, node);
  }

  @Override
  public void disconnectFrom(Edge edge, Node node) {
    baseMapping.disconnectFrom(edge, node);
  }

  @Override
  public void disconnectTo(Edge edge, Node node) {
    baseMapping.disconnectTo(edge, node);
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    return baseMapping.apply(model);
  }

  protected ConnectionMapping getBaseMapping() {
    return baseMapping;
  }
}
