package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.moved.CopiedAttribute;
import de.tum.pssif.transform.transformation.moved.MovedAttribute;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class MoveAttributeTransformation extends AbstractTransformation {
  private final NodeType  sourceType;
  private final String    attributeName;
  private final NodeType  targetType;
  private final Attribute targetAttribute;
  private final EdgeType  edgeType;

  public MoveAttributeTransformation(NodeType sourceType, String name, NodeType targetType, Attribute targetAttribute, EdgeType edgeType) {
    this.sourceType = sourceType;
    attributeName = name;
    this.targetType = targetType;
    this.targetAttribute = targetAttribute;
    this.edgeType = edgeType;
  }

  @Override
  public void apply(View view) {
    ViewedNodeType actualSourceType = view.findNodeType(sourceType.getName());
    ViewedNodeType actualTargetType = view.findNodeType(targetType.getName());
    ViewedEdgeType actualEdgeType = view.findEdgeType(edgeType.getName());
    Attribute actualTargetAttribute = actualTargetType.findAttribute(targetAttribute.getName());

    AttributeGroup defaultGroup = actualSourceType.findAttributeGroup(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME);
    ConnectionMapping mapping = actualEdgeType.getMapping(actualSourceType, actualTargetType);

    Attribute sourceAttribute = actualSourceType.findAttribute(attributeName);
    if (sourceAttribute != null) {
      actualSourceType.removeAttribute(sourceAttribute);
      actualSourceType.add(defaultGroup, new CopiedAttribute(attributeName, sourceAttribute, actualTargetAttribute, mapping));
    }
    else {
      actualSourceType.add(defaultGroup, new MovedAttribute(attributeName, actualTargetAttribute, mapping));
    }
  }
}
