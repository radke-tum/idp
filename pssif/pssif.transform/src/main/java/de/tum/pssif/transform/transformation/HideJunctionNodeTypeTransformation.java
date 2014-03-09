package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;


public class HideJunctionNodeTypeTransformation<T extends MutableJunctionNodeType> extends HideTypeTransformation<T> {
  public HideJunctionNodeTypeTransformation(T type) {
    super(type);
  }

  @Override
  public void apply(View view) {
    if (getType() != null) {
      removeType(view, getType());
    }
  }

  private void removeType(View view, JunctionNodeType type) {
    PSSIFOption<MutableJunctionNodeType> actualType = view.getMutableJunctionNodeType(type.getName());

    for (MutableEdgeType et : view.getMutableEdgeTypes()) {
      for (ConnectionMapping cm : et.getMappings().getMany()) {
        for (MutableJunctionNodeType mnt : actualType.getMany()) {
          if (cm.getFrom().equals(mnt) || cm.getTo().equals(mnt)) {
            et.removeMapping(cm);
          }
        }
      }
    }

    for (MutableJunctionNodeType mnt : actualType.getMany()) {
      view.removeJunctionNodeType(mnt);
    }
  }
}
