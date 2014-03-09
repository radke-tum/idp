package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.artificial.ArtificializedConnectionMapping;
import de.tum.pssif.transform.transformation.artificial.ArtificializedNodeType;
import de.tum.pssif.transform.transformation.artificial.ArtificializingNodeType;


public class CreateArtificialNodeTransformation extends AbstractTransformation {
  private NodeType sourceType;
  private EdgeType edgeType;
  private NodeType targetType;

  public CreateArtificialNodeTransformation(NodeType sourceType, EdgeType edgeType, NodeType targetType) {
    this.sourceType = sourceType;
    this.edgeType = edgeType;
    this.targetType = targetType;
  }

  @Override
  public void apply(View view) {
    NodeType actualSourceType = view.getNodeType(sourceType.getName()).getOne();
    NodeType actualTargetType = view.getNodeType(targetType.getName()).getOne();
    MutableEdgeType actualEdgeType = view.getMutableEdgeType(edgeType.getName()).getOne();

    ConnectionMapping mapping = actualEdgeType.getMapping(actualSourceType, actualTargetType).getOne();
    if (mapping.getFrom().equals(actualSourceType) && mapping.getTo().equals(actualTargetType)) {
      //replace the mapping
      actualEdgeType.removeMapping(mapping);
    }
    actualEdgeType.addMapping(new ArtificializedConnectionMapping(mapping, actualEdgeType, actualSourceType, actualTargetType));

    ArtificializedNodeType artificialized = new ArtificializedNodeType(actualSourceType, actualEdgeType, actualTargetType);
    for (NodeType general : actualTargetType.getGeneral().getMany()) {
      artificialized.inherit(general);
    }
    for (NodeType special : actualTargetType.getSpecials()) {
      special.inherit(artificialized);
    }

    ArtificializingNodeType artificializing = new ArtificializingNodeType(actualSourceType, actualEdgeType, actualTargetType);
    for (NodeType general : actualSourceType.getGeneral().getMany()) {
      artificializing.inherit(general);
    }
    for (NodeType special : actualSourceType.getSpecials()) {
      special.inherit(artificializing);
    }

    view.removeNodeType(actualTargetType);
    view.add(artificialized);
    view.removeNodeType(actualSourceType);
    view.add(artificializing);
  }
}
