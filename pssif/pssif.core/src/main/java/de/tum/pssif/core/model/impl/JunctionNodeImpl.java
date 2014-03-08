package de.tum.pssif.core.model.impl;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.JunctionNode;


public class JunctionNodeImpl extends NodeImpl implements JunctionNode {
  private static final String EDGE_TYPE_SIGNATURE_ANNOTATION_KEY = "JunctionNodeEdgeTypeSignature";

  @Override
  public void initializeEdgeTypeSignature(EdgeType edgeType) {
    annotate(EDGE_TYPE_SIGNATURE_ANNOTATION_KEY, edgeType.getName());
  }

  @Override
  public boolean isEdgeTypeCompatible(EdgeType edgeType) {
    boolean result = true;

    for (String s : getAnnotation(EDGE_TYPE_SIGNATURE_ANNOTATION_KEY).getMany()) {
      result = s.equals(edgeType.getName());
    }

    return result;
  }
}
