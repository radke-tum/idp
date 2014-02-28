package de.tum.pssif.transform.transformation.artificial;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class ArtificializedNodeType extends ViewedNodeType {
  private final NodeType sourceType;
  private final EdgeType edgeType;
  private final NodeType targetType;

  public ArtificializedNodeType(NodeType sourceType, EdgeType edgeType, NodeType targetType) {
    super(targetType);
    this.sourceType = sourceType;
    this.edgeType = edgeType;
    this.targetType = targetType;
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubTypes) {
    Set<Node> sources = sourceType.apply(model, true).getMany();
    Set<Node> result = Sets.newHashSet(targetType.apply(model, includeSubTypes).getMany());

    ConnectionMapping mapping = edgeType.getMapping(sourceType, targetType);

    Iterator<Node> it = result.iterator();
    while (it.hasNext()) {
      Node current = it.next();
      Set<Edge> edges = mapping.getTo().apply(current).getMany();

      edges: for (Edge e : edges) {
        for (Node n : mapping.getFrom().apply(e).getMany()) {
          if (sources.contains(n)) {
            it.remove();
            break edges;
          }
        }
      }
    }

    return PSSIFOption.many(result);
  }
}
