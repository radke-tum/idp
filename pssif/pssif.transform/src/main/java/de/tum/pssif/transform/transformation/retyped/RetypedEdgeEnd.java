package de.tum.pssif.transform.transformation.retyped;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeEnd;


public class RetypedEdgeEnd extends ViewedEdgeEnd {
  private final EdgeEnd originalEnd;

  public RetypedEdgeEnd(EdgeEnd baseEnd, String name, EdgeType edge, Multiplicity multiplicity, NodeType type, EdgeEnd originalEnd) {
    super(baseEnd, name, edge, multiplicity, type);
    this.originalEnd = originalEnd;
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    Collection<Edge> result = Sets.newHashSet();

    PSSIFOption<Edge> baseResult = getBaseEnd().apply(node);
    for (Edge e : baseResult.getMany()) {
      PSSIFOption<Node> nodes = getBaseEnd().apply(e);
      for (Edge orig : originalEnd.apply(node).getMany()) {
        if (nodes.equals(originalEnd.apply(orig))) {
          result.add(e);
        }
      }
    }

    return PSSIFOption.many(result);
  }
}
