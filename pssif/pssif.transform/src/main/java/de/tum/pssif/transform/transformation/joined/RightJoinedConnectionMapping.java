package de.tum.pssif.transform.transformation.joined;

import java.util.Iterator;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class RightJoinedConnectionMapping extends AbstractConnectionMapping {
  private final ConnectionMapping targetMapping;
  private final ConnectionMapping joinedMapping;

  public RightJoinedConnectionMapping(EdgeType type, ConnectionMapping baseMapping, ConnectionMapping targetMapping, ConnectionMapping joinedMapping) {
    super(baseMapping.getFrom(), baseMapping.getTo());
    this.targetMapping = targetMapping;
    this.joinedMapping = joinedMapping;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    PSSIFOption<Edge> tos = joinedMapping.getFrom().apply(to);
    Iterator<Edge> it = tos.getMany().iterator();
    Edge result = null;
    if (it.hasNext()) {
      Iterator<Node> toIt = joinedMapping.getTo().apply(it.next()).getMany().iterator();
      if (toIt.hasNext()) {
        result = targetMapping.create(model, from, toIt.next());
      }
      while (toIt.hasNext()) {
        targetMapping.connectTo(result, toIt.next());
      }
    }
    return result;
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    return targetMapping.apply(model);
  }

  @Override
  public void connectFrom(Edge edge, Node node) {
    targetMapping.connectFrom(edge, node);
  }

  @Override
  public void connectTo(Edge edge, Node node) {
    targetMapping.connectTo(edge, node);
  }

  @Override
  public void disconnectFrom(Edge edge, Node node) {
    targetMapping.disconnectFrom(edge, node);
  }

  @Override
  public void disconnectTo(Edge edge, Node node) {
    targetMapping.disconnectTo(edge, node);
  }

}
