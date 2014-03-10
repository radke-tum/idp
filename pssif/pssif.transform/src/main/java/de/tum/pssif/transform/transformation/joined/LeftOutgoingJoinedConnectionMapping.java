package de.tum.pssif.transform.transformation.joined;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class LeftOutgoingJoinedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping joinedMapping;
  private final ConnectionMapping targetMapping;

  public LeftOutgoingJoinedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to,
      ConnectionMapping joinedMapping) {
    super(baseMapping, type, from, to);
    this.joinedMapping = joinedMapping;
    this.targetMapping = type.getMapping(from, to).getOne();
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    //we expect exactly one node being connected to from via the joined mapping, otherwise we need to return PSSIFOption<Edge> with possibly none or many
    Node actualFromNode = joinedMapping.applyTo(joinedMapping.applyOutgoing(from).getOne());
    return targetMapping.create(model, actualFromNode, to);
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    Collection<Edge> result = Sets.newHashSet();
    for (Edge e : targetMapping.apply(model).getMany()) {
      Node from = targetMapping.applyFrom(e);
      Node to = targetMapping.applyTo(e);
      PSSIFOption<Edge> joined = joinedMapping.applyIncoming(from);
      if (joined.isOne()) {
        Node inner = joinedMapping.applyFrom(joined.getOne());
        result.add(new UnjoinedEdge(e, inner, to));
      }
      else {
        throw new PSSIFStructuralIntegrityException("ambiguous edges");
      }
    }
    return PSSIFOption.many(result);
  }

  @Override
  public Node applyFrom(Edge edge) {
    for (Edge candidate : targetMapping.apply(edge.getModel()).getMany()) {
      if (candidate.getId().equals(edge.getId())) {
        Node from = targetMapping.applyFrom(candidate);
        PSSIFOption<Edge> joined = joinedMapping.applyIncoming(from);
        if (joined.isOne()) {
          return joinedMapping.applyFrom(joined.getOne());
        }
        else {
          throw new PSSIFStructuralIntegrityException("ambiguous edges");
        }
      }
    }
    throw new PSSIFStructuralIntegrityException("edge not found");
  }

  @Override
  public PSSIFOption<Edge> applyOutgoing(Node node) {
    PSSIFOption<Edge> joined = joinedMapping.applyOutgoing(node);
    if (joined.isOne()) {
      return targetMapping.applyOutgoing(joinedMapping.applyTo(joined.getOne()));
    }
    else {
      throw new PSSIFStructuralIntegrityException("ambiguous edges");
    }
  }
}
