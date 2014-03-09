package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;


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
  }

}
