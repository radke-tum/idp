package de.tum.pssif.transform.metamodel.alternatives;

import de.tum.pssif.core.metamodel.NodeType;


public class NodeTypeRenamingTransformation extends RenamingTransformation<NodeType> {

  public NodeTypeRenamingTransformation(NodeType toRename, String newName) {
    super(toRename, newName);
  }

  @Override
  protected NodeType getRenamedType() {
    WrappingNodeType nt = new WrappingNodeType(getTypeToRename());
    nt.setName(getNewName());
    return nt;
  }

}
