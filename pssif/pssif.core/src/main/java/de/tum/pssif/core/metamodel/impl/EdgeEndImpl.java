package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public final class EdgeEndImpl extends NamedImpl implements EdgeEnd {
  private final Multiplicity multiplicity;
  private final EdgeType     edge;
  private final NodeTypeImpl type;

  public EdgeEndImpl(String name, EdgeType edge, Multiplicity multiplicity, NodeTypeImpl type) {
    super(name);
    this.edge = edge;
    this.multiplicity = multiplicity;
    this.type = type;
  }

  @Override
  public int getEdgeEndLower() {
    return this.multiplicity.getEdgeEndLower();
  }

  @Override
  public UnlimitedNatural getEdgeEndUpper() {
    return this.multiplicity.getEdgeEndUpper();
  }

  @Override
  public int getEdgeTypeLower() {
    return this.multiplicity.getEdgeTypeLower();
  }

  @Override
  public UnlimitedNatural getEdgeTypeUpper() {
    return this.multiplicity.getEdgeTypeUpper();
  }

  @Override
  public Collection<NodeType> getTypes() {
    return Sets.<NodeType> newHashSet(type);
  }

  public NodeTypeImpl getNodeType() {
    return this.type;
  }

  @Override
  public EdgeType getType() {
    return edge;
  }

  @Override
  public PSSIFOption<Node> nodes(PSSIFOption<Edge> edges) {
    PSSIFOption<Node> result = PSSIFOption.none();
    for (Edge edge : edges.getMany()) {
      result = PSSIFOption.merge(result, edge.get(this));
    }
    return result;
  }

  @Override
  public PSSIFOption<Edge> edges(PSSIFOption<Node> nodes) {
    PSSIFOption<Edge> result = PSSIFOption.none();
    for (Node node : nodes.getMany()) {
      result = PSSIFOption.merge(result, node.get(this));
    }
    return result;
  }
}
