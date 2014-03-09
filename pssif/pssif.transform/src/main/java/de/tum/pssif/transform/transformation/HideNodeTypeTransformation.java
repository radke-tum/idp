package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;


public class HideNodeTypeTransformation<T extends MutableNodeType> extends HideTypeTransformation<T> {
  public HideNodeTypeTransformation(T type) {
    super(type);
  }

  @Override
  public void apply(View view) {
    if (getType() != null) {
      removeType(view, getType());
    }
  }

  private void removeType(View view, NodeType type) {
    PSSIFOption<MutableNodeType> actualType = view.getMutableNodeType(type.getName());

    for (MutableNodeType mnt : actualType.getMany()) {
      for (NodeType special : mnt.getSpecials()) {
        removeType(view, special);
      }
    }

    for (MutableEdgeType et : view.getMutableEdgeTypes()) {
      for (ConnectionMapping cm : et.getMappings().getMany()) {
        for (MutableNodeType mnt : actualType.getMany()) {
          if (cm.getFrom().equals(mnt) || cm.getTo().equals(mnt)) {
            et.removeMapping(cm);
          }
        }
      }
    }

    for (MutableNodeType mnt : actualType.getMany()) {
      view.removeNodeType(mnt);
    }
  }
}
