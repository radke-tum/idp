package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.transform.transformation.joined.RightJoinedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class RightJoinConnectionMappingTransformation extends AbstractTransformation {
  private final EdgeType          baseType;
  private final ConnectionMapping baseMapping;
  private final EdgeType          joinedType;
  private final ConnectionMapping joinedMapping;
  private final ConnectionMapping targetMapping;

  public RightJoinConnectionMappingTransformation(EdgeType baseType, ConnectionMapping baseMapping, EdgeType joinedType,
      ConnectionMapping joinedMapping, ConnectionMapping targetMapping) {
    this.baseType = baseType;
    this.baseMapping = baseMapping;
    this.joinedType = joinedType;
    this.joinedMapping = joinedMapping;
    this.targetMapping = targetMapping;
  }

  @Override
  public void apply(View view) {
    ViewedEdgeType actualBaseType = view.findEdgeType(baseType.getName());
    ViewedNodeType actualFromType = view.findNodeType(baseMapping.getFrom().getNodeType().getName());
    ViewedNodeType actualToType = view.findNodeType(baseMapping.getTo().getNodeType().getName());
    ConnectionMapping actualBaseMapping = actualBaseType.getMapping(actualFromType, actualToType);
    ViewedEdgeType actualJoinedType = view.findEdgeType(joinedType.getName());
    ConnectionMapping actualJoinedMapping = actualJoinedType.getMapping(joinedMapping.getFrom().getNodeType(), joinedMapping.getTo().getNodeType());

    ViewedNodeType targetFromType = view.findNodeType(targetMapping.getFrom().getNodeType().getName());
    ViewedNodeType targetToType = view.findNodeType(targetMapping.getTo().getNodeType().getName());
    ConnectionMapping targetMapping = actualBaseType.getMapping(targetFromType, targetToType);

    actualBaseType.removeMapping(actualBaseMapping);
    actualBaseType.addMapping(new RightJoinedConnectionMapping(actualBaseType, actualBaseMapping, targetMapping, actualJoinedMapping));
  }

}
