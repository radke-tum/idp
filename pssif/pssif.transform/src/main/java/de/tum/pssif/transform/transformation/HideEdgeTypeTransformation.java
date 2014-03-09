package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class HideEdgeTypeTransformation extends HideTypeTransformation<MutableEdgeType> {
  public HideEdgeTypeTransformation(MutableEdgeType type) {
    super(type);
  }

  @Override
  public void apply(View view) {
    if (getType() != null) {
      removeType(view, getType());
    }
  }

  private void removeType(View view, EdgeType type) {
    PSSIFOption<MutableEdgeType> actualType = view.getMutableEdgeType(type.getName());

    for (MutableEdgeType met : actualType.getMany()) {
      for (EdgeType special : met.getSpecials()) {
        removeType(view, special);
      }
    }

    for (MutableEdgeType met : actualType.getMany()) {
      view.removeEdgeType(met);
    }
  }
}
