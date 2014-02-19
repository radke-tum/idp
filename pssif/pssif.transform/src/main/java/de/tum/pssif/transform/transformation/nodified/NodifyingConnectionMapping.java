package de.tum.pssif.transform.transformation.nodified;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.impl.base.AbstractConnectionMapping;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class NodifyingConnectionMapping extends AbstractConnectionMapping {
  private final ConnectionMapping baseMapping;
  private final NodifiedAttribute attribute;

  public NodifyingConnectionMapping(ConnectionMapping baseMapping, NodifiedAttribute attribute) {
    super(baseMapping.getFrom(), new ToNodifiedEdgeEnd(baseMapping, attribute));
    this.baseMapping = baseMapping;
    this.attribute = attribute;
  }

  @Override
  public Edge create(Model model, Node from, Node to) {
    return baseMapping.create(model, from, to);
  }

  @Override
  public PSSIFOption<Edge> apply(Model model) {
    Collection<Edge> result = Sets.newHashSet();

    //TODO filtering needs to be enhanced here as there may be partially artificial edges?
    for (Edge candidate : baseMapping.apply(model).getMany()) {
      for (Node fromNode : baseMapping.getFrom().apply(candidate).getMany()) {
        for (Node toNode : baseMapping.getTo().apply(candidate).getMany()) {
          //TODO only add to result if not to artificial nodified instance
          if (attribute.get(fromNode).getOne().getValue().equals(toNode.getId())) {
            System.out.println("CM: Edge(" + candidate.getId() + ") -> Node(" + toNode.getId() + ")");
          }
          else {
            System.out.println("real edge, keep it");
            result.add(candidate);
          }
        }
      }
    }

    return PSSIFOption.many(result);
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
}
