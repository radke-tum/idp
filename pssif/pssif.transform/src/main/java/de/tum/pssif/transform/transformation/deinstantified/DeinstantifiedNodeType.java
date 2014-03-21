package de.tum.pssif.transform.transformation.deinstantified;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class DeinstantifiedNodeType extends ViewedNodeType {

  public DeinstantifiedNodeType(NodeType baseType) {
    super(baseType);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubtypes) {
    return PSSIFOption.none();
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id, boolean includeSubtypes) {
    return PSSIFOption.none();
  }
}
