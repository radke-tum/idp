package de.tum.pssif.core.model.impl;

import de.tum.pssif.core.exception.PSSIFIllegalAccessException;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.JunctionNode;


public class JunctionNodeImpl extends NodeImpl implements JunctionNode {
  private EdgeTypeSignature edgeTypeSignature;
  private boolean           edgeTypeSignatureInitialized = false;

  @Override
  public void initializeEdgeTypeSignature(EdgeType edgeType) {
    if (edgeTypeSignatureInitialized && !isEdgeTypeCompatible(edgeType)) {
      throw new PSSIFIllegalAccessException("EdgeTypeSignature was already initialized");
    }
    edgeTypeSignature = new EdgeTypeSignature(edgeType);
    edgeTypeSignatureInitialized = true;
  }

  @Override
  public boolean isEdgeTypeCompatible(EdgeType edgeType) {
    if (edgeTypeSignatureInitialized) {
      return edgeTypeSignature.isCompatibleWith(edgeType);
    }
    else {
      return true;
    }
  }
}
