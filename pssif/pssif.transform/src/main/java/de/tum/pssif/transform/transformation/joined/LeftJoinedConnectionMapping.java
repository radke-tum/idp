package de.tum.pssif.transform.transformation.joined;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.transform.transformation.artificial.ArtificialEdge;


public class LeftJoinedConnectionMapping extends AbstractConnectionMapping {
  private final ConnectionMapping baseMapping;
  private final ConnectionMapping targetMapping;
  private final ConnectionMapping joinedMapping;

  public LeftJoinedConnectionMapping(EdgeType type, ConnectionMapping baseMapping, ConnectionMapping targetMapping, ConnectionMapping joinedMapping) {
    super(baseMapping.getFrom(), baseMapping.getTo());
    this.baseMapping = baseMapping;
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
    Set<Edge> result = Sets.newHashSet(targetMapping.apply(model).getMany());
    Set<Edge> artificial = Sets.newHashSet();
    Set<Node> joinedNodes = joinedMapping.getFrom().getNodeType().apply(model, true).getMany();

    Iterator<Edge> it = result.iterator();
    while (it.hasNext()) {
      Edge current = it.next();
      Collection<Node> froms = Sets.newHashSet();
      Collection<Node> tos = Sets.newHashSet(targetMapping.getTo().apply(current).getMany());
      for (Node f : targetMapping.getFrom().apply(current).getMany()) {
        for (Edge e : joinedMapping.getTo().apply(f).getMany()) {
          for (Node n : joinedMapping.getFrom().apply(e).getMany()) {
            if (joinedNodes.contains(n)) {
              it.remove();
              froms.add(n);
            }
          }
        }
      }

      artificial.add(new ArtificialEdge(current, baseMapping.getFrom(), froms, targetMapping.getTo(), tos));
    }

    result.addAll(artificial);

    return PSSIFOption.many(result);
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
