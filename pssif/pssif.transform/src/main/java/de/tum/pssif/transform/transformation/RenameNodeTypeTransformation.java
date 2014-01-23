package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class RenameNodeTypeTransformation extends RenameTransformation<NodeType> {

  public RenameNodeTypeTransformation(NodeType target, String name) {
    super(target, name);
  }

  @Override
  public Metamodel apply(View view) {
    NodeType actualTarget = view.findNodeType(getTarget().getName());
    view.removeNodeType(actualTarget);
    ViewedNodeType renamed = new ViewedNodeType(actualTarget, getName());
    view.addNodeType(renamed);
    if (actualTarget.getGeneral() != null) {
      actualTarget.getGeneral().unregisterSpecialization(actualTarget);
      renamed.inherit(actualTarget.getGeneral());
    }
    //TODO move all connected edge ends to new nodetype
    return view;
  }
}
