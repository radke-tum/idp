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
  private final EdgeType                edge;
  private final Collection<EdgeEndImpl> bundled;

  public EdgeEndBundle(String name, EdgeType edge, Collection<EdgeEndImpl> bundled) {
    super(name);
    this.edge = edge;
    this.bundled = Collections.unmodifiableCollection(bundled);
  }

  @Override
  public int getEdgeEndLower() {
    int result = Integer.MAX_VALUE;

    for (EdgeEnd end : bundled) {
      result = Math.min(end.getEdgeEndLower(), result);
    }

    return result;
  }

  @Override
  public UnlimitedNatural getEdgeEndUpper() {
    UnlimitedNatural result = UnlimitedNatural.of(0);

    for (EdgeEnd end : bundled) {
      result = UnlimitedNatural.max(result, end.getEdgeEndUpper());
    }

    return result;
  }

  @Override
  public int getEdgeTypeLower() {
    int result = Integer.MAX_VALUE;

    for (EdgeEnd end : bundled) {
      result = Math.min(end.getEdgeTypeLower(), result);
    }

    return result;
  }

  @Override
  public UnlimitedNatural getEdgeTypeUpper() {
    UnlimitedNatural result = UnlimitedNatural.of(0);

    for (EdgeEnd end : bundled) {
      result = UnlimitedNatural.max(result, end.getEdgeTypeUpper());
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

  @Override
  public boolean includesEdgeType(int count) {
    for (EdgeEnd end : bundled) {
      if (end.includesEdgeType(count)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean includesEdgeEnd(int count) {
    for (EdgeEnd end : bundled) {
      if (end.includesEdgeEnd(count)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean equals(String name, NodeType type) {
    for (EdgeEnd end : bundled) {
      if (end.equals(name, type)) {
        return true;
      }
    }
    return false;
  }
}
