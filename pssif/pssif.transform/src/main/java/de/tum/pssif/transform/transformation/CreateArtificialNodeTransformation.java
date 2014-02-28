package de.tum.pssif.transform.transformation;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.artificial.ArtificializedNodeType;
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
    view.removeNodeType(actualTargetType);

    ArtificializedNodeType artificialized = new ArtificializedNodeType(actualSourceType, actualEdgeType, actualTargetType);
    if (actualTargetType.getGeneral() != null) {
      artificialized.inherit(actualTargetType.getGeneral());
    }
    Collection<NodeType> specials = Sets.newHashSet(actualTargetType.getSpecials());
    for (NodeType special : specials) {
      special.inherit(artificialized);
    }
    view.addNodeType(artificialized);

    CreateArtificialNodeType newType = new CreateArtificialNodeType(actualSourceType, actualTargetType, actualEdgeType);
    if (actualSourceType.getGeneral() != null) {
      newType.inherit(actualSourceType.getGeneral());
    }
    specials = Sets.newHashSet(actualSourceType.getSpecials());
    for (NodeType special : specials) {
      special.inherit(newType);
    }
    view.addNodeType(newType);
  }

}
