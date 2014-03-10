package de.tum.pssif.transform.transformation.joined;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFIllegalAccessException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class RightOutgoingJoinedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping joinedMapping;
  private final ConnectionMapping targetMapping;

  public RightOutgoingJoinedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to,
      ConnectionMapping joinedMapping) {
    super(baseMapping, type, from, to);
    this.joinedMapping = joinedMapping;
    this.targetMapping = type.getMapping(from, to).getOne();
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    //we expect exactly one node being connected to from via the joined mapping, otherwise we need to return PSSIFOption<Edge> with possibly none or many
    Node actualToNode = joinedMapping.applyTo(joinedMapping.applyOutgoing(to).getOne());
    return targetMapping.create(model, from, actualToNode);
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    Collection<Edge> result = Sets.newHashSet();
    for (Edge e : targetMapping.apply(model).getMany()) {
      Node from = targetMapping.applyFrom(e);
      Node to = targetMapping.applyTo(e);
      PSSIFOption<Edge> joined = joinedMapping.applyIncoming(to);
      if (joined.isOne()) {
        Node inner = joinedMapping.applyFrom(joined.getOne());
        result.add(new UnjoinedEdge(e, from, inner));
      }
      else {
        throw new PSSIFIllegalAccessException("ambiguous edges");
      }
    }
    return PSSIFOption.many(result);
  }

  @Override
  public Node applyFrom(Edge edge) {
    // TODO Auto-generated method stub
    return super.applyFrom(edge);
  }

  @Override
  public Node applyTo(Edge edge) {
    // TODO Auto-generated method stub
    return super.applyTo(edge);
  }

  @Override
  public PSSIFOption<Edge> applyIncoming(Node node) {
    // TODO Auto-generated method stub
    return super.applyIncoming(node);
  }

  @Override
  public PSSIFOption<Edge> applyOutgoing(Node node) {
    // TODO Auto-generated method stub
    return super.applyOutgoing(node);
  }
}
