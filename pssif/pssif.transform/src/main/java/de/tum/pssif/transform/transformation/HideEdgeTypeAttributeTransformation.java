package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;


public class HideEdgeTypeAttributeTransformation extends HideAttributeTransformation<EdgeType, Edge> {
  public HideEdgeTypeAttributeTransformation(EdgeType type, Attribute attribute) {
    super(type, attribute);
  }

  protected ViewedEdgeType getActualTarget(View view) {
    ViewedEdgeType type = view.findEdgeType(getType().getName());
    return type;
  }
}
