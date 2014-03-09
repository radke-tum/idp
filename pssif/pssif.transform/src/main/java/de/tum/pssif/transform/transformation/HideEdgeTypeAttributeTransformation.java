package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class HideEdgeTypeAttributeTransformation extends HideAttributeTransformation<MutableEdgeType> {
  public HideEdgeTypeAttributeTransformation(MutableEdgeType type, Attribute attribute) {
    super(type, attribute);
  }

  @Override
  protected PSSIFOption<MutableEdgeType> getActualTarget(View view) {
    return view.getMutableEdgeType(getType().getName());
  }
}
