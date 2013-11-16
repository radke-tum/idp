package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeEndBundle extends NamedImpl implements EdgeEnd {
  private final EdgeType            edge;
  private final Collection<EdgeEnd> bundled;

  public EdgeEndBundle(String name, EdgeType edge, Collection<EdgeEnd> bundled) {
    super(name);
    this.edge = edge;
    this.bundled = Collections.unmodifiableCollection(bundled);
  }

  @Override
  public int getLower() {
    int result = Integer.MAX_VALUE;

    for (EdgeEnd end : bundled) {
      result = Math.min(end.getLower(), result);
    }

    return result;
  }

  @Override
  public UnlimitedNatural getUpper() {
    UnlimitedNatural result = UnlimitedNatural.of(0);

    for (EdgeEnd end : bundled) {
      result = UnlimitedNatural.max(result, end.getUpper());
    }

    return result;
  }

  @Override
  public Collection<NodeType> getTypes() {
    Collection<NodeType> result = Sets.newHashSet();

    for (EdgeEnd type : bundled) {
      result.addAll(type.getTypes());
    }

    return result;
  }

  @Override
  public EdgeType getType() {
    return edge;
  }

  @Override
  public PSSIFOption<Node> nodes(PSSIFOption<Edge> edges) {
    PSSIFOption<Node> result = PSSIFOption.none();
    for (EdgeEnd end : bundled) {
      result = PSSIFOption.merge(result, end.nodes(edges));
    }
    return result;
  }

  @Override
  public PSSIFOption<Edge> edges(PSSIFOption<Node> nodes) {
    PSSIFOption<Edge> result = PSSIFOption.none();
    for (EdgeEnd end : bundled) {
      result = PSSIFOption.merge(result, end.edges(nodes));

    }
    return result;
  }

}
