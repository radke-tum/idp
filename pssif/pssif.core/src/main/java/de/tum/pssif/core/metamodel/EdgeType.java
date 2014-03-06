package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.common.PSSIFOption;


public interface EdgeType extends ElementType<EdgeType> {
  PSSIFOption<ConnectionMapping> getMappings();

  PSSIFOption<ConnectionMapping> getMapping(NodeType from, NodeType to);

  PSSIFOption<ConnectionMapping> getOutgoingMappings(NodeType from);

  PSSIFOption<ConnectionMapping> getIncomingMappings(NodeType to);
}
