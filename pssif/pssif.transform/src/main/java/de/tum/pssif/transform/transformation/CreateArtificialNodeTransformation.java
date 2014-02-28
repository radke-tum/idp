package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.artificial.CreateArtificialNodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class CreateArtificialNodeTransformation extends AbstractTransformation {
  private NodeType sourceType;
  private NodeType targetType;
  private EdgeType edgeType;

  public CreateArtificialNodeTransformation(NodeType sourceType, NodeType targetType, EdgeType edgeType) {
    this.sourceType = sourceType;
    this.targetType = targetType;
    this.edgeType = edgeType;
  }

  @Override
  public void apply(View view) {
    ViewedNodeType actualSourceType = view.findNodeType(sourceType.getName());
    ViewedNodeType actualTargetType = view.findNodeType(targetType.getName());
    ViewedEdgeType actualEdgeType = view.findEdgeType(edgeType.getName());

    view.removeNodeType(actualSourceType);
    CreateArtificialNodeType newType = new CreateArtificialNodeType(actualSourceType, actualTargetType, actualEdgeType);
    if (actualSourceType.getGeneral() != null) {
      newType.inherit(actualSourceType.getGeneral());
    }
    for (NodeType special : actualSourceType.getSpecials()) {
      special.inherit(newType);
    }
    view.addNodeType(newType);
  }

}
