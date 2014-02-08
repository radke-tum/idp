package de.tum.pssif.transform.transformation.nodified;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.impl.base.AbstractEdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ToNodifiedEdgeEnd extends AbstractEdgeEnd {
  private final ConnectionMapping baseMapping;
  private final NodifiedAttribute attribute;

  public ToNodifiedEdgeEnd(ConnectionMapping baseMapping, NodifiedAttribute attribute) {
    super(baseMapping.getTo().getName(), baseMapping.getTo().getEdgeType(), MultiplicityContainer.of(baseMapping.getTo().getEdgeEndLower(),
        baseMapping.getTo().getEdgeEndUpper(), baseMapping.getTo().getEdgeTypeLower(), baseMapping.getTo().getEdgeTypeUpper()), baseMapping.getTo()
        .getNodeType());
    this.baseMapping = baseMapping;
    this.attribute = attribute;
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    Collection<Edge> result = Sets.newHashSet();

    for (Edge e : baseMapping.getTo().apply(node).getMany()) {
      for (Node fromNode : baseMapping.getFrom().apply(e).getMany()) {
        for (Node toNode : baseMapping.getTo().apply(e).getMany()) {
          //TODO filter here
          if (attribute.get(fromNode).getOne().getValue().equals(toNode.getId())) {
            System.out.println("EE: Edge(" + e.getId() + ") -> Node(" + toNode.getId() + ")");
          }
          else {
            System.out.println("real edge, keep it");
            result.add(e);
          }
        }
      }
    }

    return PSSIFOption.many(result);
  }

  @Override
  public PSSIFOption<Node> apply(Edge edge) {
    Collection<Node> result = Sets.newHashSet();

    //TODO filtering needs to be enhanced here as there may be partially artificial edges?
    for (Node fromNode : baseMapping.getFrom().apply(edge).getMany()) {
      for (Node toNode : baseMapping.getTo().apply(edge).getMany()) {
        //TODO only add to result if not to artificial nodified instance
        if (attribute.get(fromNode).getOne().getValue().equals(toNode.getId())) {
          System.out.println("EE: Edge(" + edge.getId() + ") -> Node(" + toNode.getId() + ")");
        }
        else {
          System.out.println("real edge, keep the node");
          result.add(toNode);
        }
      }
    }

    return PSSIFOption.many(result);
  }
}
