package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.joined.LeftOutgoingJoinedConnectionMapping;


public class JoinLeftOutgoingTransformation extends AbstractTransformation {
  private final EdgeType type;
  private final EdgeType joinedType;
  private final NodeType from;
  private final NodeType inner;
  private final NodeType to;

  public JoinLeftOutgoingTransformation(EdgeType type, EdgeType joinedType, NodeType from, NodeType inner, NodeType to) {
    this.type = type;
    this.joinedType = joinedType;
    this.from = from;
    this.inner = inner;
    this.to = to;
  }

  @Override
  public void apply(View view) {
    MutableEdgeType actualType = view.getMutableEdgeType(type.getName()).getOne();
    MutableEdgeType actualJoinedType = view.getMutableEdgeType(joinedType.getName()).getOne();
    NodeType actualFrom = view.getNodeType(from.getName()).getOne();
    NodeType actualInner = view.getNodeType(inner.getName()).getOne();
    NodeType actualTo = view.getNodeType(to.getName()).getOne();

    //remove an eventually existing mapping to avoid ambiguity
    PSSIFOption<ConnectionMapping> existing = actualType.getMapping(actualFrom, actualTo);
    if (existing.isOne() && existing.getOne().getFrom().equals(actualFrom) && existing.getOne().getTo().equals(actualTo)) {
      actualType.removeMapping(existing.getOne());
    }
    actualType.addMapping(new LeftOutgoingJoinedConnectionMapping(actualType.getMapping(actualInner, actualTo).getOne(), actualType, actualInner,
        actualTo, actualJoinedType.getMapping(actualInner, actualFrom).getOne()));
  }
}
