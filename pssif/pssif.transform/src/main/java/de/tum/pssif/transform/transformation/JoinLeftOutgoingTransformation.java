package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.joined.LeftOutgoingJoinedConnectionMapping;


public class JoinLeftOutgoingTransformation extends AbstractTransformation {
  private final String            type;
  private final ConnectionMapping joinedMapping;
  private final String            inner;
  private final String            to;

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
  public JoinLeftOutgoingTransformation(EdgeType type, ConnectionMapping joinedMapping, NodeType inner, NodeType to) {
    this.type = type.getName();
    this.joinedMapping = joinedMapping;
    this.inner = inner.getName();
    this.to = to.getName();
  }

  @Override
  public void apply(Viewpoint view) {
    MutableEdgeType actualType = view.getMutableEdgeType(type).getOne();
    NodeType actualInner = view.getNodeType(inner).getOne();
    NodeType actualTo = view.getNodeType(to).getOne();

    actualType.addMapping(new LeftOutgoingJoinedConnectionMapping(actualType.getMapping(actualInner, actualTo).getOne(), actualType, actualInner,
        actualTo, joinedMapping));
  }
}
