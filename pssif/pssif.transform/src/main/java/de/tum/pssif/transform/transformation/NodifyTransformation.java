package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.nodified.NodifiedAttribute;
import de.tum.pssif.transform.transformation.nodified.NodifiedNodeType;
import de.tum.pssif.transform.transformation.nodified.NodifyingConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class NodifyTransformation extends AbstractTransformation {
  private final NodeType sourceType;
  private final NodeType targetType;
  private final EdgeType edgeType;
  private final String   attributeName;

  public NodifyTransformation(NodeType sourceType, NodeType targetType, EdgeType edgeType, String attributeName) {
    this.sourceType = sourceType;
    this.targetType = targetType;
    this.edgeType = edgeType;
    this.attributeName = attributeName;
  }

  @Override
  public void apply(View view) {
    ViewedNodeType actualSourceType = view.findNodeType(sourceType.getName());
    ViewedNodeType actualTargetType = view.findNodeType(targetType.getName());
    ViewedEdgeType actualEdgeType = view.findEdgeType(edgeType.getName());

    AttributeGroup defaultGroup = actualSourceType.findAttributeGroup(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME);
    ConnectionMapping mapping = actualEdgeType.getMapping(actualSourceType, actualTargetType);
    NodifiedAttribute attribute = new NodifiedAttribute(attributeName, actualTargetType, actualEdgeType, mapping);
    actualSourceType.add(defaultGroup, attribute);

    actualEdgeType.removeMapping(mapping);
    actualEdgeType.addMapping(new NodifyingConnectionMapping(mapping, attribute));

    view.removeNodeType(actualTargetType);
    view.addNodeType(new NodifiedNodeType(actualTargetType, mapping, attribute));
  }
}
