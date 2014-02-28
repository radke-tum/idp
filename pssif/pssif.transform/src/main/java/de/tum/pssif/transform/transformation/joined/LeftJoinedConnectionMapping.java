package de.tum.pssif.transform.transformation.joined;

import java.util.Iterator;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class LeftJoinedConnectionMapping extends AbstractConnectionMapping {
  private final ConnectionMapping targetMapping;
  private final ConnectionMapping joinedMapping;

  public LeftJoinedConnectionMapping(EdgeType type, ConnectionMapping baseMapping, ConnectionMapping targetMapping, ConnectionMapping joinedMapping) {
    super(baseMapping.getFrom(), baseMapping.getTo());
    this.targetMapping = targetMapping;
    this.joinedMapping = joinedMapping;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    PSSIFOption<Edge> froms = joinedMapping.getFrom().apply(from);
    Iterator<Edge> it = froms.getMany().iterator();
    Edge result = null;
    if (it.hasNext()) {
      Iterator<Node> fromIt = joinedMapping.getTo().apply(it.next()).getMany().iterator();
      if (fromIt.hasNext()) {
        result = targetMapping.create(model, fromIt.next(), to);
      }
      while (fromIt.hasNext()) {
        targetMapping.connectFrom(result, fromIt.next());
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
