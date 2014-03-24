package de.tum.pssif.transform.transformation.artificial;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;


public class ArtificializedConnectionMapping extends ViewedConnectionMapping {
  private final ConnectionMapping sourceMapping;

  public ArtificializedConnectionMapping(ConnectionMapping baseMapping, EdgeType type, NodeTypeBase from, NodeTypeBase to, EdgeType sourceType) {
    super(baseMapping, type, from, to);
    sourceMapping = sourceType.getMapping(from, to).getOne();
  }

  @Override
  public PSSIFOption<Edge> apply(Model model, boolean includeSubtypes) {
    Collection<Edge> result = Sets.newHashSet();
    candidate: for (Edge candidate : getBaseMapping().apply(model, includeSubtypes).getMany()) {
      Node from = getBaseMapping().applyFrom(candidate);
      Node to = getBaseMapping().applyTo(candidate);
      for (Edge edge : sourceMapping.applyOutgoing(from, includeSubtypes).getMany()) {
        if (sourceMapping.applyTo(edge).equals(to)) {
          continue candidate;
        }
      }
      result.add(candidate);
    }
    return PSSIFOption.many(result);
  }

  @Override
  public PSSIFOption<Edge> applyIncoming(Node node, boolean includeSubtypes) {
    Collection<Edge> result = Sets.newHashSet();
    candidate: for (Edge candidate : getBaseMapping().applyIncoming(node, includeSubtypes).getMany()) {
      Node from = getBaseMapping().applyFrom(candidate);
      for (Edge edge : sourceMapping.applyOutgoing(from, includeSubtypes).getMany()) {
        if (sourceMapping.applyTo(edge).equals(node)) {
          continue candidate;
        }
      }
      result.add(candidate);
    }
    return PSSIFOption.many(result);
  }

  @Override
  public PSSIFOption<Edge> applyOutgoing(Node node, boolean includeSubtypes) {
    Collection<Edge> result = Sets.newHashSet();
    candidate: for (Edge candidate : getBaseMapping().applyOutgoing(node, includeSubtypes).getMany()) {
      Node to = getBaseMapping().applyTo(candidate);
      for (Edge edge : sourceMapping.applyIncoming(to, includeSubtypes).getMany()) {
        if (sourceMapping.applyFrom(edge).equals(node)) {
          continue candidate;
        }
      }
      result.add(candidate);
    }
    return PSSIFOption.many(result);
  }
}
