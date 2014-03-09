package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.joined.RightOutgoingJoinedConnectionMapping;


public class JoinRightOutgoingTransformation extends AbstractTransformation {
  private final String            type;
  private final ConnectionMapping joinedMapping;
  private final String            from;
  private final String            inner;

  /**
   * The joinedMapping is not re-fetched from the view when applying this transformation 
   * because we may need a joined type out of a previous view without having artificial edges removed
   * 
   * @param type
   * @param joinedType
   * @param from
   * @param inner
   * @param to
   */
  public JoinRightOutgoingTransformation(EdgeType type, ConnectionMapping joinedMapping, NodeType from, NodeType inner) {
    this.type = type.getName();
    this.joinedMapping = joinedMapping;
    this.from = from.getName();
    this.inner = inner.getName();
  }

  @Override
  public void apply(View view) {
    MutableEdgeType actualType = view.getMutableEdgeType(type).getOne();
    NodeType actualFrom = view.getNodeType(from).getOne();
    NodeType actualInner = view.getNodeType(inner).getOne();

    actualType.addMapping(new RightOutgoingJoinedConnectionMapping(actualType.getMapping(actualFrom, actualInner).getOne(), actualType, actualFrom,
        actualInner, joinedMapping));
  }
}
