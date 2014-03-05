package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class AliasNodeTypeTransformation extends RenameTransformation<NodeType> {

  public AliasNodeTypeTransformation(NodeType target, String name) {
    super(target, name);
  }

  @Override
  public void apply(View view) {
    ViewedNodeType actualTargetType = view.findNodeType(getTarget().getName());
    ViewedNodeType type = new ViewedNodeType(actualTargetType, getName());
    type.inherit(actualTargetType.getGeneral());
    view.addNodeType(type);
  }

}
