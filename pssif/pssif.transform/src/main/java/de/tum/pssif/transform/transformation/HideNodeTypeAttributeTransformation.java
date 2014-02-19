package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;


public class HideNodeTypeAttributeTransformation extends HideAttributeTransformation<NodeType, Node> {
  public HideNodeTypeAttributeTransformation(NodeType type, Attribute attribute) {
    super(type, attribute);
  }

  @Override
  protected NodeType getActualTarget(View view) {
    return view.findNodeType(getType().getName());
  }
}
