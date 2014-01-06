package de.tum.pssif.core.metamodel.impl;

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
  private final NodeType     type;

  public EdgeEndImpl(String name, EdgeType edge, Multiplicity multiplicity, NodeType type) {
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
  public NodeType getNodeType() {
    return type;
  }

  @Override
  public EdgeType getEdgeType() {
    return edge;
  }

  @Override
  public boolean includesEdgeType(int count) {
    return multiplicity.includesEdgeType(count);
  }

  @Override
  public boolean includesEdgeEnd(int count) {
    return multiplicity.includesEdgeEnd(count);
  }

  @Override
  public PSSIFOption<Edge> apply(Node node) {
    return new ReadConnectedOperation(this).apply(node);
  }

  @Override
  public PSSIFOption<Node> apply(Edge edge) {
    return new ReadConnectedOperation(this).apply(edge);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof EdgeEnd)) {
      return false;
    }
    EdgeEnd other = (EdgeEnd) obj;
    //TODO check multiplicity here as well?
    return super.equals(obj) && this.edge.equals(other.getEdgeType()) && this.type.equals(other.getNodeType());
  }

  @Override
  public int hashCode() {
    return getMetaType().hashCode() ^ (getNodeType().getName() + getName() + getEdgeType().getName()).hashCode();
  }

  @Override
  public Class<?> getMetaType() {
    return EdgeEnd.class;
  }

  public String toString() {
    return "EdgeEnd:" + this.getName();
  }
}
