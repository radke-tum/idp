package de.tum.pssif.core.metamodel.impl.base;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;


public abstract class AbstractEdgeEnd extends AbstractNamed implements EdgeEnd {
  private final Multiplicity multiplicity;
  private final EdgeType     edge;
  private final NodeType     type;

  public AbstractEdgeEnd(String name, EdgeType edge, Multiplicity multiplicity, NodeType type) {
    super(name);
    this.edge = edge;
    this.multiplicity = multiplicity;
    this.type = type;
  }

  @Override
  public final int getEdgeEndLower() {
    return this.multiplicity.getEdgeEndLower();
  }

  @Override
  public final UnlimitedNatural getEdgeEndUpper() {
    return this.multiplicity.getEdgeEndUpper();
  }

  @Override
  public final int getEdgeTypeLower() {
    return this.multiplicity.getEdgeTypeLower();
  }

  @Override
  public final UnlimitedNatural getEdgeTypeUpper() {
    return this.multiplicity.getEdgeTypeUpper();
  }

  @Override
  public final NodeType getNodeType() {
    return type;
  }

  @Override
  public final EdgeType getEdgeType() {
    return edge;
  }

  @Override
  public final boolean includesEdgeType(int count) {
    return multiplicity.includesEdgeType(count);
  }

  @Override
  public final boolean includesEdgeEnd(int count) {
    return multiplicity.includesEdgeEnd(count);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof EdgeEnd)) {
      return false;
    }
    EdgeEnd other = (EdgeEnd) obj;
    //TODO check multiplicity here as well?
    return super.equals(obj) && this.edge.equals(other.getEdgeType()) && this.type.equals(other.getNodeType());
  }

  @Override
  public final int hashCode() {
    return getMetaType().hashCode() ^ (getNodeType().getName() + getName() + getEdgeType().getName()).hashCode();
  }

  @Override
  public final Class<?> getMetaType() {
    return EdgeEnd.class;
  }

  @Override
  public final String toString() {
    return "EdgeEnd:" + this.getName() + "(" + getNodeType() + ", " + getEdgeType() + ")";
  }
}
