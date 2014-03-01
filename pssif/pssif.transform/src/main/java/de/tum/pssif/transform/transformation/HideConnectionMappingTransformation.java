package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;


public class HideConnectionMappingTransformation extends AbstractTransformation {
  private final EdgeType          type;
  private final ConnectionMapping mapping;

  public HideConnectionMappingTransformation(EdgeType type, ConnectionMapping mapping) {
    this.type = type;
    this.mapping = mapping;
  }

  @Override
  public void apply(View view) {
    ViewedEdgeType actualType = view.findEdgeType(type.getName());
    NodeType fromType = view.findNodeType(mapping.getFrom().getNodeType().getName());
    NodeType toType = view.findNodeType(mapping.getTo().getNodeType().getName());
    ConnectionMapping actualMapping = actualType.getMapping(fromType, toType);
    if (actualMapping.getFrom().getNodeType().equals(fromType) && actualMapping.getTo().getNodeType().equals(toType)) {
      actualType.removeMapping(actualMapping);
    }
  }

}
